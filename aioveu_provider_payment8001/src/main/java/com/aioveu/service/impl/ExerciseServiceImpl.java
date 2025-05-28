package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExerciseDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.DelayMessageType;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.exception.SportException;
import com.aioveu.form.ExerciseCustomForm;
import com.aioveu.form.ExerciseForm;
import com.aioveu.form.PushExerciseForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.ListUtil;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
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
public class ExerciseServiceImpl extends ServiceImpl<ExerciseDao, Exercise> implements ExerciseService {

    @Autowired
    private ExerciseDao exerciseDao;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ExerciseImageService exerciseImageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private TopicExerciseService topicExerciseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserVipCardService userVipCardService;


    @Autowired
    private JoinExerciseRuleService joinExerciseRuleService;

    @Autowired
    private MQMessageService mqMessageService;

    @Override
    public IPage<ExerciseManagerItemVO> getManagerAll(int page, int size, Long storeId, String storeName, Long categoryId, Integer status) {
        return exerciseDao.getManagerAll(new Page<>(page, size), storeId, categoryId, storeName, status);
    }

    @Override
    public Long copyById(Long id) {
        return copy(id, null, null, null, null);
    }

    @Override
    public Long copy(Long id, String name, Long storeId, Double originalPrice, Double price) {
        Exercise exercise = getById(id);
        exercise.setId(null);
        if (StringUtils.isNotEmpty(name)) {
            exercise.setName(name);
        } else {
            exercise.setName(exercise.getName());
        }
        if (exercise.getStatus() == 10 || exercise.getStatus() == 15) {
            Store store = storeService.getById(storeId);
            Company company = companyService.getById(store.getCompanyId());
            exercise.setAgreementTemplate(company.getGradeAgreementTemplate());
            exercise.setStatus(10);
        } else {
            exercise.setStatus(2);
        }
        exercise.setCreateDate(new Date());
        exercise.setUpdateDate(new Date());
        if (storeId != null) {
            exercise.setStoreId(storeId);
        }
        if (originalPrice != null) {
            exercise.setOriginalPrice(new BigDecimal(originalPrice));
        }
        if (price != null) {
            exercise.setPrice(new BigDecimal(price));
        }
        save(exercise);

        List<ExerciseImage> exerciseImageList = exerciseImageService.getByExerciseId(id);
        for (ExerciseImage image : exerciseImageList) {
            image.setId(null);
            image.setExerciseId(exercise.getId());
            image.setCreateDate(null);
            image.setUpdateDate(null);
        }
        exerciseImageService.saveBatch(exerciseImageList);
        return exercise.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long id, Integer status) {
        Exercise exercise = new Exercise();
        exercise.setId(id);
        exercise.setStatus(status);

        Exercise old = getById(id);
        if (status == 2 && old.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()))) {
            // 主题活动 在主题上面也下架
            List<TopicExercise> topicExerciseList = topicExerciseService.getByExercise(id);
            if (CollectionUtils.isNotEmpty(topicExerciseList)) {
                for (TopicExercise te : topicExerciseList) {
                    te.setStatus(6);
                    topicExerciseService.unJoin(te);
                }
            }
        } else if (status == 1 && old.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()))) {
            // 主题活动上架 在主题上面也需要上架
            List<TopicExercise> topicExerciseList = topicExerciseService.getByExercise(id);
            if (CollectionUtils.isNotEmpty(topicExerciseList)) {
                for (TopicExercise te : topicExerciseList) {
                    te.setStatus(1);
                    topicExerciseService.join(te);
                }
            }
        }
        if (saveOrUpdate(exercise)) {
            return true;
        }
        throw new SportException("操作失败！");
    }

    @Override
    public boolean exerciseDelete(Long id) {
        return changeStatus(id, DataStatus.DELETE.getCode());
    }

    @Override
    public ExerciseManagerDetailVO managerDetail(Long id) {
        Exercise exercise = getById(id);
        if (exercise != null) {
            ExerciseManagerDetailVO exerciseManagerDetailVO = new ExerciseManagerDetailVO();
            BeanUtils.copyProperties(exercise, exerciseManagerDetailVO);
            Store store = storeService.getById(exercise.getStoreId());
            if (store != null) {
                exerciseManagerDetailVO.setStoreName(store.getName());
                exerciseManagerDetailVO.setCompanyId(store.getCompanyId());
            }
            exerciseManagerDetailVO.setExerciseCouponList(exerciseCouponService.getByExerciseId(id));

            exerciseManagerDetailVO.setJoinExerciseRules(joinExerciseRuleService.getByExerciseId(id.toString()));

            return exerciseManagerDetailVO;
        }
        return null;
    }

    @Override
    public ExerciseVO getDetail(Long id, Boolean preview) {
        ExerciseVO exerciseVO = exerciseDao.getDetail(id, preview);
        if (exerciseVO != null) {
            if (CollectionUtils.isNotEmpty(exerciseVO.getImageList())) {
                exerciseVO.setImageList(exerciseVO.getImageList().stream().map(FileUtil::getImageFullUrl).collect(Collectors.toList()));
            }
            if (exerciseVO.getExerciseStartTime() != null) {
                exerciseVO.setExerciseTime(SportDateUtils.get2Day(exerciseVO.getExerciseStartTime(), exerciseVO.getExerciseEndTime()));
            }
            if (exerciseVO.getStartTime() != null) {
                if (exerciseVO.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.COUNT.getCode()))) {
                    exerciseVO.setEnrollTime(SportDateUtils.get2Day(exerciseVO.getExerciseStartTime(), exerciseVO.getExerciseEndTime()));
                } else {
                    exerciseVO.setEnrollTime(SportDateUtils.get2Day(exerciseVO.getStartTime(), exerciseVO.getEndTime()));
                }
            }
            IPage<UserEnterVO> pageInfo =
                    orderDetailService.getEnrollUserList(1, 9, Collections.singleton(exerciseVO.getId()), exerciseVO.getCategoryId());
            exerciseVO.setEnrollUserList(pageInfo.getRecords().stream()
                    .peek(item -> item.setHead(FileUtil.getImageFullUrl(item.getHead()))).collect(Collectors.toList()));
            exerciseVO.setEnrollCount((int) pageInfo.getTotal());
            exerciseVO.setJoinExerciseRule(joinExerciseRuleService.getByExerciseId(exerciseVO.getId().toString()));
            return exerciseVO;
        } else {
            throw new SportException("该商品已下架或者失效!");
        }
    }

    @Override
    public ExerciseCountDetailVO getCountDetail(Long id) {
        ExerciseCountDetailVO exerciseVO = getBaseMapper().getCountDetail(id);
        if (exerciseVO != null) {
            if (exerciseVO.getExerciseStartTime() != null) {
                exerciseVO.setExerciseTime(SportDateUtils.get2Day(exerciseVO.getExerciseStartTime(), exerciseVO.getExerciseEndTime()));
            }
            IPage<UserEnterVO> pageInfo =
                    orderDetailService.getEnrollUserList(1, 9, Collections.singleton(exerciseVO.getId()), exerciseVO.getCategoryId());
            exerciseVO.setEnrollUserList(pageInfo.getRecords().stream()
                    .peek(item -> item.setHead(FileUtil.getImageFullUrl(item.getHead()))).collect(Collectors.toList()));
            exerciseVO.setEnrollCount((int) pageInfo.getTotal());
            return exerciseVO;
        } else {
            throw new SportException("该商品已下架或者失效!");
        }
    }

    @Override
    public IPage<BaseServiceItemVO> getAll(Map<String, Object> param) throws Exception {
        ListUtil.checkParam(param, false);

//        if (param.get("keyword")!=null && param.get("userId")!=null){
//            SearchHistory searchHistory = new SearchHistory();
//            searchHistory.setCategoryId(1L);
//            searchHistory.setKeyword(param.get("keyword")+"");
//            searchHistory.setUserId(param.get("userId")+"");
//            searchHistoryService.save(searchHistory);
//        }

        Integer page = NumberUtils.toInt(Optional.ofNullable(param.get("page")).orElse(1).toString());
        Integer pageSize = (NumberUtils.toInt(Optional.ofNullable(param.get("size")).orElse(10).toString()));
        if (param.get("categoryCode") != null) {
            param.put("categoryId", categoryService.getByCode(param.get("categoryCode").toString()));
        }
        return exerciseDao.getAll(new Page<>(page, pageSize), param);
    }

    @Override
    public List<ProductSimpleVO> getBatchHotExerciseByStoreIds(List<Long> storeIds) {
        return getBaseMapper().getBatchHotExerciseByStoreIds(storeIds);
    }

    /**
     * 保存活动图片
     * @param id
     * @param image
     * @return
     */
    private boolean saveExerciseImage(Long id, String image) {
        ExerciseImage exerciseImage = new ExerciseImage();
        exerciseImage.setExerciseId(id);
        exerciseImage.setUrl(image);
        exerciseImage.setImageType(0);
        return exerciseImageService.save(exerciseImage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrUpdate(ExerciseForm exercise) {
        Long formId = exercise.getId();
        boolean flag = false;
        if (saveOrUpdate(exercise)) {
            if (formId != null) {
                Exercise old = getById(exercise.getId());
                if (!old.getImage().equals(exercise.getImage())) {
                    // 删除活动/课程的 的导航图
                    exerciseImageService.deleteByExerciseIdAndImageType(exercise.getId(), 0);
                    saveExerciseImage(exercise.getId(), exercise.getImage());
                }
            } else if (StringUtils.isNotEmpty(exercise.getImage())) {
                saveExerciseImage(exercise.getId(), exercise.getImage());
            }

            // 删除活动/课程对应的优惠券
            exerciseCouponService.deleteByExerciseId(exercise.getId());

            List<ExerciseCoupon> exerciseCouponList = exercise.getExerciseCouponList();
            if (CollectionUtils.isNotEmpty(exerciseCouponList)) {
                if (saveExerciseCoupon(exerciseCouponList, exercise.getId(), exercise.getCategoryId())) {
                    flag = true;
                }
            } else {
                flag = true;
            }
        }

        //删除对应的拼单规则
        joinExerciseRuleService.deleteByExerciseId(exercise.getId().toString());

        //拼单分类
        //重新插入拼单规则表中，并且插入延时队列
        Long category = categoryService.getByCode(ExerciseCategory.JOIN_EXERCISE.getCode());
        List<JoinExerciseRule> rules = exercise.getJoinExerciseRules();
        if (exercise.getCategoryId().equals(category)){
            if(CollectionUtils.isNotEmpty(rules)) {
                for (int i = 0; i < rules.size(); i++) {
                    rules.get(i).setExerciseId(exercise.getId());
                    rules.get(i).setStoreId(exercise.getStoreId());
                }
                joinExerciseRuleService.saveBatch(rules);
            }
            //新建-拼单，
            // 往延迟队列中插入数据，拼单活动结束时，统计下单用户，并按照规则返现。
            // 延迟时间=拼单活动结束时间
            if (formId == null){
                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("type", DelayMessageType.JOIN_EXERCISE_FINISH.getCode());
                msgMap.put("exerciseId", exercise.getId());
                msgMap.put("categoryId", exercise.getCategoryId());
                msgMap.put("endTime", exercise.getExerciseEndTime());
                mqMessageService.sendDelayMsgByDate(msgMap, exercise.getExerciseEndTime());
            }
        }

        if (flag){
            return flag;
        }

        throw new SportException("创建失败！");
    }

    /**
     * 保存活动优惠券
     *
     * @param exerciseCouponList
     * @param exerciseId
     * @param categoryId
     * @return
     */
    private boolean saveExerciseCoupon(List<ExerciseCoupon> exerciseCouponList, Long exerciseId, Long categoryId) {
        for (ExerciseCoupon item : exerciseCouponList) {
            if (item.getCouponNumber() == null) {
                throw new SportException("优惠券领取数量不能为空！");
            }
            if (item.getCouponTemplateId() == null) {
                throw new SportException("优惠券id不能为空！");
            }
            item.setExerciseId(exerciseId);
            item.setCategoryId(categoryId);
        }
        return exerciseCouponService.batchSave(exerciseCouponList);
    }

    @Override
    public String getAppIdByExerciseId(Long id) {
        return getBaseMapper().getAppIdByExerciseId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exerciseCustom(ExerciseCustomForm form) {
        List<ExerciseCoupon> exerciseCouponList = form.getExerciseCouponList();
        if (CollectionUtils.isEmpty(exerciseCouponList)) {
            throw new SportException("优惠券不能为空！");
        }
        Exercise exercise = new Exercise();
        BeanUtils.copyProperties(form, exercise);
        exercise.setCategoryId(categoryService.getByCode(ExerciseCategory.CUSTOM.getCode()));
        exercise.setStatus(10);
        Store store = storeService.getById(form.getStoreId());
        Company company = companyService.getById(store.getCompanyId());
        exercise.setAgreementTemplate(company.getGradeAgreementTemplate());
        if (save(exercise) && saveExerciseCoupon(exerciseCouponList, exercise.getId(), exercise.getCategoryId())) {
            return true;
        }
        throw new SportException("灵活课包创建失败！");
    }

    @Override
    public IPage<ExerciseManagerItemVO> getCustomList(int page, int size, Long storeId) {
        return getBaseMapper().getManagerAll(new Page<>(page, size), storeId,
                categoryService.getByCode(ExerciseCategory.CUSTOM.getCode()), null, null);
    }

    @Override
    public void status2Invalid(Long exerciseId) {
        Exercise exercise = getById(exerciseId);
        if (exercise.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.CUSTOM.getCode()))
                && exercise.getStatus() == 10) {
            Exercise up = new Exercise();
            up.setId(exerciseId);
            up.setStatus(15);
            updateById(up);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrUpdatePush(PushExerciseForm form) {
        Exercise exercise = new Exercise();
        exercise.setId(form.getId());
        exercise.setName(form.getName());
        exercise.setDescription(form.getDescription());
        exercise.setStoreId(form.getStoreId());
        exercise.setStartTime(form.getStartTime());
        exercise.setEndTime(form.getEndTime());
        exercise.setLimitNumber(form.getLimitNumber());
        exercise.setOriginalPrice(form.getPrice());
        exercise.setPrice(form.getPrice());
        exercise.setOriginalPrice(form.getOriginalPrice());

        if (form.getId() == null) {
            exercise.setCategoryId(categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()));
            exercise.setStatus(1);
            // 将第一张图片作为活动的封面
            if (CollectionUtils.isNotEmpty(form.getImageList())){
                exercise.setImage(form.getImageList().get(0));
            }
        }
        if (saveOrUpdate(exercise)) {
            if (CollectionUtils.isNotEmpty(form.getImageList())) {
                exerciseImageService.deleteByExerciseId(exercise.getId());
                List<ExerciseImage> exerciseImages = form.getImageList().stream()
                        .map(item -> {
                            ExerciseImage ei = new ExerciseImage();
                            ei.setImageType(2);
                            ei.setExerciseId(exercise.getId());
                            ei.setUrl(item);
                            ei.setPriority(0);
                            return ei;
                        }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(exerciseImages)) {
                    exerciseImageService.saveBatch(exerciseImages);
                }
            }
            // 新建的地推活动，创建完直接加入到主题里面
            if (form.getId() == null) {
                TopicExercise topicExercise = new TopicExercise();
                topicExercise.setTopicId(form.getTopicId());
                topicExercise.setCategoryId(form.getCategoryId());
                topicExercise.setExerciseId(exercise.getId());
                topicExercise.setStoreId(exercise.getStoreId());
                topicExerciseService.join(topicExercise);
            } else {
                TopicExercise old = topicExerciseService.getByExerciseAndTopicId(form.getId(), form.getTopicId());
                if (form.getCategoryId() != null) {
                    TopicExercise topicExercise = new TopicExercise();
                    topicExercise.setId(old.getId());
                    topicExercise.setCategoryId(form.getCategoryId());
                    topicExerciseService.updateById(topicExercise);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IPage<PushExerciseItemVO> getPushList(int page, int size, Long storeId) {
        return exerciseDao.getPushList(new Page<>(page, size), storeId, categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()));
    }

    @Override
    public Exercise selExerciseById(long id) {
        return getById(id);
    }

    @Override
    public PushExerciseForm getPush(Long id, Long topicId) {
        Exercise exercise = getById(id);
        if (exercise != null) {
            PushExerciseForm pushExerciseForm = new PushExerciseForm();
            pushExerciseForm.setId(id);
            pushExerciseForm.setName(exercise.getName());
            pushExerciseForm.setStoreId(exercise.getStoreId());
            pushExerciseForm.setDescription(exercise.getDescription());
            pushExerciseForm.setStartTime(exercise.getStartTime());
            pushExerciseForm.setEndTime(exercise.getEndTime());
            pushExerciseForm.setLimitNumber(exercise.getLimitNumber());
            pushExerciseForm.setTopicId(topicId);
            pushExerciseForm.setPrice(exercise.getPrice());
            pushExerciseForm.setOriginalPrice(exercise.getOriginalPrice());
            TopicExercise topicExercise = topicExerciseService.getByExerciseAndTopicId(id, topicId);
            if (topicExercise != null) {
                pushExerciseForm.setCategoryId(topicExercise.getCategoryId());
            }
            List<ExerciseImage> exerciseImages = exerciseImageService.getByExerciseId(id);
            pushExerciseForm.setImageList(exerciseImages.stream()
                    .map(item -> FileUtil.getImageFullUrl(item.getUrl()))
                    .collect(Collectors.toList()));
            return pushExerciseForm;
        }
        return null;
    }

    @Override
    public boolean cancelTop(List<Long> exerciseIds) {
        List<Exercise> exerciseList = exerciseIds.stream().map(id -> {
            Exercise exercise = new Exercise();
            exercise.setId(id);
            exercise.setTop(false);
            return exercise;
        }).collect(Collectors.toList());
        return updateBatchById(exerciseList);
    }

    @Override
    public BigDecimal getVipPrice(Long id, String username) {
        Exercise exercise = getById(id);
        if (categoryService.getByCode(ExerciseCategory.COURSE.getCode()).equals(exercise.getCategoryId())) {
            String userId = userService.getUserIdFromCache(username);
            Store store = storeService.getById(exercise.getStoreId());
            UserVipCard userVipCard = userVipCardService.getUserVipCard(userId, exercise.getCategoryId(), store.getCompanyId(), store.getId());
            if (userVipCard != null) {
                return exercise.getVipPrice();
            }
        }
        return null;
    }

    @Override
    public List<IdNameVO> getExperience() {
        return getBaseMapper().getExperience(categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()));
    }

    @Override
    public Store getCompanyIdById(Long exerciseId) {
        return getBaseMapper().getCompanyIdById(exerciseId);
    }

    @Override
    public List<BaseServiceItemVO> getMiniStoreExerciseList(Long storeId, String categoryCode) {
        Long categoryId = categoryService.getByCode(categoryCode);
        Date now = new Date();
        QueryWrapper<Exercise> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Exercise::getStoreId, storeId)
                .eq(Exercise::getStatus, DataStatus.NORMAL.getCode())
                .eq(Exercise::getCategoryId, categoryId)
                .gt(Exercise::getExerciseEndTime, now)
                .orderByDesc(Exercise::getHot, Exercise::getExerciseStartTime);
        wrapper.last("limit 4");

        List<Exercise> experienceList = list(wrapper);
        if (CollectionUtils.isNotEmpty(experienceList)) {
            return experienceList.stream().map(item -> {
                BaseServiceItemVO baseServiceItemVO = new BaseServiceItemVO();
                BeanUtils.copyProperties(item, baseServiceItemVO);
                baseServiceItemVO.setImage(FileUtil.getImageFullUrl(baseServiceItemVO.getImage()));
                baseServiceItemVO.setLowerPrice(item.getPrice());
                if (ExerciseCategory.COUNT.getCode().equals(categoryCode)) {
                    baseServiceItemVO.setDate(SportDateUtils.get2Day(item.getExerciseStartTime(), item.getExerciseEndTime()));
                } else {
                    if (now.before(item.getStartTime())) {
                        baseServiceItemVO.setDate(DateFormatUtils.format(item.getStartTime(), "yyyy-MM-dd"));
                    } else {
                        baseServiceItemVO.setDate(DateFormatUtils.format(now, "yyyy-MM-dd"));
                    }
                }
                return baseServiceItemVO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
