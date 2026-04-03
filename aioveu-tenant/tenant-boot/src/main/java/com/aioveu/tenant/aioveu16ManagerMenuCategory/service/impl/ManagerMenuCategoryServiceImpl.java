package com.aioveu.tenant.aioveu16ManagerMenuCategory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.converter.ManagerMenuCategoryConverter;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.mapper.ManagerMenuCategoryMapper;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query.ManagerMenuCategoryQuery;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryVo;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.service.ManagerMenuCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ManagerMenuCategoryServiceImpl
 * @Description TODO 管理端菜单分类（多租户）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:14
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class ManagerMenuCategoryServiceImpl extends ServiceImpl<ManagerMenuCategoryMapper, ManagerMenuCategory> implements ManagerMenuCategoryService {

    private final ManagerMenuCategoryConverter managerMenuCategoryConverter;

    /**
     * 获取管理端菜单分类（多租户）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<ManagerMenuCategoryVo>} 管理端菜单分类（多租户）分页列表
     */
    @Override
    public IPage<ManagerMenuCategoryVo> getManagerMenuCategoryPage(ManagerMenuCategoryQuery queryParams) {
        Page<ManagerMenuCategoryVo> page = this.baseMapper.getManagerMenuCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理端菜单分类（多租户）表单数据
     *
     * @param id 管理端菜单分类（多租户）ID
     * @return 管理端菜单分类（多租户）表单数据
     */
    @Override
    public ManagerMenuCategoryForm getManagerMenuCategoryFormData(Long id) {
        ManagerMenuCategory entity = this.getById(id);
        return managerMenuCategoryConverter.toForm(entity);
    }

    /**
     * 新增管理端菜单分类（多租户）
     *
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveManagerMenuCategory(ManagerMenuCategoryForm formData) {
        ManagerMenuCategory entity = managerMenuCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理端菜单分类（多租户）
     *
     * @param id   管理端菜单分类（多租户）ID
     * @param formData 管理端菜单分类（多租户）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateManagerMenuCategory(Long id,ManagerMenuCategoryForm formData) {
        ManagerMenuCategory entity = managerMenuCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理端菜单分类（多租户）
     *
     * @param ids 管理端菜单分类（多租户）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteManagerMenuCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理端菜单分类（多租户）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
