package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.RedisConstant;
import com.aioveu.dao.ExtensionPositionDao;
import com.aioveu.entity.ExtensionPosition;
import com.aioveu.entity.Store;
import com.aioveu.entity.Topic;
import com.aioveu.exception.SportException;
import com.aioveu.form.PushUserNearbyForm;
import com.aioveu.form.TopicCodeForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.vo.ExtensionPageCodeVo;
import com.aioveu.vo.ExtensionUserRangeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@Service
public class ExtensionPositionServiceImpl extends ServiceImpl<ExtensionPositionDao, ExtensionPosition>
        implements ExtensionPositionService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ExtensionPageCodeVo getTopicCode(TopicCodeForm form) {
        Long topicId = form.getTopicId();
        Topic topic = topicService.getById(topicId);
        if (topic == null) {
            throw new SportException("主题不存在!");
        }

        ExtensionPageCodeVo extensionPageCodeVo = new ExtensionPageCodeVo();
        String prefix = topicId + "_" + form.getUserId();
        String key = "MINI_CODE_EXTENSION_" + prefix;
        String pushShareKey = RedisConstant.PUSH_USER_SHARE_KEY + form.getUserId();
        String fileName = "mini-code/extension/" + prefix + ".png";
        String page = "pages/project/pages/projectDetail/projectDetail";
        String shareKey = redisTemplate.opsForValue().get(pushShareKey);
        boolean needCache = false;
        if (shareKey == null) {
            shareKey = RandomStringUtils.randomAlphanumeric(24);
            redisTemplate.opsForValue().set(pushShareKey, shareKey);
        } else {
            needCache = true;
        }
        log.info("shareKey:" + shareKey);
        String pageImage = codeService.miniPageCode(topicId + "," + shareKey, key, page, fileName, "wxd6d275b4e626447d", needCache);
        extensionPageCodeVo.setUrl(FileUtil.getImageFullUrl(pageImage));

        extensionPageCodeVo.setDescription(topic.getIntroduce());
        extensionPageCodeVo.setQa(topic.getQa());
        extensionPageCodeVo.setTopicName(topic.getName());
        extensionPageCodeVo.setEndTime(topic.getEndTime());

        String redisKey = RedisConstant.TOPIC_CODE_TIME + shareKey;
        log.info("redisKey:" + redisKey);
        // 刷新二维码有效期
        redisTemplate.opsForValue().set(redisKey, form.getUserId(), 10, TimeUnit.MINUTES);

        positionRecord(form, 2);
        return extensionPageCodeVo;
    }

    @Override
    public boolean positionRecord(TopicCodeForm form, Integer status) {
        return recordPosition(form.getLongitude(), form.getLatitude(), form.getTopicId(), form.getUserId(), form.getRunStep(), 1);
    }

    @Override
    public String checkCodeExpire(String pushUserKey) {
        String redisKey = RedisConstant.TOPIC_CODE_TIME + pushUserKey;
        log.info("checkCodeExpire redisKey:" + redisKey);
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public boolean recordPosition(Double longitude, Double latitude, Long topicId, String userId, Integer runStep, Integer status) {
        ExtensionPosition extensionPosition = new ExtensionPosition();
        extensionPosition.setExtensionId(userId);
        extensionPosition.setThemeId(topicId);
        extensionPosition.setStatus(status);
        extensionPosition.setLatitude(latitude);
        extensionPosition.setLongitude(longitude);
        extensionPosition.setRunStep(runStep);

        return save(extensionPosition);
    }

    @Override
    public List<ExtensionUserRangeVo> getExtensionUserRange(PushUserNearbyForm form) {
        Store store = storeService.getById(form.getStoreId());
        Double latitude = store.getLatitude();
        Double longitude = store.getLongitude();
        form.setLongitude(longitude);
        form.setLatitude(latitude);
        List<ExtensionUserRangeVo> extensionUserRangeVoList = getBaseMapper().getStoreUserRange(form);
        if (CollectionUtils.isNotEmpty(extensionUserRangeVoList)) {
            extensionUserRangeVoList.forEach(item -> item.setLogoUrl(FileUtil.getImageFullUrl(item.getLogoUrl())));
        }
        return extensionUserRangeVoList;
    }
}
