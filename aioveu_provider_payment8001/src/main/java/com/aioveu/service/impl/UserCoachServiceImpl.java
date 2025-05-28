package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserCoachDao;
import com.aioveu.entity.UserCoach;
import com.aioveu.service.UserCoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserCoachServiceImpl extends ServiceImpl<UserCoachDao, UserCoach> implements UserCoachService {

    @Override
    public List<UserCoach> getByUserId(String userId) {
        QueryWrapper<UserCoach> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.lambda().eq(UserCoach::getUserId, userId);
        return list(wrapperTeacher);
    }

    @Override
    public List<UserCoach> getByUserId(String userId, Long storeId) {
        return this.getBaseMapper().getByUserId(userId,storeId);
    }

    @Override
    public boolean create(String userId, List<Long> coachIdList, Long storeId, Long companyId) {
        return saveBatch(coachIdList.stream().map(coachId -> {
            UserCoach userCoach = new UserCoach();
            userCoach.setUserId(userId);
            userCoach.setCoachId(coachId);
            userCoach.setCompanyId(companyId);
            userCoach.setStoreId(storeId);
            return userCoach;
        }).collect(Collectors.toList()));
    }

    @Override
    public boolean delUserId(String userId) {
        QueryWrapper<UserCoach> userCoachQueryWrapper = new QueryWrapper<>();
        userCoachQueryWrapper.lambda().eq(UserCoach::getUserId, userId);
        return remove(userCoachQueryWrapper);
    }

    @Override
    public boolean delUserId(String userId, List<Long> coachId) {
        QueryWrapper<UserCoach> userCoachQueryWrapper = new QueryWrapper<>();
        userCoachQueryWrapper.lambda().eq(UserCoach::getUserId, userId)
                .in(UserCoach::getCoachId,coachId);
        return remove(userCoachQueryWrapper);
    }

    @Override
    public Long getStoreUserCoach(String userId, Long storeId) {
        return getBaseMapper().getStoreUserCoach(userId, storeId);
    }

    @Override
    public String getCoachUserIdByGradeId(Long gradeId) {
        return getBaseMapper().getCoachUserIdByGradeId(gradeId);
    }
}
