package com.aioveu.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description MQ的直连模式队列
 * @author: 雒世松
 * @date: 2025/5/31 0031 21:13
 */
@Configuration
public class DirectMqConfig {

    public final static String DIRECT_ORDER_QUEUE = "sport_direct_order_queue";

    /**
     * 直连 exchange
     */
    public final static String DIRECT_EXCHANGE = "sport_direct_exchange";



    /**
     * 订单状态修改路由
     */
    public static final String DIRECT_ROUTE_ORDER_STATUS_KEY = "sport_direct_route_order_status_key";

    /**
     * 场地同步
     */
    public final static String DIRECT_FIELD_SYNC = "sport_direct_field_sync";

    /**
     * 场地同步 Key
     */
    public static final String DIRECT_ROUTE_FIELD_SYNC_KEY = "sport_direct_route_field_sync_key";

    public static final String CREATE_FIELD_PLAN_SYNC_KEY = "sport_create_field_plan_sync_key";

    /**
     * 创建订场计划队列
     */
    public static final String CREATE_FILE_PLAN_QUEUE = "sport_create_file_queue";

    /**
     * 发送异步消息通知队列
     */
    public static final String SEND_NOTICE_QUEUE = "sport_send_notice_queue";

    /**
     * 发送异步消息通知路由
     */
    public static final String SEND_NOTICE_KEY = "sport_send_notice_key";

    /**
     * 操作日志消息队列
     */
    public static final String SEND_OPERATE_LOG_QUEUE = "sport_send_operate_log_queue";

    /**
     * 操作日志消息通知路由
     */
    public static final String SEND_OPERATE_LOG_KEY = "sport_send_operate_log_key";


    /**
     * 用户归属修改消息队列
     */
    public static final String USER_ATTRIBUTION_MODIFY_QUEUE = "sport_user_attribution_modify_queue";

    /**
     * 用户归属修改通知路由
     */
    public static final String USER_ATTRIBUTION_MODIFY_KEY = "sport_user_attribution_modify_key";

    /**
     * 约课消息队列
     */
    public static final String GRADE_ENROLL_QUEUE = "sport_grade_enroll_queue";

    /**
     * 约课路由
     */
    public static final String GRADE_ENROLL_KEY = "sport_grade_enroll_key";

    /**
     * 店铺增值服务变动 消息队列
     */
    public static final String STORE_CHARGING_CHANGE_QUEUE = "sport_store_charging_change_queue";

    /**
     * 店铺增值服务变动 通知路由
     */
    public static final String STORE_CHARGING_CHANGE_KEY = "sport_store_charging_change_key";


    /**
     * direct模式队列
     * @return
     */
    @Bean
    public Queue directOrderQueue() {
        // durable:是否持久化,默认是false, 持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
        return new Queue(DIRECT_ORDER_QUEUE, true);
    }

    @Bean
    public Queue directFieldSyncQueue() {
        // durable:是否持久化,默认是false, 持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
        return new Queue(DIRECT_FIELD_SYNC, true);
    }

    @Bean
    public Queue directCreateFieldPlanSyncQueue() {
        // durable:是否持久化,默认是false, 持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
        return new Queue(CREATE_FILE_PLAN_QUEUE, true);
    }

    @Bean
    public Queue directSendNoticeQueue() {
        return new Queue(SEND_NOTICE_QUEUE, true);
    }

    @Bean
    public Queue directSendOperateLogQueue() {
        return new Queue(SEND_OPERATE_LOG_QUEUE, true);
    }

    @Bean
    public Queue directUserAttributionModifyQueue() {
        return new Queue(USER_ATTRIBUTION_MODIFY_QUEUE, true);
    }

    @Bean
    public Queue directGradeEnrollQueue() {
        return new Queue(GRADE_ENROLL_QUEUE, true);
    }

    @Bean
    public Queue storeChargingChangeQueue() {
        return new Queue(STORE_CHARGING_CHANGE_QUEUE, true);
    }

    /**
     * 直连型交换机，根据消息携带的路由键将消息投递给对应队列
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Binding directExchangeBinging() {
        return BindingBuilder.bind(directOrderQueue()).to(directExchange()).with(DIRECT_ROUTE_ORDER_STATUS_KEY);
    }

    @Bean
    public Binding directFieldSyncExchangeBinging() {
        return BindingBuilder.bind(directFieldSyncQueue()).to(directExchange()).with(DIRECT_ROUTE_FIELD_SYNC_KEY);
    }

    @Bean
    public Binding directCreateFieldPlanSyncExchangeBinging() {
        return BindingBuilder.bind(directCreateFieldPlanSyncQueue()).to(directExchange()).with(CREATE_FIELD_PLAN_SYNC_KEY);
    }

    @Bean
    public Binding directSendNoticeExchangeBinging() {
        return BindingBuilder.bind(directSendNoticeQueue()).to(directExchange()).with(SEND_NOTICE_KEY);
    }

    @Bean
    public Binding directSendOperateLogExchangeBinging() {
        return BindingBuilder.bind(directSendOperateLogQueue()).to(directExchange()).with(SEND_OPERATE_LOG_KEY);
    }

    @Bean
    public Binding directUserAttributionModifyExchangeBinging() {
        return BindingBuilder.bind(directUserAttributionModifyQueue()).to(directExchange()).with(USER_ATTRIBUTION_MODIFY_KEY);
    }

    @Bean
    public Binding directGradeEnrollExchangeBinging() {
        return BindingBuilder.bind(directGradeEnrollQueue()).to(directExchange()).with(GRADE_ENROLL_KEY);
    }

    @Bean
    public Binding directStoreChargingChangeExchangeBinging() {
        return BindingBuilder.bind(storeChargingChangeQueue()).to(directExchange()).with(STORE_CHARGING_CHANGE_KEY);
    }


}
