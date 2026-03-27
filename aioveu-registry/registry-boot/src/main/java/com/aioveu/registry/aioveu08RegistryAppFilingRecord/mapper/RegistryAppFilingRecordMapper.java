package com.aioveu.registry.aioveu08RegistryAppFilingRecord.mapper;

import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.entity.RegistryAppFilingRecord;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.query.RegistryAppFilingRecordQuery;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.vo.RegistryAppFilingRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryAppFilingRecordMapper
 * @Description TODO 小程序备案记录Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:11
 * @Version 1.0
 **/
@Mapper
public interface RegistryAppFilingRecordMapper extends BaseMapper<RegistryAppFilingRecord> {

    /**
     * 获取小程序备案记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryAppFilingRecordVo>} 小程序备案记录分页列表
     */
    Page<RegistryAppFilingRecordVo> getRegistryAppFilingRecordPage(Page<RegistryAppFilingRecordVo> page, RegistryAppFilingRecordQuery queryParams);

}
