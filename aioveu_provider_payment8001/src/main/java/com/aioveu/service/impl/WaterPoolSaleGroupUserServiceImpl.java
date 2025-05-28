package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.WaterPoolSaleGroupUserDao;
import com.aioveu.entity.WaterPoolSaleGroupUser;
import com.aioveu.service.WaterPoolSaleGroupUserService;
import com.aioveu.vo.WaterPoolSaleGroupUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WaterPoolSaleGroupUserServiceImpl extends ServiceImpl<WaterPoolSaleGroupUserDao, WaterPoolSaleGroupUser> implements WaterPoolSaleGroupUserService {

    @Autowired
    private WaterPoolSaleGroupUserDao userGroupDao;

    @Override
    public IPage<WaterPoolSaleGroupUserVO> getGroupUserAll(int page, int size, Long saleGroupId) {
        return userGroupDao.getGroupUserAll(new Page<>(page,size),saleGroupId);
    }

    /*@Override
    public boolean batchAdd(List<Group> groupList) {
        for (Group group : groupList) {
            saveGroup(group);
        }
        return true;
    }*/

    @Override
    public Boolean batchAdd(List<WaterPoolSaleGroupUser> userGroup) {
        for (WaterPoolSaleGroupUser user : userGroup) {
            save(user);
        }
        return true;
    }

    @Override
    public boolean updGroupById(WaterPoolSaleGroupUser userGroup) {
        UpdateWrapper<WaterPoolSaleGroupUser> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(WaterPoolSaleGroupUser::getId, userGroup.getId());
        WaterPoolSaleGroupUser userGroups = new WaterPoolSaleGroupUser();
        userGroups.setSaleGroupId(userGroup.getSaleGroupId());
        userGroups.setUserId(userGroup.getUserId());
        return update(userGroups,wrapper);
    }

    @Override
    public boolean deleteGroup(Long id) {
        return removeById(id);
    }

    @Override
    public List<WaterPoolSaleGroupUser> getUserAll(Long id) {
        return userGroupDao.getUserAll(id);
    }


}
