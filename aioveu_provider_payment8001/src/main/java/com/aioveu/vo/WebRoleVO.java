package com.aioveu.vo;

import com.aioveu.entity.Role;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/21 0021 23:24
 */

@Data
public class WebRoleVO extends Role {

    private String storeName;

    private String companyName;

}
