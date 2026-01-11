package com.aioveu.pms.aioveu01Brand.service;

import com.aioveu.pms.aioveu01Brand.model.form.PmsBrandForm;
import com.aioveu.pms.aioveu01Brand.model.query.PmsBrandQuery;
import com.aioveu.pms.aioveu01Brand.model.vo.PmsBrandVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;


/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品品牌服务类
 * @Date  2026/1/10 19:05
 * @Param 
 * @return 
 **/
public interface PmsBrandService extends IService<PmsBrand> {

    /**
     *商品品牌分页列表
     *
     * @return {@link IPage<PmsBrandVO>} 商品品牌分页列表
     */
    IPage<PmsBrandVO> getPmsBrandPage(PmsBrandQuery queryParams);

    /**
     * 获取商品品牌表单数据
     *
     * @param id 商品品牌ID
     * @return 商品品牌表单数据
     */
    PmsBrandForm getPmsBrandFormData(Long id);

    /**
     * 新增商品品牌
     *
     * @param formData 商品品牌表单对象
     * @return 是否新增成功
     */
    boolean savePmsBrand(PmsBrandForm formData);

    /**
     * 修改商品品牌
     *
     * @param id   商品品牌ID
     * @param formData 商品品牌表单对象
     * @return 是否修改成功
     */
    boolean updatePmsBrand(Long id, PmsBrandForm formData);

    /**
     * 删除商品品牌
     *
     * @param ids 商品品牌ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsBrands(String ids);

}
