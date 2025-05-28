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
public class StoreSimpleVO extends NameVO {

    public StoreSimpleVO(Long id, String name) {
        super(name);
        this.id = id;
    }

    private Long id;

    private String logo;

    private Long companyId;

    private Double longitude;

    private Double latitude;

}
