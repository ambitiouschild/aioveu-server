package com.aioveu.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.aioveu.excel.bean.UserInfoCallBean;
import com.aioveu.service.UserInfoPublicService;
import com.aioveu.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/20 0020 21:45
 */
@Slf4j
public class UserInfoCallListener extends AnalysisEventListener<UserInfoCallBean> {

    /**
     * 每隔2000条存储数据库，然后清理list方便内存回收
     */
    private static final int BATCH_COUNT = 2000;

    private List<UserInfoCallBean> list = new ArrayList<>();

    private UserInfoService userInfoService;

    private Long companyId;

    private String userId;

    private int successCount;

    private UserInfoPublicService userInfoPublicService;

    public void setUserInfoPublicService(UserInfoPublicService userInfoPublicService) {
        this.userInfoPublicService = userInfoPublicService;
    }

    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    @Override
    public void invoke(UserInfoCallBean userInfoCallBean, AnalysisContext analysisContext) {
        userInfoCallBean.setCompanyId(companyId);
        list.add(userInfoCallBean);
        successCount += 1;
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
             saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        if (userInfoService != null) {
            userInfoService.batchSave(list, userId, 0);
        } else if (userInfoPublicService != null) {
            userInfoPublicService.batchSave(list, userId);
            userInfoService.batchSave(list, userId, 1);
        }
        log.info("存储数据库成功！");
    }
}
