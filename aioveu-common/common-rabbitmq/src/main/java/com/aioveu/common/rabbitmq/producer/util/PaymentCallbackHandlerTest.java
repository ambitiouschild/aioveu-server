package com.aioveu.common.rabbitmq.producer.util;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: PaymentCallbackHandlerTest
 * @Description TODO 单元测试
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:28
 * @Version 1.0
 **/
//@SpringBootTest
//@ActiveProfiles("test")
public class PaymentCallbackHandlerTest {

//    @Autowired
//    private PaymentCallbackHandler handler;
//
//    @MockBean
//    private RocketMQTemplate rocketMQTemplate;
//
//    @Test
//    void testHandleWechatCallback_Success() {
//        // 准备测试数据
//        String xmlData = "<xml>...</xml>";
//
//        // 模拟MQ发送
//        when(rocketMQTemplate.syncSendOrderly(any(), any(), any(), anyLong()))
//                .thenReturn(new SendResult(SendStatus.SEND_OK, "msgId"));
//
//        // 执行测试
//        String result = handler.handleWechatCallback(xmlData);
//
//        // 验证结果
//        assertTrue(result.contains("SUCCESS"));
//        verify(rocketMQTemplate, times(1)).syncSendOrderly(any(), any(), any(), anyLong());
//    }
}
