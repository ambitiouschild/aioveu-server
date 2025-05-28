package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CompanyStoreUserDao;
import com.aioveu.entity.CompanyStoreUser;
import com.aioveu.entity.Store;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.service.StoreService;
import com.aioveu.service.UserService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.SportStreamUtils;
import com.aioveu.vo.CompanyStoreUserVo;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.UserPhoneVO;
import com.aioveu.vo.user.SportUserForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompanyStoreUserServiceImpl extends ServiceImpl<CompanyStoreUserDao, CompanyStoreUser> implements CompanyStoreUserService {

    @Autowired
    private CompanyStoreUserDao companyStoreUserDao;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Override
    public List<StoreSimpleVO> getStoreByUserId(String userId) {
        List<StoreSimpleVO> idNameVOList = companyStoreUserDao.getStoreByUserId(userId);
        if (CollectionUtils.isEmpty(idNameVOList)) {
            throw new SportException("当前用户无授权店铺, 请联系管理员开通!");
        }
        return idNameVOList.stream().filter(Objects::nonNull)
                .filter(item -> item.getId() != null)
                .peek(item -> item.setLogo(FileUtil.getImageFullUrl(item.getLogo())))
                .filter(SportStreamUtils.distinctByKey(StoreSimpleVO::getId)).collect(Collectors.toList());
    }

    @Override
    public boolean hasStorePermission(String userId, Long storeId, Long companyId) {
        SportUserForm user = userService.getManagerUserById(userId);
        if (user == null) {
            throw new SportException("用户id");
        }
        QueryWrapper<CompanyStoreUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyStoreUser::getUserId, userId)
                .eq(CompanyStoreUser::getStatus, DataStatus.NORMAL.getCode());

        if (CollectionUtils.isNotEmpty(user.getRoleCodes()) && user.getRoleCodes().stream().anyMatch(s -> s.startsWith("admin"))) {
            // 管理员角色的可以不用校验店铺权限
        } else {
            if (storeId != null && storeId > 0) {
                queryWrapper.lambda().eq(CompanyStoreUser::getStoreId, storeId);
            }
        }
        // 公司权限必须校验
        if (companyId != null && companyId > 0) {
            queryWrapper.lambda().eq(CompanyStoreUser::getCompanyId, companyId);
        }
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean hasCompanyPermission(String userId, Long companyId) {
        if (StringUtils.isEmpty(userId) || companyId == null) {
            throw new SportException("用户id或者公司id错误");
        }
        QueryWrapper<CompanyStoreUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("company_id", companyId)
                .isNull("store_id")
                .eq("status", DataStatus.NORMAL.getCode());
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean batchCreateByStoreId(String userId, List<Long> storeIds) {
        List<Store> stores = storeService.listByIds(storeIds);
        List<CompanyStoreUser> companyStoreUserList = stores.stream().map(new Function<Store, CompanyStoreUser>() {
            @Override
            public CompanyStoreUser apply(Store store) {
                CompanyStoreUser item = new CompanyStoreUser();
                item.setUserId(userId);
                item.setCompanyId(store.getCompanyId());
                item.setStoreId(store.getId());
                return item;
            }
        }).collect(Collectors.toList());
        return saveBatch(companyStoreUserList);
    }

    @Override
    public boolean delUserId(String userId) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, userId);
        return remove(wrapper);
    }

    @Override
    public boolean delUserId(String userId, Long storeId) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, userId);
        wrapper.lambda().eq(CompanyStoreUser::getStoreId, storeId);
        return remove(wrapper);
    }

    @Override
    public boolean delUserIdByCompanyId(String userId, Long companyId) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, userId);
        wrapper.lambda().eq(CompanyStoreUser::getCompanyId, companyId);
        return remove(wrapper);
    }

    @Override
    public List<CompanyStoreUser> getUserIdByCompanyId(String userId, Long companyId) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, userId)
                .eq(CompanyStoreUser::getCompanyId, companyId)
                .eq(CompanyStoreUser::getStatus, 1);
        return list(wrapper);
    }

    @Override
    public String getStoreAdminUser(Long storeId) {
        return getBaseMapper().getStoreAdminUser(storeId);
    }

    @Override
    public boolean checkUserStore(String userId, Long storeId) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, userId)
                .eq(CompanyStoreUser::getStoreId, storeId);
        return count(wrapper) > 0;
    }

    @Override
    public List<UserPhoneVO> findPhoneAndUserId(Long companyId,Long id ,Long storeId) {
        return companyStoreUserDao.findPhoneAndUserId(companyId, id, storeId);
    }

    @Override
    public List<CompanyStoreUserVo> getListByUserId(String userId) {
        return getBaseMapper().getListByUserId(userId);
    }

    @Override
    public boolean create(CompanyStoreUser companyStoreUser) {
        QueryWrapper<CompanyStoreUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CompanyStoreUser::getUserId, companyStoreUser.getUserId())
                .eq(CompanyStoreUser::getStoreId, companyStoreUser.getStoreId())
                .eq(CompanyStoreUser::getCompanyId, companyStoreUser.getCompanyId());
        if (count(wrapper) > 0) {
            throw new SportException("当前店铺已授权，请勿重复添加");
        }
        return save(companyStoreUser);
    }
}
