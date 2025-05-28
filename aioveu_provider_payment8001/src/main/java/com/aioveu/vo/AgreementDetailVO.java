package com.aioveu.vo;

import com.aioveu.entity.Agreement;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/6 0006 15:23
 */
@Data
public class AgreementDetailVO extends Agreement {

    private String companyName;

    private String storeName;

}
