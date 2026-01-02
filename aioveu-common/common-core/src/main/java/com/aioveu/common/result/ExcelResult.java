package com.aioveu.common.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ExcelResult
 * @Description TODO  Excel导出响应结构体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:16
 * @Version 1.0
 **/

@Data
public class ExcelResult {

    /**
     * 响应码，来确定是否导入成功
     */
    private String code;

    /**
     * 有效条数
     */
    private Integer validCount;

    /**
     * 无效条数
     */
    private Integer invalidCount;

    /**
     * 错误提示信息
     */
    private List<String> messageList;

    public ExcelResult() {
        this.code = ResultCode.SUCCESS.getCode();
        this.validCount = 0;
        this.invalidCount = 0;
        this.messageList = new ArrayList<>();
    }
}
