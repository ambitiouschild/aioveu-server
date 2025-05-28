package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.RoleUserDao;
import com.aioveu.entity.RoleUser;
import com.aioveu.exception.SportException;
import com.aioveu.service.RoleUserService;
import com.aioveu.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserDao, RoleUser> implements RoleUserService {

    @Autowired
    private RoleUserDao roleUserDao;

    @Override
    public List<String> getByUserId(String userId) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getUserId, userId);
        return list(roleUserUpdateWrapper).stream().map(RoleUser::getRoleCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getByUserId(String userId,Long storeId) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getUserId, userId)
                .eq(RoleUser::getStoreId, storeId);
        return list(roleUserUpdateWrapper).stream().map(RoleUser::getRoleCode).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public boolean save(String userId, List<String> roleCodes) {
        roleUserDao.delete(new QueryWrapper<RoleUser>().eq("user_id", userId));
        List<RoleUser> roleUserList = roleCodes.stream().map(code -> {
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleCode(code);
            return roleUser;
        }).collect(Collectors.toList());
        return saveBatch(roleUserList);
    }

    @Override
    public boolean save(String userId, List<String> roleCodes, Long storeId, Long companyId) {
        roleUserDao.delete(new QueryWrapper<RoleUser>()
                .eq("user_id", userId)
                .eq("store_id", storeId));
        List<RoleUser> roleUserList = roleCodes.stream().map(code -> {
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleCode(code);
            roleUser.setStoreId(storeId);
            roleUser.setCompanyId(companyId);
            return roleUser;
        }).collect(Collectors.toList());
        return saveBatch(roleUserList);
    }

    @Override
    public boolean delByUserId(String userId) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getUserId, userId);
        return remove(roleUserUpdateWrapper);
    }

    @Override
    public boolean delByUserIdAndStoreId(String userId, Long storeId) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getUserId, userId)
                .eq(RoleUser::getStoreId,storeId);
        return remove(roleUserUpdateWrapper);
    }

    @Override
    public List<RoleUser> getByRoleCode(String roleCode) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getRoleCode, roleCode);
        return list(roleUserUpdateWrapper);
    }

    @Override
    public List<UserRoleVo> getUserRole(String userId) {
        return getBaseMapper().getUserRole(userId);
    }

    @Override
    public boolean saveUserRole(RoleUser roleUser) {
        QueryWrapper<RoleUser> roleUserUpdateWrapper = new QueryWrapper<>();
        roleUserUpdateWrapper.lambda().eq(RoleUser::getRoleCode, roleUser.getRoleCode())
                .eq(RoleUser::getUserId, roleUser.getUserId());
        if (count(roleUserUpdateWrapper) > 0) {
            throw new SportException("当前角色已添加，请勿重复添加");
        }
        return save(roleUser);
    }
}
