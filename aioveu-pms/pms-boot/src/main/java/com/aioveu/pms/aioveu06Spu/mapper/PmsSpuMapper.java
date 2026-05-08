package com.aioveu.pms.aioveu06Spu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;
import com.aioveu.pms.aioveu06Spu.model.vo.PmsSpuVO;
import com.aioveu.pms.aioveu06Spu.model.vo.SpuPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PmsSpuMapper extends BaseMapper<PmsSpu> {

    /**
     * Admin-商品分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return 商品分页列表
     */
    List<PmsSpuVO> listPagedSpu(Page<PmsSpuVO> page, PmsSpuQuery queryParams);

    /**
     * APP-商品分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return 商品分页列表
     */
    List<SpuPageVO> listPagedSpuForApp(Page<SpuPageVO> page, PmsSpuQuery queryParams);


    /**
     * Admin-获取商品分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsSpuVO>} 商品分页列表
     */
    Page<PmsSpuVO> getPmsSpuPage(Page<PmsSpuVO> page, PmsSpuQuery queryParams);


    /**
     * 批量更新商品状态
     */
    int batchUpdateStatus(@Param("spuIds") List<Long> spuIds,
                          @Param("status") Integer status);

    /**
     * 批量上架
     */
    int batchShelf(@Param("spuIds") List<Long> spuIds);


    /**
     * 批量下架
     */
    int batchOffShelf(@Param("spuIds") List<Long> spuIds);


    /**
     * 批量逻辑删除商品
     */
    int batchRemove(@Param("spuIds") List<Long> spuIds);



}
