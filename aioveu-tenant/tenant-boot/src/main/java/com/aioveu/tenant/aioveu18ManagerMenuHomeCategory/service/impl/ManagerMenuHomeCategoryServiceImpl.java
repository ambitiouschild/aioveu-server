package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.converter.ManagerMenuHomeCategoryConverter;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.mapper.ManagerMenuHomeCategoryMapper;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.entity.ManagerMenuHomeCategory;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.form.ManagerMenuHomeCategoryForm;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.query.ManagerMenuHomeCategoryQuery;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.vo.ManagerMenuHomeCategoryVo;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.service.ManagerMenuHomeCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ManagerMenuHomeCategoryServiceImpl
 * @Description TODO 管理端app首页分类配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:42
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class ManagerMenuHomeCategoryServiceImpl extends ServiceImpl<ManagerMenuHomeCategoryMapper, ManagerMenuHomeCategory> implements ManagerMenuHomeCategoryService {


    private final ManagerMenuHomeCategoryConverter managerMenuHomeCategoryConverter;

    /**
     * 获取管理端app首页分类配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<ManagerMenuHomeCategoryVo>} 管理端app首页分类配置分页列表
     */
    @Override
    public IPage<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryPage(ManagerMenuHomeCategoryQuery queryParams) {
        Page<ManagerMenuHomeCategoryVo> page = this.baseMapper.getManagerMenuHomeCategoryPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取管理端app首页分类配置表单数据
     *
     * @param id 管理端app首页分类配置ID
     * @return 管理端app首页分类配置表单数据
     */
    @Override
    public ManagerMenuHomeCategoryForm getManagerMenuHomeCategoryFormData(Long id) {
        ManagerMenuHomeCategory entity = this.getById(id);
        return managerMenuHomeCategoryConverter.toForm(entity);
    }

    /**
     * 新增管理端app首页分类配置
     *
     * @param formData 管理端app首页分类配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveManagerMenuHomeCategory(ManagerMenuHomeCategoryForm formData) {
        ManagerMenuHomeCategory entity = managerMenuHomeCategoryConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新管理端app首页分类配置
     *
     * @param id   管理端app首页分类配置ID
     * @param formData 管理端app首页分类配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateManagerMenuHomeCategory(Long id,ManagerMenuHomeCategoryForm formData) {
        ManagerMenuHomeCategory entity = managerMenuHomeCategoryConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除管理端app首页分类配置
     *
     * @param ids 管理端app首页分类配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteManagerMenuHomeCategorys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的管理端app首页分类配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
