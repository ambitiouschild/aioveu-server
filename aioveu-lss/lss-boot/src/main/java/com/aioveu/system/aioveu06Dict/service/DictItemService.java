package com.aioveu.system.aioveu06Dict.service;

import com.aioveu.system.aioveu06Dict.model.entity.DictItem;
import com.aioveu.system.aioveu06Dict.model.form.DictItemForm;
import com.aioveu.system.aioveu06Dict.model.query.DictItemPageQuery;
import com.aioveu.system.aioveu06Dict.model.vo.DictItemOptionVO;
import com.aioveu.system.aioveu06Dict.model.vo.DictItemPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
  *@ClassName: ConfigService
  *@Description TODO  字典项接口
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/20 18:01
  *@Version 1.0
  **/
public interface DictItemService extends IService<DictItem> {

    /**
     * 字典项分页列表
     *
     * @param queryParams 查询参数
     * @return 字典项分页列表
     */
    Page<DictItemPageVO> getDictItemPage(DictItemPageQuery queryParams);

    /**
     * 获取字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表
     */
    List<DictItemOptionVO> getDictItems(String dictCode);

    /**
     * 获取字典项表单
     *
     * @param itemId 字典项ID
     * @return 字典项表单
     */
    DictItemForm getDictItemForm(Long itemId);

    /**
     * 保存字典项
     *
     * @param formData 字典项表单
     * @return 是否成功
     */
    boolean saveDictItem(DictItemForm formData);

    /**
     * 更新字典项
     *
     * @param formData 字典项表单
     * @return 是否成功
     */
    boolean updateDictItem(DictItemForm formData);

    /**
     * 删除字典项
     *
     * @param ids 字典项ID,多个逗号分隔
     */
    void deleteDictItemByIds(String ids);
}
