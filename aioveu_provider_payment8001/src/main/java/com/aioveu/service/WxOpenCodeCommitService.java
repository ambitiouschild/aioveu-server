package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WxOpenCodeCommit;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2025/2/12 15:27
 * @Describe：
 */
public interface WxOpenCodeCommitService  extends IService<WxOpenCodeCommit> {
    /**
     * 获取最新的上传记录
     * @param appId
     * @return
     */
    WxOpenCodeCommit getLatestCodeCommit(String appId);

    List<WxOpenCodeCommit> getListByIds(List<Long> codeCommitIds);

    /**
     * 获取上上一个版本
     * @param appId
     * @return
     */
    WxOpenCodeCommit getBackCommit(String appId);

}
