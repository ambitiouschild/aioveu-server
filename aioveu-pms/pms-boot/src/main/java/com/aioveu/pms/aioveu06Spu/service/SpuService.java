package com.aioveu.pms.aioveu06Spu.service;

import com.aioveu.pms.aioveu06Spu.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.query.SpuPageQuery;

import java.util.List;

/**
 * @Description: TODO 商品业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:34
 * @param
 * @return:
 **/

public interface SpuService extends IService<PmsSpu> {


    /**
     * Admin-商品分页列表
     *
     * @param queryParams
     * @return
     */
    IPage<PmsSpuPageVO> listPagedSpu(SpuPageQuery queryParams);

    /**
     * 「应用端」商品分页列表
     *
     * @param queryParams
     * @return
     */
    IPage<SpuPageVO> listPagedSpuForApp(SpuPageQuery queryParams);


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
}
