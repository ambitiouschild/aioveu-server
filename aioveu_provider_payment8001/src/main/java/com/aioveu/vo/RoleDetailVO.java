package com.aioveu.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RoleDetailVO {

    private Long id;

    @NotEmpty(message = "角色名称不能为空")
    private String name;

    private String code;

    private Integer type;

    private List<String> menuCodes;

    private Long storeId;

    private Long companyId;

}
