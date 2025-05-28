package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.TopicDao;
import com.aioveu.entity.Topic;
import com.aioveu.entity.TopicImage;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.enums.TopicStatus;
import com.aioveu.exception.SportException;
import com.aioveu.form.TopicForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.RingTopic;
import com.aioveu.vo.TopicBaseVO;
import com.aioveu.vo.TopicDetailVO;
import com.aioveu.vo.TopicItemVO;
import com.aioveu.vo.TopicStoreItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class TopicServiceImpl extends ServiceImpl<TopicDao, Topic> implements TopicService {

    @Autowired
    private TopicImageService topicImageService;

    @Autowired
    private TopicCategoryService topicCategoryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExtensionPositionService extensionPositionService;

    @Autowired
    private PushTopicService pushTopicService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicExerciseService topicExerciseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService;

    @Override
    public IPage<TopicBaseVO> getTopicListByCategoryId(int page, int size, Long categoryId, String userId) {
        IPage<TopicBaseVO> topicBaseVOIPage = getBaseMapper().getTopicListByCategoryId(new Page<>(page, size),
                categoryId, userId, categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()));
        if (CollectionUtils.isNotEmpty(topicBaseVOIPage.getRecords())) {
            topicBaseVOIPage.getRecords().forEach(item -> {
                item.setCover(FileUtil.getImageFullUrl(item.getCover()));
            });
        }
        return topicBaseVOIPage;
    }

    @Override
    public boolean create(TopicForm form) {
        checkTopic(form);
        Topic topic = new Topic();
        BeanUtils.copyProperties(form, topic);
        topic.setStatus(TopicStatus.UN_REVIEWED.getCode());

        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("color",topic.getColor());
        if (count(queryWrapper) > 0){
            throw new SportException("该颜色已经存在，请更换颜色");
        }
        return save(topic);
    }

    private void checkTopic(TopicForm form) {
        if (StringUtils.isEmpty(form.getColor())){
            throw new SportException("color不能为空");
        }
        String[] rgbArray = form.getColor().split(",");
        if (rgbArray.length != 3) {
            throw new SportException("RGB:" + form.getColor() + "颜色错误！");
        }
        RingTopic.testColorValueRange(Integer.parseInt(rgbArray[0]), Integer.parseInt(rgbArray[1]), Integer.parseInt(rgbArray[2]), 255);
    }

    @Override
    public boolean deleteById(Long id) {
        Topic topic = getById(id);
        if (topic != null) {
            //TODO 雒世松 如果有活动参与 不可删除
           // topicImageService.deleteImageByTopicId(id);
            //OssUtil.deleteFile(topic.getCover());
            topic.setStatus(TopicStatus.DELETE.getCode());
            getBaseMapper().updateById(topic);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTopic(TopicForm form) {
        if (form.getId() == null) {
            throw new SportException("id不能为空！");
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(form, topic);
        if (StringUtils.isNotEmpty(topic.getColor())) {
            checkTopic(form);
            QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("color",topic.getColor()).ne("id",topic.getId());
            if (count(queryWrapper) > 0){
                throw new SportException("该颜色已经存在，请更换颜色");
            }
        }

        if (form.getCover() != null && form.getCover().startsWith("http")) {
            topic.setCover(null);
        }
        if (updateById(topic)) {
            if (StringUtils.isNotEmpty(topic.getColor())) {
                // 更新主题下店铺的光环图片
                List<Long> storeIds = topicExerciseService.getStoreIdByTopic(topic.getId());
                if (CollectionUtils.isNotEmpty(storeIds)) {
                    for (Long storeId : storeIds) {
                        storeService.updateTopicLogo(storeId);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TopicForm getDetailById(Long id) {
        Topic topic = getById(id);
        if (topic != null) {
            TopicForm form = new TopicForm();
            BeanUtils.copyProperties(topic, form);
            form.setCover(FileUtil.getImageFullUrl(topic.getCover()));
            return form;
        }
        return null;
    }

    @Override
    public IPage<TopicItemVO> getList(int page, int size, Long categoryId) {
        return getBaseMapper().getManagerList(new Page<>(page, size), categoryId);
    }

    @Override
    public TopicBaseVO recommend(String username) {
        Long topicId = pushTopicService.getByUserId(userService.getUserIdFromCache(username));
        if (topicId == null) {
            throw new SportException("当前账号未配置地推主题, 请联系管理员配置！");
        }
        Topic topic = getById(topicId);
        if (topic.getStatus() != 1) {
            throw new SportException("地推主题已下架或者过期, 请联系管理员！");
        }
        TopicBaseVO idNameVO = new TopicBaseVO();
        idNameVO.setId(topicId);
        idNameVO.setName(topic.getName());
        idNameVO.setEndTime(topic.getEndTime());
        return idNameVO;
    }

    @Override
    public TopicDetailVO miniAppDetail(Long id, String pushUserKey, String userId) {
        Topic topic = getById(id);
        if (topic.getStatus().equals(DataStatus.DELETE.getCode())) {
            throw new SportException("主题活动不存在或已下架");
        }
        TopicDetailVO detailVO = new TopicDetailVO();
        BeanUtils.copyProperties(topic, detailVO);

        if (StringUtils.isNotEmpty(pushUserKey)) {
            String pushUserId = extensionPositionService.checkCodeExpire(pushUserKey);
            if (StringUtils.isEmpty(pushUserId)) {
                throw new SportException("主题二维码已过期, 请重新扫码!");
            }
            detailVO.setPushUserId(pushUserId);
        }

        detailVO.setCategoryList(topicCategoryService.getByTopicId(id));

        List<TopicImage> topicImageList = topicImageService.getByTopicId(id);
        detailVO.setImageList(topicImageList.stream().map(TopicImage::getUrl).collect(Collectors.toList()));
        return detailVO;
    }

    @Override
    public IPage<TopicStoreItemVO> getStoreCategoryList(int page, int size, Long categoryId) {
        IPage<TopicStoreItemVO> pageDetail = getBaseMapper().getStoreCategoryList(new Page<>(page, size), categoryId,null);
        if (CollectionUtils.isNotEmpty(pageDetail.getRecords())) {
            pageDetail.getRecords().forEach(item -> {
                item.setCover(FileUtil.getImageFullUrl(item.getCover()));
            });
        }
        return pageDetail;
    }

    @Override
    public TopicStoreItemVO getStoreNumberAndPushNumberList(Long categoryId,Long topicId) {
        IPage<TopicStoreItemVO> storeCategoryList = getBaseMapper().getStoreCategoryList(new Page<>(1, 10), categoryId, topicId);
        if (storeCategoryList.getTotal() > 0){
            return storeCategoryList.getRecords().get(0);
        }
        return null;

    }

    @Override
    public boolean upAndLow(TopicForm topicForm) {
        if (topicForm.getId() != null && topicForm.getStatus() != null){
            Topic topic = new Topic();
            BeanUtils.copyProperties(topicForm, topic);
            updateById(topic);
            return true;
        }else {
            throw new SportException("id或状态为空");
        }
    }
}
