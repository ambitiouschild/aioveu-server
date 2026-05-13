package com.aioveu.pay.aioveu12MqProducerPayment.adapter;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName: MessageSendRequestBuilder
 * @Description TODO 方案三：Builder模式（流畅API）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:00
 * @Version 1.0
 **/

/*
* // 使用示例
MessageSendRequest request = MessageSendRequestBuilder.of("ORDER_TOPIC", order)
    .bizId(orderId)
    .bizType("ORDER")
    .tag("ORDER_CREATE")
    .delayTime(5000L)  // 延迟5秒
    .async(true)
    .build();

* */


/**
 * Builder模式构建发送请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendRequestBuilder {

    private String topic;
    private Object body;

    @Builder.Default
    private String bizId = "";

    @Builder.Default
    private String bizType = "";

    @Builder.Default
    private String tag = "";

    @Builder.Default
    private long timeout = 3000L;

    // 静态工厂方法
    public static MessageSendRequestBuilder of(String topic, Object body) {
        return MessageSendRequestBuilder.builder()
                .topic(topic)
                .body(body)
                .build();
    }
}
