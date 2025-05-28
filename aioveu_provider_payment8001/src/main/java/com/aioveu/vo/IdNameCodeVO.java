package com.aioveu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/21 0021 23:24
 */

@Data
@NoArgsConstructor
public class IdNameCodeVO extends IdNameVO {

    public IdNameCodeVO(Long id, String name, String code) {
        super(id, name);
        this.code = code;
    }

    private String code;

}
