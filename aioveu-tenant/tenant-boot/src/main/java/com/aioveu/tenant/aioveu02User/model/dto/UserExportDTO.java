package com.aioveu.tenant.aioveu02User.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: UserExportDTO
 * @Description TODO 用户导出视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:07
 * @Version 1.0
 **/
@Data
@ColumnWidth(20)
@Schema(description ="用户导出视图对象")
public class UserExportDTO {

    @ExcelProperty(value = "用户名")
    private String username;

    @ExcelProperty(value = "用户昵称")
    private String nickname;

    @ExcelProperty(value = "部门")
    private String deptName;

    @ExcelProperty(value = "性别")
    private String gender;

    @ExcelProperty(value = "手机号码")
    private String mobile;

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createTime;
}
