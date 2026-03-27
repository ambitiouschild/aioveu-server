package com.aioveu.registry.aioveu07RegistryInvoiceInfo.mapper;

import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.entity.RegistryInvoiceInfo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.query.RegistryInvoiceInfoQuery;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.vo.RegistryInvoiceInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryInvoiceInfoMapper
 * @Description TODO 发票信息Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:41
 * @Version 1.0
 **/
@Mapper
public interface RegistryInvoiceInfoMapper extends BaseMapper<RegistryInvoiceInfo> {

    /**
     * 获取发票信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryInvoiceInfoVo>} 发票信息分页列表
     */
    Page<RegistryInvoiceInfoVo> getRegistryInvoiceInfoPage(Page<RegistryInvoiceInfoVo> page, RegistryInvoiceInfoQuery queryParams);

}
