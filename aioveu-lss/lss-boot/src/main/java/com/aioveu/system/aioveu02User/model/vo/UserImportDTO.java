package com.aioveu.system.aioveu02User.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName: UserImportDTO
 * @Description TODO 用户导入对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 19:14
 * @Version 1.0
 **/

@Data
public class UserImportDTO {

    @ExcelProperty(value = "用户名")
    private String username;

    @ExcelProperty(value = "昵称")
    private String nickname;

    @ExcelProperty(value = "性别")
    private String genderLabel;

    @ExcelProperty(value = "手机号码")
    private String mobile;

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty("角色")
    private String roleCodes;

    @ExcelProperty("部门")
    private String deptCode;
}
