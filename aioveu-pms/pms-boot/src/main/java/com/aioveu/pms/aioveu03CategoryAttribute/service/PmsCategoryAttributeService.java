package com.aioveu.pms.aioveu03CategoryAttribute.service;

import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;
import com.aioveu.pms.aioveu03CategoryAttribute.model.query.PmsCategoryAttributeQuery;
import com.aioveu.pms.aioveu03CategoryAttribute.model.vo.PmsCategoryAttributeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
  *@ClassName: PmsCategoryAttributeService
  *@Description TODO 商品分类类型（规格，属性）服务类
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2026/1/11 19:49
  *@Version 1.0
  **/
public interface PmsCategoryAttributeService extends IService<PmsCategoryAttribute> {

    /**
     *商品类型（规格，属性）分页列表
     *
     * @return {@link IPage<PmsCategoryAttributeVO>} 商品类型（规格，属性）分页列表
     */
    IPage<PmsCategoryAttributeVO> getPmsCategoryAttributePage(PmsCategoryAttributeQuery queryParams);

    /**
     * 获取商品类型（规格，属性）表单数据
     *
     * @param id 商品类型（规格，属性）ID
     * @return 商品类型（规格，属性）表单数据
     */
    PmsCategoryAttributeForm getPmsCategoryAttributeFormData(Long id);

    /**
     * 新增商品类型（规格，属性）
     *
     * @param formData 商品类型（规格，属性）表单对象
     * @return 是否新增成功
     */
    boolean savePmsCategoryAttribute(PmsCategoryAttributeForm formData);

    /**
     * 修改商品类型（规格，属性）
     *
     * @param id   商品类型（规格，属性）ID
     * @param formData 商品类型（规格，属性）表单对象
     * @return 是否修改成功
     */
    boolean updatePmsCategoryAttribute(Long id, PmsCategoryAttributeForm formData);

    /**
     * 删除商品类型（规格，属性）
     *
     * @param ids 商品类型（规格，属性）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsCategoryAttributes(String ids);
}
