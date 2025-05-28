package com.aioveu.data.sync;

import com.aioveu.dto.FieldPlanDTO;
import lombok.Data;

import java.util.List;

/**
 * @description 同步到第三方平台订场异常
 * @author: 雒世松
 * @date: 2025/4/26 18:14
 */
@Data
public class FieldSyncHandleError {

    /**
     * 消息
     */
    private String msg;

    /**
     * 订场信息
     */
    private List<FieldPlanDTO> fieldPlanDTOList;

}
