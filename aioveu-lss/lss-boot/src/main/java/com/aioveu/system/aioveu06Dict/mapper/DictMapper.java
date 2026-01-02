package com.aioveu.system.aioveu06Dict.mapper;

import com.aioveu.system.aioveu06Dict.model.entity.Dict;
import com.aioveu.system.aioveu06Dict.model.query.DictPageQuery;
import com.aioveu.system.aioveu06Dict.model.vo.DictPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DictMapper
 * @Description TODO  字典 访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:05
 * @Version 1.0
 **/

@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 字典分页列表
     *
     * @param page 分页参数
     * @param queryParams 查询参数
     * @return 字典分页列表
     */
    Page<DictPageVO> getDictPage(Page<DictPageVO> page, DictPageQuery queryParams);
}
