package com.aioveu.refund.aioveu02RefundItem.mapper;

import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu02RefundItem.model.query.RefundItemQuery;
import com.aioveu.refund.aioveu02RefundItem.model.vo.RefundItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundItemMapper
 * @Description TODO  退款商品明细Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:44
 * @Version 1.0
 **/

@Mapper
public interface RefundItemMapper extends BaseMapper<RefundItem> {

    /**
     * 获取退款商品明细分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundItemVO>} 退款商品明细分页列表
     */
    Page<RefundItemVO> getRefundItemPage(Page<RefundItemVO> page, RefundItemQuery queryParams);
}
