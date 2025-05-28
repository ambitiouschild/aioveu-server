package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.ExtensionPosition;
import com.aioveu.form.PushUserNearbyForm;
import com.aioveu.vo.ExtensionUserRangeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: xiaoyao
 * @date: 2022年10月14日
 * */
@Repository
public interface ExtensionPositionDao extends BaseMapper<ExtensionPosition> {

    /**
     * 获取商户参与主题的周边地推人员
     * @param form
     * @return
     */
    List<ExtensionUserRangeVo> getExtensionUserRange(PushUserNearbyForm form);

    /**
     * 获取店铺周边的地推人员
     * @param form
     * @return
     */
    List<ExtensionUserRangeVo> getStoreUserRange(PushUserNearbyForm form);

}
