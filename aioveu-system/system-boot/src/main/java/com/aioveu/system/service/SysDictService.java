package com.aioveu.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.common.web.model.Option;
import com.aioveu.system.model.entity.SysDict;
import com.aioveu.system.model.form.DictForm;
import com.aioveu.system.model.query.DictPageQuery;
import com.aioveu.system.model.vo.DictPageVO;

import java.util.List;

/**
 * @Description: TODO 字典接口
 * @Author: 雒世松
 * @Date: 2025/6/5 17:29
 * @param
 * @return:
 **/

public interface SysDictService extends IService<SysDict> {
    /**
     * 字典数据项分页列表
     *
     * @param queryParams
     * @return
     */
    Page<DictPageVO> getDictPage(DictPageQuery queryParams);

    /**
     * 字典数据项表单
     *
     * @param id 字典数据项ID
     * @return
     */
    DictForm getDictForm(Long id);

    /**
     * 新增字典数据项
     *
     * @param dictForm 字典数据项表单
     * @return
     */
    boolean saveDict(DictForm dictForm);

    /**
     * 修改字典数据项
     *
     * @param id       字典数据项ID
     * @param dictForm 字典数据项表单
     * @return
     */
    boolean updateDict(Long id, DictForm dictForm);

    /**
     * 删除字典数据项
     *
     * @param idsStr 字典数据项ID，多个以英文逗号(,)分割
     * @return
     */
    boolean deleteDict(String idsStr);

    /**
     * 获取字典下拉列表
     *
     * @param typeCode
     * @return
     */
    List<Option> listDictOptions(String typeCode);

}
