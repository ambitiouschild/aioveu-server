package com.aioveu.pms.aioveu06Spu.service;

import com.aioveu.pms.aioveu06Spu.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;

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
}
