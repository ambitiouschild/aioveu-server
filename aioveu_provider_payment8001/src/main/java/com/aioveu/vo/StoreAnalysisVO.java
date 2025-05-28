package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:14
 */
@Data
public class StoreAnalysisVO {

    /**
     * 订单数  一段时间内的付费订单数（可查明细）
     */
    private int orderNumber;

    /**
     * 订单金额 一段时间内付费订单的总金额
     */
    private BigDecimal amount;

    /**
     * VIP订单金额
     */
    private BigDecimal vipActiveAmount;

    /**
     * VIP订单数量
     */
    private int vipActiveOrderNumber;

    //-----订场收款相关统计 start------------

    /**
     * 订场总订单数 指定时间范围内的订单数 包含待使用 使用中 已完成和退款订单
     */
    private int fieldOrderNumber;

    /**
     * 在线支付订单数 指定范围内在线支付的订单数 包含待使用 使用中 已完成和退款订单
     */
    private int onlineFieldOrderNumber;

    /**
     * 会员卡支付订单数 指定范围内会员卡支付的订单数 包含待使用 使用中 已完成和退款订单
     */
    private int vipFieldOrderNumber;

    /**
     * 订场总金额 指定时间范围内的订单总金额 包含待使用 使用中 已完成和退款订单
     */
    private BigDecimal fieldOrderAmount;

    /**
     * 在线支付订单总金额 指定范围内在线支付的订单数 包含待使用 使用中 已完成和退款订单
     */
    private BigDecimal onlineFieldOrderAmount;

    /**
     * 会员卡支付订单总金额 指定范围内会员卡支付的订单数 包含待使用 使用中 已完成和退款订单
     */
    private BigDecimal vipFieldOrderAmount;

    /**
     * 订场订单退款数 指定时间范围内退款订单数量
     */
    private int refundFieldOrderNumber;

    /**
     * 订场订单退款总金额 指定时间范围内退款订单总金额
     */
    private BigDecimal refundFieldOrderAmount;

    /**
     * 实收金额(微信支付结算到账的金额) 指定时间范围内实际下单的金额(支付成功待使用、使用中、完成 退款) - 当日退款金额
     */
    private BigDecimal actualFieldOrderAmount;

    //-----订场收款相关统计 end------------


    //-----订场使用相关统计 start------------
    /**
     * 订场实际完成的总金额 (指定时间范围内完成消费的订场对应的金额)
     */
    private BigDecimal fieldActiveAmount;

    /**
     * 订场实际完成的在线支付的金额
     */
    private BigDecimal fieldOnlineActiveAmount;

    /**
     * 订场实际完成的会员卡支付的金额
     */
    private BigDecimal fieldVipActiveAmount;

    /**
     * 未完成/未激活订单金额：指定时间范围内的订场订单
     */
    private BigDecimal unActiveAmount;

    /**
     * 未完成的在线支付的订单金额
     */
    private BigDecimal unActiveOnlineAmount;

    /**
     * 未完成的会员卡的订单金额
     */
    private BigDecimal unActiveVipActiveAmount;

    //-----订场使用相关统计 end------------

    //-----订场会员卡统计 start------------
    /**
     * VIP后台调整金额
     */
    private BigDecimal vipBackendAmount;

    /**
     * 会员卡金额
     */
    private Double vipCardAmount;

    //-----订场会员卡统计 end------------

    /**
     * 指定时间内，班级约课所使用的课券数量
     */
    private int couponUsedNumber;

    /**
     * 指定时间内，班级约课所使用的课券金额
     *
     */
    private BigDecimal couponUsedAmount;
    /**
     * 已签到的课程核销课券
     */
    private BigDecimal couponUsedAndSignAmount;


    /**
     * 优惠券未核销数量  未核销课程券数  全部剩余课程券
     */
    private int couponUnUsedNumber;

    /**
     * 优惠券未核销的金额数  全部剩余未核销券的总金额
     */
    private BigDecimal couponUnUsedAmount;

    /**
     * 统计店铺取消约课次数
     */
    private int storeCancelCount;

    /**
     * 取消班级数量返还课券金额：家长取消课和老师取消课返还的所有课程券金额累加（可查明细）
     */
    private BigDecimal storeCancelAmount;

    /**
     * 体验课数量
     */
    private int experienceNumber;

    /**
     * 用户取消约课数量
     */
    private int userCancelCount;

    /**
     * 用户取消约课返还课券金额
     */
    private BigDecimal userCancelAmount;

    /**
     * 已完成班级数量
     */
    private int hasClassGradeNum;

    /**
     * 有签到的班级数量
     */
    private int hasSignUserGradeNum;

    /**
     * 班级签到人数
     */
    private int gradeSignUserNum;
    /**
     * 班级约课人数
     */
    private int gradeEnrollUserNum;

    /**
     * 班级已课评人数
     */
    private int gradeCommentUserNum;

    /**
     * 新签续费
     */
    private Map<String, StoreAnalysisItemVO> renewalMap;

    /**
     * 教练满课率
     * 例如有2个班
     * 班级1:班级最多人数2人，当前报名1人，满班率为50%
     * 班级2:班级最多人数2人，当前报名2人，满班率为100%
     * 教练满班率 = (50*1+100*1)/2 = 75%
     *
     * 有5个班
     * 班级1:班级最多人数2人，当前报名1人，满班率为50%
     * 班级2:班级最多人数2人，当前报名2人，满班率为100%
     * 班级3:班级最多人数1人，当前报名0人，满班率为0%
     * 班级4:班级最多人数4人，当前报名3人，满班率为75%
     * 班级5:班级最多人数2人，当前报名1人，满班率为50%
     * 总的满班率 = (50*2+100*1+0*1+75*1)/5 = 55%
     */
    private BigDecimal fullGradeRate;

    /**
     * 教练多人班级满班率
     * 同fullGradeRate计算逻辑，但要去掉限制人数=1的班级。
     */
    private BigDecimal multiUserFullGradeRate;
    /**
     * 体验率
     */
    private BigDecimal experienceRate;

    /**
     * 成单率
     */
    private BigDecimal completionRate;

    /**
     * 跟进率
     */
    private BigDecimal followRate;

    /**
     * 新客资总数
     */
    private Integer newUserTotal;

    /**
     * 班级总时长
     */
    private BigDecimal gradeTotalHours;

    /**
     * 班级场地总金额
     */
    private BigDecimal gradeTotalAmount;
    /**
     * 班级使用场地总时长
     */
    private BigDecimal gradeUsedFieldTotalHours;

    /**
     * 班级使用场地总金额
     */
    private BigDecimal gradeUsedFieldTotalAmount;

    /**
     * 手动核销券数
     */
    private Integer verifyCouponNumber;

    /**
     * 手动核销券金额
     */
    private BigDecimal verifyCouponAmount;

}
