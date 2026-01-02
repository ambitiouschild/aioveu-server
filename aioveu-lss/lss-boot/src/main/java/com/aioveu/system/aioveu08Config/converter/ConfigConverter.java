package com.aioveu.system.aioveu08Config.converter;

import com.aioveu.system.aioveu08Config.model.entity.Config;
import com.aioveu.system.aioveu08Config.model.form.ConfigForm;
import com.aioveu.system.aioveu08Config.model.vo.ConfigVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: ConfigConverter
 * @Description TODO  系统配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:26
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface ConfigConverter {

    Page<ConfigVO> toPageVo(Page<Config> page);

    Config toEntity(ConfigForm configForm);

    ConfigForm toForm(Config entity);
}
