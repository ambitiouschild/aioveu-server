package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserRecommendRecordDao;
import com.aioveu.entity.UserRecommendRecord;
import com.aioveu.service.UserRecommendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 雒世松12
 * @Date 2022年10月10日
 */
@Slf4j
@Service
public class UserRecommendRecordServiceImpl extends ServiceImpl<UserRecommendRecordDao, UserRecommendRecord> implements UserRecommendRecordService {


    @Override
    public void bindExtensionUser(UserRecommendRecord userRecommendRecord) {
        save(userRecommendRecord);
    }
}
