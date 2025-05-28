package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_check_record")
@Data
public class CheckRecord extends IdEntity {

    private Long companyId;

    private Long storeId;

    @NotEmpty(message = "核销码不能为空")
    private String image;
    /**
     * 核销码文本内容
     */
    private String codeText;

    private String createUser;

    private String createUserId;

    private String checkUser;

    private String checkUserId;

    // status 1 未核销 2 已核销

}
