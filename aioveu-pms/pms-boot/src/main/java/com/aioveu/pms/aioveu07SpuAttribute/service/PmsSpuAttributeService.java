package com.aioveu.pms.aioveu07SpuAttribute.service;

import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import com.aioveu.pms.aioveu07SpuAttribute.model.query.PmsSpuAttributeQuery;
import com.aioveu.pms.aioveu07SpuAttribute.model.vo.PmsSpuAttributeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品类型（属性/规格）服务类
 * @Date  2026/1/11 22:10
 * @Param
 * @return
 **/

public interface PmsSpuAttributeService extends IService<PmsSpuAttribute> {

    /**
     *商品类型（属性/规格）分页列表
     *
     * @return {@link IPage<PmsSpuAttributeVO>} 商品类型（属性/规格）分页列表
     */
    IPage<PmsSpuAttributeVO> getPmsSpuAttributePage(PmsSpuAttributeQuery queryParams);

    /**
     * 获取商品类型（属性/规格）表单数据
     *
     * @param id 商品类型（属性/规格）ID
     * @return 商品类型（属性/规格）表单数据
     */
    PmsSpuAttributeForm getPmsSpuAttributeFormData(Long id);

    /**
     * 新增商品类型（属性/规格）
     *
     * @param formData 商品类型（属性/规格）表单对象
     * @return 是否新增成功
     */
    boolean savePmsSpuAttribute(PmsSpuAttributeForm formData);

    /**
     * 修改商品类型（属性/规格）
     *
     * @param id   商品类型（属性/规格）ID
     * @param formData 商品类型（属性/规格）表单对象
     * @return 是否修改成功
     */
    boolean updatePmsSpuAttribute(Long id, PmsSpuAttributeForm formData);

    /**
     * 删除商品类型（属性/规格）
     *
     * @param ids 商品类型（属性/规格）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsSpuAttributes(String ids);


}
