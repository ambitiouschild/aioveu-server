package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.TopicExerciseDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.exception.SportException;
import com.aioveu.form.TopicExerciseForm;
import com.aioveu.form.TopicExercisePrePayForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class TopicExerciseServiceImpl extends ServiceImpl<TopicExerciseDao, TopicExercise> implements TopicExerciseService {

    @Autowired
    private TopicCategoryService topicCategoryService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ExercisePushRewardConfigService exercisePushRewardConfigService;

    @Autowired
    private ExerciseShowRecordService exerciseShowRecordService;

    @Autowired
    private UserExtensionAccountService userExtensionAccountService;

    @Override
    public boolean create(Long exerciseId, Long topicId, Long categoryId) {
        if (categoryId != null) {
            List<CategoryBaseVO> categoryList = topicCategoryService.getByTopicId(topicId);
            if (CollectionUtils.isEmpty(categoryList)) {
                throw new SportException("该主题未配置分类！");
            }
            if (categoryList.stream().noneMatch(item -> item.getId().equals(categoryId))) {
                throw new SportException("分类id错误！");
            }
        }
        TopicExercise te = new TopicExercise();
        te.setExerciseId(exerciseId);
        te.setCategoryId(categoryId);
        te.setTopicId(topicId);
        return join(te);
    }

    @Override
    public TopicExerciseJoinDetailVO joinList(Long topicId, Long storeId) {
        Topic topic = topicService.getById(topicId);
        if (topic != null) {
            TopicExerciseJoinDetailVO topicExerciseJoinDetailVO = new TopicExerciseJoinDetailVO();
            topicExerciseJoinDetailVO.setCover(FileUtil.getImageFullUrl(topic.getCover()));
            topicExerciseJoinDetailVO.setTopicId(topicId);
            topicExerciseJoinDetailVO.setTopicName(topic.getName());
            topicExerciseJoinDetailVO.setEndTime(topic.getEndTime());

            List<CategoryBaseVO> categoryName = topicCategoryService.getByTopicId(topicId);
            if (categoryName.size()>0){
                topicExerciseJoinDetailVO.setCategoryName(categoryName);
            }

            TopicStoreItemVO storeNumberAndPushNumberList = topicService.getStoreNumberAndPushNumberList(topic.getCategoryId(), topic.getId());
            if (Objects.nonNull(storeNumberAndPushNumberList)){
                topicExerciseJoinDetailVO.setStoreNumber(storeNumberAndPushNumberList.getStoreNumber());
                topicExerciseJoinDetailVO.setPushNumber(storeNumberAndPushNumberList.getPushNumber());
            }

            List<IdNameVO> signList = new ArrayList<>();
            Exercise exercise = getBaseMapper().getByStoreAndTopic(storeId, topicId);
            if (exercise != null) {
                signList.add(new IdNameVO(exercise.getId(), exercise.getName()));
            }
            topicExerciseJoinDetailVO.setSignList(signList);

            return topicExerciseJoinDetailVO;
        }
        return null;
    }

    @Override
    public IPage<ExerciseTopicItemVO> exerciseList(TopicExerciseForm form) {
        // 121.480248,31.236276 上海市人民政府坐标
        Map<String, Object> params = new HashMap<>();
        params.put("topicId", form.getTopicId());
        params.put("categoryId", form.getCategoryId());
        params.put("latitude", form.getLatitude());
        params.put("longitude", form.getLongitude());
        params.put("userId", form.getUserId());
        params.put("productCategoryId", categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()));
        if (params.get("latitude") == null) {
            params.put("latitude", 31.236276);
            params.put("longitude", 121.480248);
        }

        params.put("sort", "T.top desc");
        Page<ExerciseTopicItemVO> iPage = new Page<>(form.getPage(), form.getSize());
//        iPage.setOptimizeCountSql(false);
        iPage.setSearchCount(false);

        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getTopicId, form.getTopicId());
        iPage.setTotal(count(queryWrapper));

        IPage<ExerciseTopicItemVO> page = getBaseMapper().getExerciseList(iPage, params);
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            List<ExerciseShowRecord> exerciseShowRecords = new ArrayList<>((int)page.getSize());
            page.getRecords().forEach(item -> {
                ExerciseShowRecord er = new ExerciseShowRecord();
                if (item.getId() != null) {
                    er.setExerciseId(item.getId());
                    er.setTopicId(form.getTopicId());
                    er.setUserId(form.getUserId());
                    exerciseShowRecords.add(er);
                } else {
                    log.error("活动id为空:" + JacksonUtils.obj2Json(params));
                }

                List<String> imageList = item.getImageList();
                if (imageList != null) {
                    List<String> newImageList = new ArrayList<>(imageList.size());
                    imageList.forEach(url -> {
                        newImageList.add(FileUtil.getImageFullUrl(url));
                    });
                    item.setImageList(newImageList);
                }
                item.setStoreLogo(FileUtil.getImageFullUrl(item.getStoreLogo()));
                item.setRewardProduct(exercisePushRewardConfigService.getStoreGift(item.getId(), form.getTopicId()));

            });
            //TODO 后续可以考虑消息队列异步刷新置顶
            exerciseTop(form.getTopicId());
            exerciseShowRecordService.saveBatch(exerciseShowRecords);
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean join(TopicExercise te) {
        Exercise exercise = exerciseService.getById(te.getExerciseId());
        te.setStoreId(exercise.getStoreId());

        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getStoreId, te.getStoreId()).
                eq(TopicExercise::getTopicId, te.getTopicId())
                .eq(TopicExercise::getStatus,1).or().eq(TopicExercise::getStatus,6);
        TopicExercise topicExercise = getOne(queryWrapper);
        if (topicExercise != null && te.getStatus() == null) {
            if (topicExercise.getStatus().equals(6)){
                throw new SportException("该主题存在下架的活动，请联系管理员！");
            }
            throw new SportException("该主题已有活动参加, 不可重复参加");
        }
        if (topicExercise == null) {
            // 商户自己的活动加入主题
            // 系统默认插入3条奖励设置
            if (exercisePushRewardConfigService.createSystemConfig(te.getExerciseId(), te.getTopicId()) && save(te)) {
                storeService.updateTopicLogo(exercise.getStoreId());
                return true;
            }
        } else if (topicExercise.getStatus() == 6) {
            // 后台上架主题活动
            TopicExercise updateTe = new TopicExercise();
            updateTe.setId(topicExercise.getId());
            updateTe.setStatus(1);
            return updateById(updateTe);
        }
        throw new SportException("加入失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unJoin(TopicExercise te) {
        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getExerciseId, te.getExerciseId()).
                eq(TopicExercise::getTopicId, te.getTopicId());
        if (te.getStatus() == null || te.getStatus() == 0) {
            exercisePushRewardConfigService.deleteConfig(te.getExerciseId(), te.getTopicId());
        }
        TopicExercise topicExercise = getOne(queryWrapper);
        if (Objects.nonNull(topicExercise)) {
            Exercise exercise = new Exercise();
            exercise.setId(topicExercise.getExerciseId());
            if (te.getStatus() == null || te.getStatus() == 0) {
                // 商户自己删除退出
                exercise.setStatus(0);
                topicExercise.setStatus(0);
                exerciseService.updateById(exercise);
            } else if (te.getStatus() == 6){
                // 系统后台操作下架
                topicExercise.setStatus(te.getStatus());
            }
            updateById(topicExercise);
            storeService.updateTopicLogo(topicExercise.getStoreId());
            return true;
        }
        throw new SportException("退出失败");
    }

    @Override
    public List<TopicExercise> getByStoreId(Long storeId) {
        return getBaseMapper().getByStoreId(storeId);
    }

    @Override
    public ExerciseTopicPrePayVO prePay(TopicExercisePrePayForm form) {
        if (CollectionUtils.isEmpty(form.getExerciseIdList())) {
            throw new SportException("报名活动不能为空");
        }
        Topic topic = topicService.getById(form.getTopicId());
        if (topic == null || !topic.getStatus().equals(DataStatus.NORMAL.getCode())) {
            throw new SportException("主题不存在或已下架");
        }
        ExerciseTopicPrePayVO exerciseTopicPrePayVO = new ExerciseTopicPrePayVO();
        List<Exercise> exerciseList = exerciseService.listByIds(form.getExerciseIdList());
        BigDecimal totalPrice = new BigDecimal(0);
        for (Exercise item : exerciseList) {
            totalPrice = totalPrice.add(item.getPrice());
        }
        exerciseTopicPrePayVO.setPrice(totalPrice);
        exerciseTopicPrePayVO.setExerciseList(getBaseMapper().getByIdList(form.getExerciseIdList()));
        return exerciseTopicPrePayVO;
    }

    @Override
    public List<Long> getUserBuyExercise(Long topicId, String userId) {
        return getBaseMapper().getUserBuyExercise(topicId, categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()), userId);
    }

    @Override
    public TopicExercise getByExerciseAndTopicId(Long exerciseId, Long topicId) {
        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getExerciseId, exerciseId).
                eq(TopicExercise::getTopicId, topicId);
        return getOne(queryWrapper);
    }

    @Override
    public List<String> getStoreTopicColor(Long storeId) {
        return getBaseMapper().getStoreTopicColor(storeId);
    }

    @Override
    public void exerciseTop(Long topicId) {
        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getTopicId, topicId);
        List<TopicExercise> topicExerciseList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(topicExerciseList)) {
            TopicExercise max = topicExerciseList.stream()
                    .max(Comparator.comparingInt(TopicExercise::getPriority))
                    .orElseThrow(IllegalAccessError::new);
            TopicExercise min = topicExerciseList.stream()
                    .min(Comparator.comparingInt(TopicExercise::getPriority))
                    .orElseThrow(IllegalAccessError::new);
            min.setPriority(max.getPriority() + 1);
            exerciseService.cancelTop(topicExerciseList.stream().map(TopicExercise::getExerciseId).collect(Collectors.toList()));
            getBaseMapper().updateExerciseTop(min.getExerciseId(), min.getId(), min.getPriority());
        }
    }

    @Override
    public List<Long> getStoreIdByTopic(Long topicId) {
        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getTopicId, topicId)
                .eq(TopicExercise::getStatus, DataStatus.NORMAL.getCode());
        List<TopicExercise> topicExerciseList = list(queryWrapper);
        return topicExerciseList.stream().map(TopicExercise::getStoreId).collect(Collectors.toList());
    }

    @Override
    public List<TopicExercise> getByExercise(Long exerciseId) {
        QueryWrapper<TopicExercise> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicExercise::getExerciseId, exerciseId);
        return list(queryWrapper);
    }
}
