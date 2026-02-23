package com.aioveu.tenant.aioveu08Config.mapper;

import com.aioveu.tenant.aioveu08Config.model.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ConfigMapper
 * @Description TODO 系统配置 访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:13
 * @Version 1.0
 **/

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
}
