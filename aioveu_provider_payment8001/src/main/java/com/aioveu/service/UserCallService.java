package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserCall;
import com.aioveu.form.UserCallFollow;
import com.aioveu.form.UserCallUpdateForm;
import com.aioveu.vo.NewUserInfoVO;
import com.aioveu.vo.UserCallSimpleVO;
import com.aioveu.vo.UserCallVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserCallService extends IService<UserCall> {

    IPage<NewUserInfoVO> newUserInfoList(int page, int size, String createUserId, String userId,String timeFrom, String timeTo, Integer status, String keyword);

    boolean updateUserReceiveCallToUser(String userId, String transferPhone);

    /**
     * 列表门店的所有教练/销售池
     * @param page
     * @param size
     * @param storeId
     * @param keyword
     * @return
     */
    IPage<UserCallVO> getAllListByPage(int page, int size,Long storeId, String keyword, Integer type);
    /**
     * 列表
     * @param page
     * @param size
     * @param userId
     * @param keyword
     * @return
     */
    IPage<UserCallVO> getList(int page, int size, String userId, String keyword, Integer type, Long storeId);

    /**
     * 批量添加 到自己的私有池
     * @param userId
     * @param userInfoList
     * @return
     */
    boolean batchAdd(String userId, List<Long> userInfoList, Integer type);

    /**
     * 自动将用户添加到指定用户的私海池
     * @param userPhone 用户手机号
     * @param userId 用户
     * @param type 类型
     * @param storeId
     * @param nickName
     * @return
     */
    boolean autoAddMyPool(String userPhone, String userId, Integer type, Long storeId, String nickName);

    /**
     * 更新状态 跟进完成
     * @param userCallIds
     * @return
     */
    boolean batchUpdateStatus(List<Long> userCallIds);

    /**
     * 更新联系人拨打信息
     * @param form
     * @return
     */
    boolean updateUserCall(UserCallUpdateForm form);

    /**
     * 跟进的简单列表
     * @param page
     * @param size
     * @param userInfoId
     * @return
     */
    IPage<UserCallSimpleVO> getSimpleList(int page, int size, Long userInfoId);

    /**
     * 用户信息跟进
     * @param userCallFollow
     * @return
     */
    Boolean userCallFollow(UserCallFollow userCallFollow);

    /**
     * 平均分配公海用户给组下面的用户
     * @param userInfoIdList
     * @param userIdList
     */
    void distributionUserInfo(List<Long> userInfoIdList, List<String> userIdList);


    /**
     * 添加跟进记录 私海移除到公海
     * @param userInfoList
     * @param userId
     * @param nickName
     * @return
     */
    Boolean removeRecords(List<Long> userInfoList, String userId, String nickName);

    /**
     * 添加跟进记录 私海添加到公海
     * @param userInfoList
     * @param userId
     * @param nickName
     * @return
     */
    Boolean addRecords(List<Long> userInfoList, String userId, String nickName);

    /**
     * 添加跟进记录 导入到公海
     * @param userInfoList
     * @param userId
     * @param nickName
     * @return
     */
    Boolean exportRecords(List<Long> userInfoList, String userId, String nickName);


    /**
     * 客户列表、学员列表
     * 客户归属--修改
     * 销售客户池、私海池归属更新
     * @param userId   客户id
     * @param phone    客户号码
     * @param coachUserId   教练私海池userid
     * @param salesUserId   销售客户池userid
     * @param companyId     公司id
     * @return
     */
    boolean updateUserReceiveCalls(String userId, String phone, String coachUserId, String salesUserId, String companyId) ;



}
