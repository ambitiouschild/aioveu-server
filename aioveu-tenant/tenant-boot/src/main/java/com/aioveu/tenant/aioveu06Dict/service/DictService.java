package com.aioveu.tenant.aioveu06Dict.service;

import com.aioveu.common.model.Option;
import com.aioveu.tenant.aioveu06Dict.model.entity.Dict;
import com.aioveu.tenant.aioveu06Dict.model.form.DictForm;
import com.aioveu.tenant.aioveu06Dict.model.query.DictQuery;
import com.aioveu.tenant.aioveu06Dict.model.vo.DictPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: DictService
 * @Description TODO 字典业务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:46
 * @Version 1.0
 **/
public interface DictService extends IService<Dict> {


    /**
     * 获取字典分页列表
     *
     * @param queryParams 分页查询对象
     * @return 字典分页列表
     */
    Page<DictPageVO> getDictPage(DictQuery queryParams);

    /**
     * 获取字典列表
     *
     * @return 字典列表
     */
    List<Option<String>> getDictList();

    /**
     * 获取字典表单数据
     *
     * @param id 字典ID
     * @return 字典表单
     */
    DictForm getDictForm(Long id);

    /**
     * 新增字典
     *
     * @param dictForm 字典表单
     * @return 是否成功
     */
    boolean saveDict(DictForm dictForm);

    /**
     * 修改字典
     *
     * @param id     字典ID
     * @param dictForm 字典表单
     * @return 是否成功
     */
    boolean updateDict(Long id, DictForm dictForm);

    /**
     * 删除字典
     *
     * @param ids 字典ID集合
     */
    void deleteDictByIds(List<String> ids);

    /**
     * 根据字典ID列表获取字典编码列表
     *
     * @param ids 字典ID列表
     * @return 字典编码列表
     */
    List<String> getDictCodesByIds(List<String> ids);
}
