package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserInfo;
import com.aioveu.excel.bean.UserInfoCallBean;
import com.aioveu.excel.bean.UserOrderBean;
import com.aioveu.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 导入数据
     * @param file
     * @param companyId
     * @param username
     * @return
     */
    String importData(MultipartFile file, Long companyId, String username);

    /**
     * 批量保存
     * @param list
     * @param userId
     * @param type 0 私海公海导入 1 珊瑚公海导入
     * @return
     */
    boolean batchSave(List<UserInfoCallBean> list, String userId, Integer type);

    /**
     * 列表
     * @param page
     * @param size
     * @param companyId
     * @param username
     * @return
     */
    IPage<UserInfoVO> getList(int page, int size, Long companyId, Integer type, String username, String phone);

    /**
     * 用户详情
     * @param id
     * @return
     */
    UserInfoDetailVO detail(Long id);

    /**
     * 订单列表
     * @param page
     * @param size
     * @param phone
     * @return
     */
    IPage<UserInfoOrderVO> orderList(int page, int size, String phone);

    /**
     * 预约订单列表
     * @param page
     * @param size
     * @param phone
     * @return
     */
    IPage<UserInfoOrderVO> appointmentList(int page, int size, String phone);

    /**
     * 更新最后一次联系时间
     * @param id
     * @param lastCall
     * @return
     */
    boolean updateLastCallDate(Long id, Date lastCall);

    /**
     * 根据id获取编辑的客户信息
     * @param id
     * @return
     */
    UserInfoEditVO edit(Long id);

    /**
     * 创建一条线索
     * @param userInfo
     * @param userId
     * @return
     */
    boolean create(UserInfo userInfo, String userId, String introduce);

    /**
     * 根据手机号获取用户私海池数据
     * @param phone
     * @return
     */
    UserInfoCreateOrderVO getByPhone(String phone, Long companyId);

    /**
     * 用户信息移出私海 添加移除记录 更新信息资料状态
     * @param userInfoList
     * @param userId
     * @param nickName
     * @return
     */
    boolean batchRemove(List<Long> userInfoList, String userId, String nickName);

    /**
     * 用户信息添加私海 添加添加记录 更新信息资料状态
     * @param userInfoList
     * @param userId
     * @param nickName
     * @return
     */
    boolean batchAddUserPool(List<Long> userInfoList, String userId, String nickName);

    /**
     * 导入订单和用户
     * @param file
     * @param companyId
     */
    void importOrder(MultipartFile file, Long companyId);

    /**
     * 自定义订单批量保存
     * @param list
     * @return
     */
    boolean batchOrderSave(List<UserOrderBean> list);

    /**
     * 公海显示数量增加
     * @return
     */
    Integer addUserInfoNumber();

    /**
     * 获取用户的id
     * @param id
     * @return
     */
    String getUserId(Long id);

    /**
     * 获取公司对应的手机号用户的所属销售或者教练的用户id
     * @param phone
     * @param companyId
     * @return
     */
    String getUserIdByPhoneAndCompanyId(String phone, Long companyId);

    /**
     * 根据用户id，公司id
     * 获取用户的归属教练
     * @param userId
     * @param companyId
     * @return
     */
    String getUserIdByIdAndCompanyId(String userId, Long companyId);

    /**
     * 更新私海池 用户资料
     * @param phone
     * @param username
     * @param childName
     * @param childAge
     * @param companyId
     * @return
     */
    boolean updateUserAndChildNameAndAge(String phone, String username, String childName, Integer childAge, Long companyId);

    /**
     * 手机号查询用户资料
     * @param phone
     * @return
     */
    UserInfo getByUserPhone(String phone, Long companyId);

    /**
     * 通过手机号和公司id更新小孩名称
     * @param phone
     * @param companyId
     * @param childName
     * @return
     */
    boolean updateChildNameByPhoneAndCompanyId(String phone, Long companyId, String childName);

}
