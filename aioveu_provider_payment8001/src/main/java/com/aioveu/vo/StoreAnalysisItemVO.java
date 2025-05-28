package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:14
 */
@Data
@AllArgsConstructor
public class StoreAnalysisItemVO {
    private BigDecimal newSignatureAmount;
    private BigDecimal renewalAmount;
    private BigDecimal passiveRenewalAmount;
    private List<String> newSignatureOrderIdList;
    private List<String> renewalOrderIdList;
    private List<String> passiveRenewalOrderIdList;
}
