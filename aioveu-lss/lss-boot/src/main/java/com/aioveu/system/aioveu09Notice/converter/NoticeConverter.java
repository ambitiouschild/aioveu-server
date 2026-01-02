package com.aioveu.system.aioveu09Notice.converter;

import com.aioveu.system.aioveu09Notice.model.entity.Notice;
import com.aioveu.system.aioveu09Notice.model.form.NoticeForm;
import com.aioveu.system.aioveu09Notice.model.vo.NoticeBO;
import com.aioveu.system.aioveu09Notice.model.vo.NoticeDetailVO;
import com.aioveu.system.aioveu09Notice.model.vo.NoticePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @ClassName: NoticeConverter
 * @Description TODO  通知公告对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:28
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface NoticeConverter {

    @Mappings({
            @Mapping(target = "targetUserIds", expression = "java(cn.hutool.core.util.StrUtil.split(entity.getTargetUserIds(),\",\"))")
    })
    NoticeForm toForm(Notice entity);

    @Mappings({
            @Mapping(target = "targetUserIds", expression = "java(cn.hutool.core.collection.CollUtil.join(formData.getTargetUserIds(),\",\"))")
    })
    Notice toEntity(NoticeForm formData);

    NoticePageVO toPageVo(NoticeBO bo);

    Page<NoticePageVO> toPageVo(Page<NoticeBO> noticePage);

    NoticeDetailVO toDetailVO(NoticeBO noticeBO);
}
