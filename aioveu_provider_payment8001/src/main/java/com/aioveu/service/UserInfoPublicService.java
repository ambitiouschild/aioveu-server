package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserInfoPublic;
import com.aioveu.excel.bean.UserInfoCallBean;
import com.aioveu.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserInfoPublicService extends IService<UserInfoPublic> {

    /**
     * 导入数据
     * @param file
     * @return
     */
    boolean importData(MultipartFile file);

    /**
     * 批量保存
     * @param list
     * @param userId
     * @return
     */
    boolean batchSave(List<UserInfoCallBean> list, String userId);

    /**
     * 列表
     * @param page
     * @param size
     * @return
     */
    IPage<UserInfoVO> getList(int page, int size);

    /**
     * 手机号查找
     * @param phone
     * @return
     */
    UserInfoPublic getByPhone(String phone);
}
