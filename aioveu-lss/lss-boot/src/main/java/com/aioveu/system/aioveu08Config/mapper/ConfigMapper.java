package com.aioveu.system.aioveu08Config.mapper;

import com.aioveu.system.aioveu08Config.model.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ConfigMapper
 * @Description TODO  系统配置 访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:33
 * @Version 1.0
 **/

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
}
