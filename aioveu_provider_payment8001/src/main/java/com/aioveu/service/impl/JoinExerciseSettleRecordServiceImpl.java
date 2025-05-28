package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.JoinExerciseSettleRecordDao;
import com.aioveu.entity.Exercise;
import com.aioveu.entity.JoinExerciseRule;
import com.aioveu.entity.JoinExerciseSettleRecord;
import com.aioveu.entity.Order;
import com.aioveu.enums.DelayMessageType;
import com.aioveu.enums.OrderStatus;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author： yao
 * @Date： 2024/10/8 09:09
 * @Describe：
 */
@Slf4j
@Service
public class JoinExerciseSettleRecordServiceImpl extends ServiceImpl<JoinExerciseSettleRecordDao, JoinExerciseSettleRecord> implements JoinExerciseSettleRecordService {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private JoinExerciseRuleService joinExerciseRuleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Override
    public JoinExerciseSettleRecord getByExerciseId(String exerciseId) {
        QueryWrapper<JoinExerciseSettleRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JoinExerciseSettleRecord::getExerciseId,exerciseId);
        return getOne(queryWrapper);
    }

    /**
     * 结算拼单
     * 采用分布式锁
     * 1、判断该拼单产品id是否结算过（查询结算记录表）
     *      结算过，不处理
     *      未结算，继续执行
     * 2、根据产品id获取拼单产品信息、拼单规则配置
     * 3、判断消息时间与产品结算时间（活动结束时间）是否一致
     *      消息时间 >= 结算时间，继续执行
     *      消息时间 < 结算时间，重新往延迟队列插入数据，延迟到结算时间，结束。
     * 4、根据产品id、分类id获取所有成功的订单
     * 5、根据订单数，按照拼单规则，判断，是否满足拼单人数
     *      满足，走返现逻辑。
     *      不满足，则不处理, 打印日志，结束。
     * 6、返现逻辑 = 用户实际下单金额 - 拼单规则对应金额，
     *      > 0 ,返给用户对应金额到【我的】-【余额】，并记录返现明细。
     *      <= 0 ,打印日志，不处理，结束。
     * @param exerciseId  拼单产品id
     * @param categoryId  拼单产品分类
     * @param endTime    拼单结算时间（拼单活动结束时间）
     * @param messageId mq消息id
     */
    @Override
    public void settleJoinOrder(String exerciseId, Long categoryId, Date endTime, String messageId) {
        log.info("messageId:{},拼单产品：{},活动结算时间：{} ，开始返现结算！", messageId, exerciseId, endTime);
        JoinExerciseSettleRecord joinRecord = getByExerciseId(exerciseId);
        if (joinRecord != null && joinRecord.getTotalNumber() > 0 && joinRecord.getTotalNumber().equals(joinRecord.getSettleNumber())){
            log.info("messageId:{},拼单产品：{} ，该产品已经结算完毕，本次不处理！", messageId, exerciseId);
            return;
        }
        Exercise exercise = exerciseService.getById(exerciseId);
        if (exercise == null || (exercise.getStatus() != 1 && exercise.getStatus() != 10)) {
            log.info("拼单产品：{} ,未查询到该拼单产品或者已下架，不处理！", exerciseId);
            return;
        }
        if (exercise.getExerciseEndTime().after(endTime)) {
            log.info("拼单产品：{} ,名称 = {}，活动结束时间{}大于延迟执行时间，不处理！", exerciseId, exercise.getName(), exercise.getExerciseEndTime());
            //重新写入延时队列
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("type", DelayMessageType.JOIN_EXERCISE_FINISH.getCode());
            msgMap.put("exerciseId", exercise.getId());
            msgMap.put("categoryId", exercise.getCategoryId());
            msgMap.put("endTime", exercise.getExerciseEndTime());
            mqMessageService.sendDelayMsgByDate(msgMap, exercise.getExerciseEndTime());
            return;
        }
        List<JoinExerciseRule> rules = joinExerciseRuleService.getByExerciseId(exerciseId);
        if (CollectionUtils.isEmpty(rules)) {
            log.info("拼单产品：{} ,名称 = {}，未配置拼单规则，不处理！", exercise.getId(), exercise.getName());
            return;
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getProductId, exerciseId)
                .eq(Order::getCategoryId, categoryId)
                .in(Order::getStatus, OrderStatus.PAY.getCode(), OrderStatus.USING.getCode())
                .select(Order::getId, Order::getActualAmount, Order::getUserId, Order::getCategoryId, Order::getAppId, Order::getCount, Order::getAmount);
        List<Order> orders = orderService.list(queryWrapper);
        if (CollectionUtils.isEmpty(orders)) {
            log.info("拼单产品：{} ,名称 = {}，未查到订单，不处理！", exercise.getId(), exercise.getName());
            return;
        }
        log.info("拼单产品：{} ,本次退款订单数 = {}", exercise.getName(), orders.size());
        if (joinRecord == null) {
            JoinExerciseRule joinExerciseRule = rules.stream()
                    .sorted(Comparator.comparing(JoinExerciseRule::getJoinNumber).reversed())
                    .filter(item -> orders.size() >= item.getJoinNumber())
                    .findFirst().orElse(null);
            if (joinExerciseRule == null) {
                log.info("拼单产品：{} ,名称 = {}，订单数量={},未达到规则数量，不处理！", exercise.getId(), exercise.getName(), orders.size());
                return;
            }
            log.info("拼单产品：{} ,达到规则数量={}, 拼单价格={}", exercise.getId(), joinExerciseRule.getJoinNumber(),joinExerciseRule.getJoinPrice());

            joinRecord = new JoinExerciseSettleRecord();
            joinRecord.setSettleAmount(BigDecimal.ZERO);
            joinRecord.setSettleNumber(0);
            joinRecord.setMessageId(messageId);
            joinRecord.setExerciseId(exerciseId);
            joinRecord.setExerciseName(exercise.getName());
            joinRecord.setStoreId(exercise.getStoreId());
            joinRecord.setCategoryId(categoryId);
            joinRecord.setTotalNumber(orders.size());
            joinRecord.setExerciseEndDate(exercise.getExerciseEndTime());
            joinRecord.setSingleAmount(joinExerciseRule.getJoinPrice());

            BigDecimal totalAmount = new BigDecimal(0);
            for (Order order : orders) {
                int orderCount = order.getCount();
                BigDecimal subtract = order.getActualAmount().subtract(joinExerciseRule.getJoinPrice().multiply(BigDecimal.valueOf(orderCount)));
                if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                    totalAmount = totalAmount.add(subtract);
                }
            }
            joinRecord.setTotalAmount(totalAmount);
            if (!save(joinRecord)) {
                log.info("messageId:{},拼单产品：{} ，插入返现结算表失败！", messageId, exerciseId);
            }
        } else {
            log.info("拼单产品：{} ,达到规则数量={}, 拼单价格={}", exercise.getId(), joinRecord.getTotalNumber(), joinRecord.getSingleAmount());
        }

        BigDecimal settleAmount = joinRecord.getSettleAmount();
        Integer settleCount = 0;
        try {
            for (Order order : orders) {
                int orderCount = Math.max(1, order.getCount());
                BigDecimal subtract = order.getActualAmount().subtract(joinRecord.getSingleAmount().multiply(BigDecimal.valueOf(orderCount)));
                log.info("拼单产品：{} ,order_id:{}, 实付：{}, 返现={}", exercise.getId(), order.getId(), order.getActualAmount(), subtract);
                if (subtract.compareTo(BigDecimal.ZERO) > 0 && subtract.doubleValue() > 0) {
//                    userService.addBalance(order.getUserId(), subtract, "拼单返现", "拼单返现结算" + subtract, order.getId());
                    order.setCanRefundAmount(subtract);
                    // 实付金额 要减去 退款金额
                    order.setActualAmount(order.getActualAmount().subtract(subtract));
                    if (orderService.orderRefund(order, "拼单返现", false)) {
                        settleAmount = settleAmount.add(subtract);
                        settleCount++;
                        iUserCouponService.recalculateAmount(order.getId(), order.getActualAmount());
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            //TODO 异常通知 发生异常 结算中断 后续人工处理
        }
        // 更新结算信息
        JoinExerciseSettleRecord jr  = new JoinExerciseSettleRecord();
        jr.setId(joinRecord.getId());
        jr.setSettleNumber(settleCount + joinRecord.getSettleNumber());
        jr.setSettleAmount(settleAmount);
        updateById(jr);
        log.info("拼单产品：{} ，结束返现结算,总人数={},总金额={}！", exerciseId, settleCount, settleAmount);
    }

}
