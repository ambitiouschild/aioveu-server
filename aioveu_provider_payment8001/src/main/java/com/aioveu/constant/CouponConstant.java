package com.aioveu.constant;

/**
 * <h1>常用常量定义</h1>
 * Created by Qinyi.
 */
public class CouponConstant {

    /** Kafka 消息的 Topic */
    public static final String TOPIC = "sport_user_coupon_op";

    /**
     * <h2>Redis Key 前缀定义</h2>
     * */
    public static class RedisPrefix {

        /** 优惠券码 key 前缀 */
        public static final String COUPON_TEMPLATE =
                "sport_coupon_template_code_";

        /** 用户当前所有可用的优惠券 key 前缀 */
        public static final String USER_COUPON_USABLE =
                "sport_user_coupon_usable_";

        /** 用户当前所有已使用的优惠券 key 前缀 */
        public static final String USER_COUPON_USED =
                "sport_user_coupon_used_";

        /** 用户当前所有已过期的优惠券 key 前缀 */
        public static final String USER_COUPON_EXPIRED =
                "sport_user_coupon_expired_";

        /** 用户当前所有待激活的优惠券 key 前缀 */
        public static final String USER_COUPON_UN_ACTIVE =
                "sport_user_coupon_un_active_";
    }
}
