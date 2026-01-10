package com.aioveu.pms.aioveu06Spu.model.form;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO 商品SPU表单对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:31
 * @param
 * @return:
 **/

@Data
public class PmsSpuForm {

    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private Long originPrice;
    private Long price;
    private String picUrl;
    private String[] subPicUrls;
    private String description;
    private String detail;

    private List<PmsSpuAttributeForm> attrList;

    private List<PmsSpuAttributeForm> specList;

    private List<PmsSku> skuList;
}
