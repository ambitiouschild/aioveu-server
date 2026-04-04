package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.converter.ManagerMenuCategoryItemConverter;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.mapper.ManagerMenuCategoryItemMapper;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.form.ManagerMenuCategoryItemForm;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.query.ManagerMenuCategoryItemQuery;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.vo.ManagerMenuCategoryItemVo;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.service.ManagerMenuCategoryItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ManagerMenuCategoryItemServiceImpl
 * @Description TODO 管理系统工作台菜单项（多租户支持）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:29
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerMenuCategoryItemServiceImpl extends ServiceImpl<ManagerMenuCategoryItemMapper, ManagerMenuCategoryItem> implements ManagerMenuCategoryItemService {


    private final ManagerMenuCategoryItemConverter managerMenuCategoryItemConverter;

    /**
     * 获取管理系统工作台菜单项（多租户支持）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<ManagerMenuCategoryItemVo>} 管理系统工作台菜单项（多租户支持）分页列表
     */
    @Override
    public IPage<ManagerMenuCategoryItemVo> getManagerMenuCategoryItemPage(ManagerMenuCategoryItemQuery queryParams) {
        Page<ManagerMenuCategoryItemVo> page = this.baseMapper.getManagerMenuCategoryItemPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理系统工作台菜单项（多租户支持）表单数据
     *
     * @param id 管理系统工作台菜单项（多租户支持）ID
     * @return 管理系统工作台菜单项（多租户支持）表单数据
     */
    @Override
    public ManagerMenuCategoryItemForm getManagerMenuCategoryItemFormData(Long id) {
        ManagerMenuCategoryItem entity = this.getById(id);
        return managerMenuCategoryItemConverter.toForm(entity);
    }

    /**
     * 新增管理系统工作台菜单项（多租户支持）
     *
     * @param formData 管理系统工作台菜单项（多租户支持）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveManagerMenuCategoryItem(ManagerMenuCategoryItemForm formData) {
        ManagerMenuCategoryItem entity = managerMenuCategoryItemConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理系统工作台菜单项（多租户支持）
     *
     * @param id   管理系统工作台菜单项（多租户支持）ID
     * @param formData 管理系统工作台菜单项（多租户支持）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateManagerMenuCategoryItem(Long id,ManagerMenuCategoryItemForm formData) {
        ManagerMenuCategoryItem entity = managerMenuCategoryItemConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理系统工作台菜单项（多租户支持）
     *
     * @param ids 管理系统工作台菜单项（多租户支持）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteManagerMenuCategoryItems(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理系统工作台菜单项（多租户支持）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    /**
     * 获取对应分类下的菜单项
     */
    @Override
    public List<ManagerMenuCategoryItem>  getManagerMenuCategoryItemsWithCategoryIds(List<Long> categoryIds){

        // 3. 查询这些分类下的菜单项
        LambdaQueryWrapper<ManagerMenuCategoryItem> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.in(ManagerMenuCategoryItem::getCategoryId, categoryIds)
                .eq(ManagerMenuCategoryItem::getStatus, 1)
                .eq(ManagerMenuCategoryItem::getIsDeleted, 0)
                .orderByAsc(ManagerMenuCategoryItem::getSort);

        List<ManagerMenuCategoryItem> managerMenuCategoryItems = this.list(itemQuery);

        log.info("【ManagerMenuCategoryItem】查询这些分类下的菜单项：{}",managerMenuCategoryItems);
        return managerMenuCategoryItems;
    }


}
