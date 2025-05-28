package com.aioveu.service;

import com.aioveu.entity.Audit;

/**
 * @description 审核通过的具体业务实现类
 * @author: 雒世松
 * @date: 2024/01/04 10:42
 */
public interface IApprovedService {

    /**
     * 审核通过
     * @param jsonVal 参数
     * @return
     */
    boolean approved(String jsonVal);

    /**
     * 审批不通过
     * @param jsonVal
     * @return
     */
    boolean reject(String jsonVal);

    /**
     * 提交申请，可重写自定义实现
     * 返回true，表示提交申请成功，默认true
     * 返回false，表示申请失败
     * @param audit
     * @return
     */
    default boolean submit(Audit audit){
        return true;
    }

}
