package com.aioveu.refund.aioveu05RefundProof.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: ProofTypeEnum
 * @Description TODO 凭证类型枚举：1-质量问题 2-物流问题 3-描述不符 4-其他
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 16:50
 * @Version 1.0
 **/
public enum ProofTypeEnum implements IBaseEnum<Integer> {

    /**
     * 质量问题
     */
    quality_issue(1, "质量问题"),
    /**
     * 物流问题
     */
    logistics_issue(2, "物流问题"),
    /**
     * 描述不符
     */
    Description_does_not_match(2, "描述不符"),
    /**
     * 其他
     */
    other(3, "其他");


    ProofTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;

}
