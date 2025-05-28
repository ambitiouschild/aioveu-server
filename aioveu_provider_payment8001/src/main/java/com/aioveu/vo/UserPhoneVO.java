package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneVO {

    private String userId;

    private String userName;

    private String phone;

    private String codeName;

    private Long storeId;

    private Long id;
}
