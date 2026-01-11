package com.aioveu.pms.aioveu06Spu.converter;

import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.vo.SeckillingSpuVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Description: TODO 商品对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:30
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface PmsSpuConverter {

    @Mappings({
            @Mapping(target = "album", source = "subPicUrls")
    })
    PmsSpu form2Entity(PmsSpuForm form);

    @InheritInverseConfiguration(name="form2Entity")
    PmsSpuForm entity2Form(PmsSpu entity);

    SeckillingSpuVO entity2SeckillingVO(PmsSpu entity);

    List<SeckillingSpuVO> entity2SeckillingVO(List<PmsSpu> entities);

    PmsSpuForm toForm(PmsSpu entity);

    PmsSpu toEntity(PmsSpuForm formData);

}
