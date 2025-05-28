package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.WxOpenCodeCommitDao;
import com.aioveu.entity.WxOpenCodeCommit;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.WxOpenCodeCommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author： yao
 * @Date： 2025/2/14 15:30
 * @Describe：
 */
@Slf4j
@Service
public class WxOpenCodeCommitServiceImpl extends ServiceImpl<WxOpenCodeCommitDao, WxOpenCodeCommit> implements WxOpenCodeCommitService {

    @Override
    public WxOpenCodeCommit getLatestCodeCommit(String appId) {
        LambdaQueryWrapper<WxOpenCodeCommit> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WxOpenCodeCommit::getAppId, appId)
                .eq(WxOpenCodeCommit::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(WxOpenCodeCommit::getCreateDate)
                .last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<WxOpenCodeCommit> getListByIds(List<Long> codeCommitIds) {
        LambdaQueryWrapper<WxOpenCodeCommit> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WxOpenCodeCommit::getId, codeCommitIds);
        return list(wrapper);
    }

    @Override
    public WxOpenCodeCommit getBackCommit(String appId) {
        LambdaQueryWrapper<WxOpenCodeCommit> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WxOpenCodeCommit::getAppId, appId)
                .eq(WxOpenCodeCommit::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(WxOpenCodeCommit::getCreateDate)
                .last("limit 2");
        List<WxOpenCodeCommit> list = list(wrapper);
        if (list != null && list.size() > 1) {
            return list.get(1);
        }
        return null;
    }
}
