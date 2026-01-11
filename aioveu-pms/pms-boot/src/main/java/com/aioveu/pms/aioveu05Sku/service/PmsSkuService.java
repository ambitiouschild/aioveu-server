package com.aioveu.pms.aioveu05Sku.service;

import com.aioveu.pms.aioveu05Sku.model.form.PmsSkuForm;
import com.aioveu.pms.aioveu05Sku.model.query.PmsSkuQuery;
import com.aioveu.pms.aioveu05Sku.model.vo.PmsSkuVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.model.dto.LockSkuDTO;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;

import java.util.List;


/**
 * @Description: TODO 商品库存服务类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:34
 * @param
 * @return:
 **/

public interface PmsSkuService extends IService<PmsSku> {

    /**
     * 获取商品库存信息
     *
     * @param skuId SKU ID
     * @return 商品库存信息
     */
    SkuInfoDTO getSkuInfo(Long skuId);

    /**
     * 获取商品库存信息列表
     *
     * @param skuIds SKU ID 列表
     * @return 商品库存信息列表
     */
    List<SkuInfoDTO> listSkuInfos(List<Long> skuIds);

    /**
     * 校验并锁定库存
     *
     * @param orderToken 订单临时编号 (此时订单未创建)
     * @param lockSkuList 锁定商品库存信息列表
     * @return true/false
     */
    boolean lockStock(String orderToken,List<LockSkuDTO> lockSkuList);

    /**
     * 解锁库存
     */
    boolean unlockStock(String orderSn);

    /**
     * 扣减库存
     */
    boolean deductStock(String orderSn);

    /**
     *商品库存分页列表
     *
     * @return {@link IPage<PmsSkuVO>} 商品库存分页列表
     */
    IPage<PmsSkuVO> getPmsSkuPage(PmsSkuQuery queryParams);

    /**
     * 获取商品库存表单数据
     *
     * @param id 商品库存ID
     * @return 商品库存表单数据
     */
    PmsSkuForm getPmsSkuFormData(Long id);

    /**
     * 新增商品库存
     *
     * @param formData 商品库存表单对象
     * @return 是否新增成功
     */
    boolean savePmsSku(PmsSkuForm formData);

    /**
     * 修改商品库存
     *
     * @param id   商品库存ID
     * @param formData 商品库存表单对象
     * @return 是否修改成功
     */
    boolean updatePmsSku(Long id, PmsSkuForm formData);

    /**
     * 删除商品库存
     *
     * @param ids 商品库存ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsSkus(String ids);


}
