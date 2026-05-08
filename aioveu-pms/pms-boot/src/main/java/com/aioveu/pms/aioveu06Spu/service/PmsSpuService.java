package com.aioveu.pms.aioveu06Spu.service;

import com.aioveu.pms.aioveu06Spu.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: TODO 商品业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:34
 * @param
 * @return:
 **/

public interface PmsSpuService extends IService<PmsSpu> {


    /**
     * Admin-商品分页列表
     *
     * @param queryParams
     * @return
     */
    IPage<PmsSpuVO> listPagedSpu(PmsSpuQuery queryParams);

    /**
     * 「应用端」商品分页列表
     *
     * @param queryParams
     * @return
     */
    IPage<SpuPageVO> listPagedSpuForApp(PmsSpuQuery queryParams);


    /**
     * Admin-获取商品详情
     *
     * @param id
     * @return
     */
    PmsSpuDetailVO getSpuDetail(Long id);

    /**
     * 「应用端」获取商品详情
     *
     * @param spuId
     * @return
     */
    SpuDetailVO getSpuDetailForApp(Long spuId);


    /**
     * 新增商品
     *
     * @param formData
     * @return
     */
    boolean addSpu(PmsSpuForm formData);

    /**
     * 修改商品
     *
     * @param spuId    商品ID
     * @param formData
     * @return
     */
    boolean updateSpuById(Long spuId, PmsSpuForm formData);

    /**
     * 删除商品
     *
     * @param ids 商品ID，多个以英文逗号(,)分割
     * @return
     */
    boolean removeBySpuIds(String ids);

    /**
     * 获取秒杀商品列表
     * TODO
     *
     * @return
     */
    List<SeckillingSpuVO> listSeckillingSpu();

    /**
     *商品分页列表
     *
     * @return {@link IPage<PmsSpuVO>} 商品分页列表
     */
    IPage<PmsSpuVO> getPmsSpuPage(PmsSpuQuery queryParams);


    /**
     * 根据分类ID分页查询商品列表
     *
     * @return {@link IPage<PmsSpuVO>} 根据分类ID分页查询商品列表
     */
    IPage<PmsSpuVO> getSpuListByCategory(PmsSpuQuery queryParams);


    /**
     * 获取商品表单数据
     *
     * @param id 商品ID
     * @return 商品表单数据
     */
    PmsSpuForm getPmsSpuFormData(Long id);

    /**
     * 新增商品
     *
     * @param formData 商品表单对象
     * @return 是否新增成功
     */
    boolean savePmsSpu(PmsSpuForm formData);

    /**
     * 修改商品
     *
     * @param id   商品ID
     * @param formData 商品表单对象
     * @return 是否修改成功
     */
    boolean updatePmsSpu(Long id, PmsSpuForm formData);

    /**
     * 删除商品
     *
     * @param ids 商品ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePmsSpus(String ids);


    /**
     * 批量更新商品状态（上架/下架）
     * @param spuIds 商品ID列表
     * @param status 目标状态：0=下架，1=上架
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Long> spuIds, Integer status);


    /**
     * 批量上架商品
     */
    boolean batchShelf(List<Long> spuIds);


    /**
     * 批量下架商品
     */
    boolean batchOffShelf(List<Long> spuIds);



    /**
     * 批量删除商品（逻辑删除）
     * @param spuIds 商品ID列表
     * @return 是否删除成功
     */
    boolean batchRemove(List<Long> spuIds);

    /**
     * 批量物理删除（慎用）
     */
    boolean batchPhysicalRemove(List<Long> spuIds);

    /**
     * 安全批量更新状态（检查商品是否存在，避免空更新）
     */
    boolean safeBatchUpdateStatus(List<Long> spuIds, Integer status);



    /**
     * 根据条件批量更新状态
     */
    boolean batchUpdateStatusByCondition(Long categoryId, Long brandId, Integer status);
}
