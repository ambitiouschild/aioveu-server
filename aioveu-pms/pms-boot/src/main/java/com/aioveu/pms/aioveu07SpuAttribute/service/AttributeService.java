package com.aioveu.pms.aioveu07SpuAttribute.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;

public interface AttributeService extends IService<PmsCategoryAttribute> {

    /**
     * 批量保存商品属性
     *
     * @param formData 属性表单数据
     * @return
     */
    boolean saveBatch(PmsCategoryAttributeForm formData);
}
