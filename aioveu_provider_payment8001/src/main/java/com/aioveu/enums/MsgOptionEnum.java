package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>消息类型</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum MsgOptionEnum {

    FIELD_SUCCESS("订场成功消息", "field_success_msg"),
    FIELD_CANCEL("订场取消消息", "field_cancel_msg"),
    USER_COUPON_LESS("用户课券不足消息", "user_coupon_less_msg"),
    USER_GRADE_ENROLL_SUCCESS("用户约课成功消息", "grade_enroll_success_msg"),
    ORDER_PAY_SUCCESS("用户下单成功消息", "user_order_pay_success_msg"),
    GRADE_EVALUATE_MSG("课程教练评价通知", "grade_evaluate_msg"),
    USER_GRADE_ENROLL_CANCEL("用户约课取消消息", "grade_enroll_cancel_msg"),
    AUDIT_CANCEL_GRADE_MSG("取消课程审批消息", "cancel_grade_audit_msg"),
    RECEIVE_COUPON_MSG("领取优惠券通知", "receive_coupon_msg"),
    WEEK_GRADE_COUPON_MSG("每周用户课券使用提醒", "week_grade_coupon_msg"),
    FIELD_SYNC_PLATFORM_FAIL("场地同步提醒", "field_sync_platform_fail"),
    FIELD_SYNC_CONFLICT_MSG("场地同步冲突提醒", "field_sync_conflict_msg"),
    WEEK_GRADE_MSG("每周已约课上课提醒", "week_grade_msg"),
    CHARGING_NO_VALUE("增值服务次数不足提醒", "charging_no_value_msg"),
    UN_SIGN_MSG("教练每天未签到提醒", "un_sign_msg");

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private String code;

    public static MsgOptionEnum of(String msgCode) {
        Objects.requireNonNull(msgCode);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(msgCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(msgCode + " not exists!"));
    }
}
