package com.aioveu.system.aioveu06Dict.mapper;

import com.aioveu.system.aioveu06Dict.model.entity.DictItem;
import com.aioveu.system.aioveu06Dict.model.query.DictItemPageQuery;
import com.aioveu.system.aioveu06Dict.model.vo.DictItemPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DictItemMapper
 * @Description TODO  字典项映射层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:05
 * @Version 1.0
 **/

@Mapper
public interface DictItemMapper extends BaseMapper<DictItem> {

    /**
     * 字典项分页列表
     */
    Page<DictItemPageVO> getDictItemPage(Page<DictItemPageVO> page, DictItemPageQuery queryParams);
}
