package com.aioveu.tenant.aioveu08Config.converter;

import com.aioveu.tenant.aioveu08Config.model.entity.Config;
import com.aioveu.tenant.aioveu08Config.model.form.ConfigForm;
import com.aioveu.tenant.aioveu08Config.model.vo.ConfigVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: ConfigConverter
 * @Description TODO 系统配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:07
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface ConfigConverter {

    Page<ConfigVO> toPageVo(Page<Config> page);

    Config toEntity(ConfigForm configForm);

    ConfigForm toForm(Config entity);
}
