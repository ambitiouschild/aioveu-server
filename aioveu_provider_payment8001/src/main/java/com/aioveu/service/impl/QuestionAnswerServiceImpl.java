package com.aioveu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.RedisConstant;
import com.aioveu.constant.ResultEnum;
import com.aioveu.dao.QuestionAnswerDao;
import com.aioveu.dto.ShareConfigDTO;
import com.aioveu.entity.*;
import com.aioveu.exception.SportException;
import com.aioveu.form.QuestionAnswerForm;
import com.aioveu.form.QuestionAnswerSelectForm;
import com.aioveu.service.*;
import com.aioveu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class QuestionAnswerServiceImpl extends ServiceImpl<QuestionAnswerDao, QuestionAnswer> implements QuestionAnswerService {

    @Autowired
    private FormEnrollService formEnrollService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollQuestionService enrollQuestionService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private UserViewPositionService userViewPositionService;

    @Autowired
    private ShareConfigService shareConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PosterService posterService;

    @Override
    public String enroll(QuestionAnswerForm questionAnswerForm) {
        log.info("questionAnswerForm:" + questionAnswerForm.toString());
        QueryWrapper<QuestionAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("form_enroll_id", questionAnswerForm.getFormEnrollId());
        queryWrapper.eq("user_id", questionAnswerForm.getUserId());
        queryWrapper.eq("status", 1);
        if (getBaseMapper().selectCount(queryWrapper) > 0) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "您已填写过哦");
        }

        List<QuestionAnswerSelectForm> answerSelectList = questionAnswerForm.getAnswerSelectList();
        if (CollectionUtils.isEmpty(answerSelectList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "问答内容不能为空哦");
        }

        // 报名活动总体报名人数检查
        FormEnroll formEnroll = formEnrollService.getById(questionAnswerForm.getFormEnrollId());
        if (formEnroll.getLimitNumber() != null && formEnroll.getLimitNumber()>0) {
            QueryWrapper<QuestionAnswer> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("form_enroll_id", questionAnswerForm.getFormEnrollId());
            userQueryWrapper.groupBy("user_id");
            List<QuestionAnswer> formUserList = list(userQueryWrapper);
            if (formUserList != null && formUserList.size() >= formEnroll.getLimitNumber()) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), formEnroll.getName()+ "报名已经满了");
            }
        }

        // 检查报名选择的人数限制
        enrollQuestionService.checkSelectLimitNumber(questionAnswerForm.getAnswerSelectList());

        List<QuestionAnswer> questionAnswerList = answerSelectList.stream().map(item -> {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setUserId(questionAnswerForm.getUserId());
            questionAnswer.setFormEnrollId(questionAnswerForm.getFormEnrollId());

            questionAnswer.setEnrollQuestionId(item.getEnrollQuestionId());
            questionAnswer.setAnswer(item.getAnswer());
            questionAnswer.setSelectIds(item.getSelectIds());
            return questionAnswer;
        }).collect(Collectors.toList());

        saveBatch(questionAnswerList);

        // 检查分享
        if (StringUtils.isNotEmpty(questionAnswerForm.getShareUserId()) && !questionAnswerForm.getShareUserId().equals(questionAnswerForm.getUserId())) {
            Poster poster = posterService.getById(questionAnswerForm.getPosterId());
            ShareConfig shareConfig = shareConfigService.getByCategoryIdAndProductId(poster.getRewardCompanyId(),
                    poster.getRewardCategoryId(), poster.getRewardProductId(), poster.getStoreId(), 1);
            if (shareConfig != null) {
                log.info("用户:{}, 获取到的分享设置:{}", questionAnswerForm.getUserId(), JSON.toJSONString(shareConfig));
                ShareConfigDTO shareConfigDTO = new ShareConfigDTO();
                BeanUtils.copyProperties(shareConfig, shareConfigDTO);
                shareConfigDTO.setShareUserId(questionAnswerForm.getShareUserId());
                shareConfigDTO.setUserId(questionAnswerForm.getUserId());
                log.info("用户:{}, 分享设置:{}", questionAnswerForm.getUserId(), JSON.toJSONString(shareConfigDTO));
                redisUtil.set(String.format("%s%s", RedisConstant.FORM_ENROLL_SHARE_CONFIG, questionAnswerForm.getUserId()), shareConfigDTO);
            }
        }

        try {
            // 领取优惠券
            exerciseCouponService.sendUserCouponByExerciseId(questionAnswerForm.getFormEnrollId(),
                    questionAnswerForm.getUserId(), 1010L, null);
        }catch (Exception e) {
            e.printStackTrace();
        }


        String answers = questionAnswerList.stream().map(QuestionAnswer::getAnswer).collect(Collectors.joining("; "));

        User user = userService.getById(questionAnswerForm.getUserId());
        String username = "用户";
        if (user != null) {
            username = user.getUsername();
        }

        String openId = formEnroll.getServiceOpenId();
        if (StringUtils.isNotEmpty(openId)) {
            QueryWrapper<UserViewPosition> userViewPositionQueryWrapper = new QueryWrapper<>();
            userViewPositionQueryWrapper.eq("user_id", user.getId());
            userViewPositionQueryWrapper.eq("category_id", 1010L);
            userViewPositionQueryWrapper.eq("product_id", questionAnswerForm.getFormEnrollId());
            userViewPositionQueryWrapper.orderByDesc("create_date");
            userViewPositionQueryWrapper.last("limit 1");
            UserViewPosition userViewPosition = userViewPositionService.getOne(userViewPositionQueryWrapper);

//            SubmitNoticeForm submitNoticeForm = new SubmitNoticeForm();
//            submitNoticeForm.setAppId("wxa8841f7b30a525fa");
//            submitNoticeForm.setToUser(openId);
//            submitNoticeForm.setServiceName(username + "提交了问卷");
//            submitNoticeForm.setServiceType(formEnroll.getName() + "问卷提交");
//            submitNoticeForm.setTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            StringBuilder sb = new StringBuilder("提交内容:" + answers);
//            if (userViewPosition != null) {
//                sb.append(" 位置:").append(userViewPosition.getProvince())
//                        .append(userViewPosition.getCity())
//                        .append(userViewPosition.getDistrict())
//                        .append(userViewPosition.getStreet());
//            }
//            submitNoticeForm.setRemark(sb.toString());
//            wxMaUserClient.submitNotice(submitNoticeForm);
        }

        return formEnroll.getSuccessMsg();
    }

    @Override
    public Long getSelectNumberByQuestionId(Long enrollQuestionId, Long selectId) {
        QueryWrapper<QuestionAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enroll_question_id", enrollQuestionId)
        .eq("status", 1);
        return list(queryWrapper).stream().filter(item -> {
            String[] arrays = item.getSelectIds().split(",");
            return ArrayUtils.contains(arrays, selectId + "");
        }).count();
    }
}
