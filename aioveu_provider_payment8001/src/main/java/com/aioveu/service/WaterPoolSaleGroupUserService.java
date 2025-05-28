package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WaterPoolSaleGroupUser;
import com.aioveu.vo.WaterPoolSaleGroupUserVO;

import java.util.List;

public interface WaterPoolSaleGroupUserService extends IService<WaterPoolSaleGroupUser> {

    /**
     * 查询公海销售组用户列表
     * @return
     */
    IPage<WaterPoolSaleGroupUserVO> getGroupUserAll(int page, int size, Long saleGroupId);

    /**
     * 批量添加公海销售组用户
     * @param userGroup
     * @return
     */
    Boolean batchAdd(List<WaterPoolSaleGroupUser> userGroup);

    /**
     * 修改公海销售组用户
     * @param userGroup
     * @return
     */
    boolean updGroupById(WaterPoolSaleGroupUser userGroup);

    /**
     * 根据id删除公海销售组用户
     * @param id
     * @return
     */
    boolean deleteGroup(Long id);

    /**
     * 根据组id查询公海销售组 组下的用户
     * @return
     */
    List<WaterPoolSaleGroupUser> getUserAll(Long id);


}
