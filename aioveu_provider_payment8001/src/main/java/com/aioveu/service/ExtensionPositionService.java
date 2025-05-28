package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExtensionPosition;
import com.aioveu.form.PushUserNearbyForm;
import com.aioveu.form.TopicCodeForm;
import com.aioveu.vo.ExtensionPageCodeVo;
import com.aioveu.vo.ExtensionUserRangeVo;

import java.util.List;

/**
 * @description
 * @author: xy
 */
public interface ExtensionPositionService extends IService<ExtensionPosition> {


    /**
     * 地推位置记录
     * @param longitude
     * @param latitude
     * @param topicId
     * @param userId
     * @param runStep
     * @param status
     * @return
     */
    boolean recordPosition(Double longitude, Double latitude, Long topicId, String userId, Integer runStep, Integer status);

    /**
     * 按照地图获取地推人员信息
     * @param form
     * @return
     */
    List<ExtensionUserRangeVo> getExtensionUserRange(PushUserNearbyForm form);

    /**
     * 地推获取主题的二维码
     * @param form
     * @return
     */
    ExtensionPageCodeVo getTopicCode(TopicCodeForm form);

    /**
     * 位置记录
     * @param form
     * @param status
     * @return
     */
    boolean positionRecord(TopicCodeForm form, Integer status);

    /**
     * 检查主题码是否过期
     * @param pushUserKey
     * @return
     */
    String checkCodeExpire(String pushUserKey);

}
