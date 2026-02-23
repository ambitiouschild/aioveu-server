package com.aioveu.tenant.aioveu12WebSocket.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: TextMessage
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:54
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMessage {

    private String sender;
    private String content;
    private Long timestamp;
}
