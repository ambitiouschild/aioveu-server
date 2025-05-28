package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.WaterPoolSaleGroupDao;
import com.aioveu.entity.WaterPoolSaleGroup;
import com.aioveu.entity.WaterPoolSaleGroupUser;
import com.aioveu.exception.SportException;
import com.aioveu.service.WaterPoolSaleGroupService;
import com.aioveu.service.WaterPoolSaleGroupUserService;
import com.aioveu.vo.user.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class WaterPoolSaleGroupServiceImpl extends ServiceImpl<WaterPoolSaleGroupDao, WaterPoolSaleGroup> implements WaterPoolSaleGroupService {

    @Autowired
    private WaterPoolSaleGroupUserService userGroupService;

    @Override
    public List<String> getGroupUserIdByGroupName(String name, Long companyId) {
        QueryWrapper<WaterPoolSaleGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WaterPoolSaleGroup::getName, name)
                .eq(WaterPoolSaleGroup::getCompanyId, companyId);

        WaterPoolSaleGroup waterPoolSaleGroup = getOne(queryWrapper);
        if (waterPoolSaleGroup == null) {
            throw new SportException(name + "不存在!");
        }
        return getBaseMapper().getGroupUserIdList(waterPoolSaleGroup.getId());
    }

    @Override
    public IPage<WaterPoolSaleGroup> getByStoreId(int page, int size, Long storeId) {
        QueryWrapper<WaterPoolSaleGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WaterPoolSaleGroup::getStoreId,storeId);
        IPage<WaterPoolSaleGroup> iPage = page(new Page<>(page, size), queryWrapper);
        return iPage;
    }

    @Override
    public Boolean create(WaterPoolSaleGroup waterPoolSaleGroup) {
        // 公司
        if (waterPoolSaleGroup.getCompanyId()==null) {
            throw new SportException("公司不能为空!");
        }
        // 授权店铺
        if (waterPoolSaleGroup.getStoreId()==null) {
            throw new SportException("授权店铺不能为空!");
        }
        return save(waterPoolSaleGroup);
    }



    @Override
    public boolean updGroupById(WaterPoolSaleGroup waterPoolSaleGroup) {
        // id
        if (waterPoolSaleGroup.getId()==null) {
            throw new SportException("id不能为空!");
        }
        // 公司
        if (waterPoolSaleGroup.getCompanyId()==null) {
            throw new SportException("公司不能为空!");
        }
        // 授权店铺
        if (waterPoolSaleGroup.getStoreId()==null) {
            throw new SportException("授权店铺不能为空!");
        }
        UpdateWrapper<WaterPoolSaleGroup> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(WaterPoolSaleGroup::getId, waterPoolSaleGroup.getId());
        WaterPoolSaleGroup waterPoolSaleGroups = new WaterPoolSaleGroup();
        waterPoolSaleGroups.setName(waterPoolSaleGroup.getName());
        waterPoolSaleGroups.setCompanyId(waterPoolSaleGroup.getCompanyId());
        waterPoolSaleGroups.setStoreId(waterPoolSaleGroup.getStoreId());
        return update(waterPoolSaleGroups,wrapper);
    }

    @Override
    public boolean deleteGroup(Long id) {
        List<WaterPoolSaleGroupUser> userAll = userGroupService.getUserAll(id);
        if (CollectionUtils.isNotEmpty(userAll)){
            // 有销售用户 不能删除
            throw new SportException("当前销售组有用户, 不可以删除!");
        }
        return removeById(id);
    }

}
