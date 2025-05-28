package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/6 0006 15:23
 */
@Data
public class UserExtensionAccountVO {

    private Long id;

    private String username;

    private BigDecimal balance;

    private List<StoreTelVO> storeList;

}
