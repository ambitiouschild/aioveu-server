package com.aioveu.oms.aioveu06OrderSetting.mapper;

import com.aioveu.oms.aioveu06OrderSetting.model.query.OmsOrderSettingQuery;
import com.aioveu.oms.aioveu06OrderSetting.model.vo.OmsOrderSettingVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu06OrderSetting.model.entity.OmsOrderSetting;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单配置信息Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Mapper
public interface OmsOrderSettingMapper extends BaseMapper<OmsOrderSetting> {

    /**
     * 获取订单配置信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderSettingVO>} 订单配置信息分页列表
     */
    Page<OmsOrderSettingVO> getOmsOrderSettingPage(Page<OmsOrderSettingVO> page, OmsOrderSettingQuery queryParams);

}
