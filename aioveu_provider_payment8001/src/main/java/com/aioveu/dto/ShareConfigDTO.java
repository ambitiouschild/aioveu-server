package com.aioveu.dto;

import com.aioveu.entity.ShareConfig;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Data
public class ShareConfigDTO extends ShareConfig {

    private String orderId;

    private String shareUserId;

    private String userId;

}
