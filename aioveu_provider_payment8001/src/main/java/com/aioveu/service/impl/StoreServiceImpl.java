package com.aioveu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreDao;
import com.aioveu.dto.ExerciseVipDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.exception.SportException;
import com.aioveu.form.StoreBindUserPhoneForm;
import com.aioveu.form.StoreForm;
import com.aioveu.service.*;
import com.aioveu.utils.*;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<StoreDao, Store> implements StoreService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Autowired
    private StoreImageService storeImageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private StoreProductCategoryService storeProductCategoryService;

    @Autowired
    private TopicExerciseService topicExerciseService;

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private VipCardService vipCardService;

    @Value(value = "${sport.image.path}")
    private String sportImagePath;

    @Autowired
    private JoinExerciseRuleService joinExerciseRuleService;

    @Autowired
    private ChargingOptionService chargingOptionService;

    @Override
    public IPage<StoreForm> getAll(int page, int size, Long companyId, String keyword, Integer status) {
        return getBaseMapper().getAll(new Page<>(page, size), companyId, keyword, status);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        Store store = new Store();
        store.setId(id);
        store.setStatus(status);
        return saveOrUpdate(store);
    }

    @Override
    public StoreForm detail(Long id) {
        Store store = getById(id);
        if (store != null) {
            StoreForm storeVO = new StoreForm();
            BeanUtils.copyProperties(store, storeVO);
            Company company = companyService.getById(store.getCompanyId());
            if (company != null) {
                storeVO.setCompanyName(company.getName());
            }
            List<Long> categoryIds = storeProductCategoryService.getCategoryIdsByStoreId(id);
            if (CollectionUtils.isNotEmpty(categoryIds)) {
                storeVO.setCategoryIds(categoryIds.stream().map(item -> "" + item).collect(Collectors.joining(",")));
            }
//            UserVO phoneAndUserId = companyStoreUserService.findPhoneAndUserId(storeVO.getCompanyId(), store.getId());
//            if (Objects.nonNull(phoneAndUserId)){
//                storeVO.setUserId(phoneAndUserId.getId());
//                storeVO.setPhone(phoneAndUserId.getPhone());
//            }
            return storeVO;
        }
        return null;
    }

    @Override
    public List<Store> getByCompanyId(Long companyId) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Store::getCompanyId, companyId)
                .eq(Store::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public IPage<StoreMiniVO> getMiniList(Map<String, Object> param) throws Exception {
        ListUtil.checkParam(param, false);
        if (param.get("sort") == null) {
            param.put("sort", "T.recommend_order desc");
        }
        if ("-1".equals(param.get("categoryId") + "") || "null".equals(param.get("categoryId") + "") || StringUtils.isEmpty(param.get("categoryId") + "")) {
            param.put("categoryId", null);
        }
        if (param.get("appId") != null) {
            param.put("companyId", companyService.getIdByAppId(param.get("appId").toString()));
        }
        Integer page = NumberUtils.toInt(Optional.ofNullable(param.get("page")).orElse(1).toString());
        Integer pageSize = (NumberUtils.toInt(Optional.ofNullable(param.get("size")).orElse(10).toString()));

        IPage<StoreMiniVO> pageResult = getBaseMapper().getMiniList(new Page<>(page, pageSize), param);
        List<StoreMiniVO> storeMiniList = pageResult.getRecords();
        if (CollectionUtils.isNotEmpty(storeMiniList)) {
            List<ProductSimpleVO> exerciseList = exerciseService.getBatchHotExerciseByStoreIds(storeMiniList.stream().map(StoreMiniVO::getId).collect(Collectors.toList()));
            Map<Long, List<ProductSimpleVO>> exerciseMap = exerciseList.stream().collect(Collectors.groupingBy(ProductSimpleVO::getStoreId));
            Long experienceCategoryId = categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode());
            Long exerciseCategoryId = categoryService.getByCode(ExerciseCategory.EXERCISE.getCode());
            Long courseCategoryId = categoryService.getByCode(ExerciseCategory.COURSE.getCode());
            Comparator<ProductSimpleVO> reversed = Comparator.comparing(ProductSimpleVO::getEnrollNumber).reversed();
            for (StoreMiniVO item : storeMiniList) {
                item.setLogo(FileUtil.getImageFullUrl(item.getLogo()));
                List<ProductSimpleVO> allProductList = exerciseMap.get(item.getId());
                if (CollectionUtils.isNotEmpty(allProductList)) {
                    List<ProductSimpleVO> productList = new ArrayList<>();
                    // 添加一个热门体验课
                    productList.addAll(allProductList.stream().filter(p -> p.getCategoryId().equals(experienceCategoryId))
                                    .sorted(reversed).limit(1)
                            .peek(product -> {
                                product.setCategoryUrl(FileUtil.getImageFullUrl(product.getCategoryUrl()));
                            }).collect(Collectors.toList()));
                    // 添加一个活动
                    productList.addAll(allProductList.stream().filter(p -> p.getCategoryId().equals(exerciseCategoryId))
                            .sorted(reversed).limit(1)
                            .peek(product -> {
                                product.setCategoryUrl(FileUtil.getImageFullUrl(product.getCategoryUrl()));
                            }).collect(Collectors.toList()));
                    // 添加2个课程
                    productList.addAll(allProductList.stream().filter(p -> p.getCategoryId().equals(courseCategoryId))
                            .sorted(reversed).limit(1)
                            .peek(product -> {
                                product.setCategoryUrl(FileUtil.getImageFullUrl(product.getCategoryUrl()));
                            }).collect(Collectors.toList()));
                    item.setProductList(productList);
                }
            }
            pageResult.setRecords(storeMiniList);
        }
        return pageResult;
    }

    @Override
    public StoreMiniDetailVO getMiniDetail(Long id) {
        Store store = getById(id);
        if (store != null) {
            StoreMiniDetailVO storeMiniDetailVO = new StoreMiniDetailVO();
            BeanUtils.copyProperties(store, storeMiniDetailVO);
            Date now = new Date();
            storeMiniDetailVO.setLogo(FileUtil.getImageFullUrl(storeMiniDetailVO.getLogo()));

            List<StoreVenue> storeVenues = storeVenueService.findByStoreId(id);
            storeMiniDetailVO.setHasVenue(storeVenues.size() > 0);

            // 体验课列表
            List<BaseServiceItemVO> experienceList = getBaseExerciseList(now, id, ExerciseCategory.EXPERIENCE.getCode());
            storeMiniDetailVO.setTotalExperienceCount(experienceList.size());
            storeMiniDetailVO.setExperienceList(experienceList);

            // 优惠活动列表
            List<BaseServiceItemVO> couponList = getBaseExerciseList(now, id, ExerciseCategory.COUPON.getCode());
            storeMiniDetailVO.setCouponList(couponList);

            // 优惠劵
            List<CouponTemplate> couponTemplateList = this.couponTemplateService.getCouponListByStoreId(id);
            storeMiniDetailVO.setCouponTemplateList(couponTemplateList);

            // 获取店铺的按次活动列表
            storeMiniDetailVO.setCountExerciseList(exerciseService.getMiniStoreExerciseList(id, ExerciseCategory.COUNT.getCode()));

            // 查询会员卡列表
            List<BaseServiceItemVO> vipCourseList = getBaseExerciseList(now, id, ExerciseCategory.VIP_CARD.getCode());
            storeMiniDetailVO.setVipCardList(vipCourseList);

            // 获取课程/服务列表
            List<BaseServiceItemVO> courseList = getBaseExerciseList(now, id, ExerciseCategory.COURSE.getCode());
            storeMiniDetailVO.setProductList(courseList);
            storeMiniDetailVO.setTotalProductCount(courseList.size());

            // 获取拼单产品
            List<BaseServiceItemVO> joinList = getBaseExerciseList(now, id, ExerciseCategory.JOIN_EXERCISE.getCode());
            //查询拼单产品拼单规则中的最低价格
            if (CollectionUtils.isNotEmpty(joinList)){
                List<String> joinExerciseIds = joinList.stream().map(BaseServiceItemVO::getId).distinct().map(String::valueOf).collect(Collectors.toList());
                List<JoinExerciseRule> exerciseList = joinExerciseRuleService.getJoinExerciseLowestPrice(joinExerciseIds);
                if (CollectionUtils.isNotEmpty(exerciseList)){
                    Map<Long, BigDecimal> map =  exerciseList.stream().collect(Collectors.toMap(JoinExerciseRule::getExerciseId,JoinExerciseRule::getJoinPrice,(value1, value2) -> value1));
                    for (int i = 0; i < joinList.size(); i++) {
                        joinList.get(i).setLowerPrice(map.get(joinList.get(i).getId()));
                    }
                }
            }
            storeMiniDetailVO.setJoinExerciseList(joinList);

            // 获取活动列表
            QueryWrapper<Exercise> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Exercise::getStoreId, id)
                    .eq(Exercise::getStatus, DataStatus.NORMAL.getCode())
                    .eq(Exercise::getCategoryId, categoryService.getByCode(ExerciseCategory.EXERCISE.getCode()))
                    .gt(Exercise::getEndTime, now)
                    .orderByDesc(Exercise::getHot, Exercise::getCreateDate);
            List<Exercise> exerciseList = exerciseService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(exerciseList)) {
                storeMiniDetailVO.setTotalExerciseCount(exerciseList.size());
                storeMiniDetailVO.setExerciseList(exerciseList.stream().map(item -> {
                    ExerciseItemVO exerciseItemVO = new ExerciseItemVO();
                    BeanUtils.copyProperties(item, exerciseItemVO);
                    exerciseItemVO.setImage(FileUtil.getImageFullUrl(exerciseItemVO.getImage()));
                    exerciseItemVO.setDate(SportDateUtils.get2Day(item.getStartTime(), item.getEndTime()));
                    if (exerciseItemVO.getEnrollNumber() == null) {
                        exerciseItemVO.setEnrollNumber(0);
                    }
                    if(exerciseItemVO.getStoreProductCategoryId() != null) {
                        Category category = categoryService.getById(exerciseItemVO.getStoreProductCategoryId());
                        if (category != null) {
                            exerciseItemVO.setStoreProductCategoryName(category.getName());
                        }
                    }
                    if (StringUtils.isEmpty(exerciseItemVO.getStoreProductCategoryName())) {
                        exerciseItemVO.setStoreProductCategoryName("其他");
                    }
                    return exerciseItemVO;
                }).collect(Collectors.toList()));
            }

            // 获取店铺教练
            storeMiniDetailVO.setCoachList(storeCoachService.getByStoreId(id, true, 1, 1, 100).getRecords()
                    .stream().map(item -> {
                        StoreCoachVO storeCoachVO = new StoreCoachVO();
                        BeanUtils.copyProperties(item, storeCoachVO);
                        return storeCoachVO;
                    }).collect(Collectors.toList()));

            // 店铺图片
            storeMiniDetailVO.setImageList(storeImageService.getByStoreId(id).stream().map(StoreImage::getUrl).collect(Collectors.toList()));
            return storeMiniDetailVO;
        }
        return null;
    }

    @Override
    public List<Store> getMiniName() {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public List<CategoryBaseVO> getStoreCategory(Long storeId) {
        return getBaseMapper().getStoreCategory(storeId);
    }

    @Override
    public Store getByLocation(Long storeId, Double longitude, Double latitude) {
        if (longitude == null || latitude == null) {
            return null;
        }
        return getBaseMapper().getByIdLocation(storeId, longitude, latitude);
    }

    @Override
    public List<NearbyStoreVO> getNearbyStore(Double longitude, Double latitude) {
        List<NearbyStoreVO> storeList = getBaseMapper().getNearbyStore(longitude, latitude);
        if (CollectionUtils.isNotEmpty(storeList)) {
            storeList.forEach(item -> {
                item.setLogo(FileUtil.getImageFullUrl(item.getLogo()));
                item.setStoreLogo(FileUtil.getImageFullUrl(item.getStoreLogo()));
                item.getTopicList().forEach(topicList -> {
                    topicList.setCover(FileUtil.getImageFullUrl(topicList.getCover()));
                });
            });
        }
        return storeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrUpdate(StoreForm storeForm) {
        Store store = BeanUtil.copyProperties(storeForm, Store.class);
        if (store.getCategoryCode() == null) {
            store.setCategoryCode("store_course");
        }
        // 更新默认的光环logo
        if (storeForm.getId() == null) {
            if (StringUtils.isEmpty(storeForm.getLogo())) {
                throw new SportException("logo不能为空");
            }
            store.setTopicLogo(getDefaultTopicLogo(store.getLogo()));
        }
        if (storeForm.getLogo() != null) {
            if (store.getLogo().startsWith("https")) {
                store.setLogo(null);
            } else {
                updateTopicLogoByStore(store);
            }
        }

        if (saveOrUpdate(store)) {
            if (storeProductCategoryService.batchCreateOrUpdate(store.getId(), storeForm.getCategoryIds().split(","))) {
//                    return companyStoreUserService.insert(storeForm.getCompanyId(),store.getId(),storeForm.getUserId());
                return true;
            }
            // 第一次新建店铺 赠送按量付费次数
            if (storeForm.getId() == null) {
                chargingOptionService.initStoreChargingOption(store.getId(), store.getCompanyId());
            }
        }
        throw new SportException("操作失败");
    }

    @Override
    public boolean updateTopicLogo(Long id) {
        Store store = getById(id);
        if (store == null) {
            return false;
        }
        return updateTopicLogoByStore(store);
    }

    @Override
    public List<UserPhoneVO> getStoreBindUserPhone(Long companyId, Long storeId) {
        return companyStoreUserService.findPhoneAndUserId(companyId, null, storeId);
    }

    @Override
    public Boolean modifyStoreBindUserPhone(StoreBindUserPhoneForm storeBindUserPhoneForm) {
        CompanyStoreUser companyStoreUser = new CompanyStoreUser();
        if (Objects.isNull(storeBindUserPhoneForm.getId())) {
            if (Objects.isNull(storeBindUserPhoneForm.getStoreId())) {
                throw new SportException("店铺id为空");
            }
            if (Objects.isNull(storeBindUserPhoneForm.getStoreId())) {
                throw new SportException("公司id为空");
            }
        }
        BeanUtils.copyProperties(storeBindUserPhoneForm, companyStoreUser);
        User user = userService.getByUserPhone(storeBindUserPhoneForm.getPhone());
        companyStoreUser.setUserId(user.getId());
        return companyStoreUserService.saveOrUpdate(companyStoreUser);
    }

    @Override
    public Boolean deleteStoreBindUserPhone(Long id) {
        return companyStoreUserService.removeById(id);
    }

    @Override
    public List<UserPhoneVO> getStoreBindUserPhoneOne(Long id) {
        return companyStoreUserService.findPhoneAndUserId(null, id, null);
    }

    /**
     * 更新店铺光环图片
     * @param store
     * @return
     */
    private boolean updateTopicLogoByStore(Store store) {
        List<String> colorList = topicExerciseService.getStoreTopicColor(store.getId());
        String logo = FileUtil.getImageFullUrl(store.getLogo());
        if (StringUtils.isEmpty(logo)) {
            log.error("店铺:{}logo为空", store.getId());
            return false;
        }
        logo = logo + "?x-oss-process=image/resize,m_fixed,h_30,w_30";
        Store us = new Store();
        us.setId(store.getId());
        if (CollectionUtils.isNotEmpty(colorList)) {
            sportImagePath = sportImagePath + "/" + store.getId() + "/";
            FileUtil.checkPath(sportImagePath);
            String lastFile = logo;
            for (String rgb : colorList) {
                String out = sportImagePath + System.currentTimeMillis() + ".png";
                RingTopic.ringImage(lastFile, 4, out, rgb);
                lastFile = out;
            }
            String topicLogo = "store/logo/topic/" + System.currentTimeMillis() + ".png";
            try {
                if (OssUtil.uploadSingleImage(topicLogo, new FileInputStream(lastFile))) {
                    us.setTopicLogo(topicLogo);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileUtils.deleteQuietly(new File(sportImagePath));
        } else {
            us.setTopicLogo(getDefaultTopicLogo(logo));
        }
        OssUtil.deleteFile(store.getTopicLogo());
        return updateById(us);
    }

    private String getDefaultTopicLogo(String logo) {
        String topicLogo = "store/logo/topic/" + System.currentTimeMillis() + ".png";
        byte[] imageByte = RingTopic.getCircularImage(FileUtil.getImageFullUrl(logo));
        if (OssUtil.uploadSingleImage(topicLogo, imageByte)) {
            return topicLogo;
        }
        return null;
    }

    @Override
    public IPage<StoreVipCardVO> getStoreCardList(int page, int size, Long storeId) {
        Date now = new Date();
        // 查询会员卡列表
        QueryWrapper<Exercise> vipQueryWrapper = new QueryWrapper<>();
        vipQueryWrapper.lambda().eq(Exercise::getStoreId, storeId)
                .eq(Exercise::getStatus, DataStatus.NORMAL.getCode())
                .eq(Exercise::getCategoryId, categoryService.getByCode(ExerciseCategory.VIP_CARD.getCode()))
                .gt(Exercise::getEndTime, now)
                .orderByDesc(Exercise::getCreateDate);

        Page<Exercise> vipPage = exerciseService.page(new Page<>(page, size), vipQueryWrapper);
        IPage<StoreVipCardVO> iPage = new Page<>();
        BeanUtils.copyProperties(vipPage, iPage);
        List<Exercise> records = vipPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<ExerciseVipDTO> exerciseVipList = vipCardService.getExerciseVipCard(records.stream()
                    .mapToLong(Exercise::getId)
                    .boxed()
                    .collect(Collectors.toList()));
            Map<Long, ExerciseVipDTO> exerciseVipMap = null;
            if (CollectionUtils.isNotEmpty(exerciseVipList)) {
                exerciseVipMap = exerciseVipList.stream().collect(Collectors.toMap(
                        ExerciseVipDTO::getExerciseId,
                        Function.identity()));
            }
            List<StoreVipCardVO> storeVipCardVOList = new ArrayList<>(records.size());
            for (Exercise item : records) {
                StoreVipCardVO baseServiceItemVO = new StoreVipCardVO();
                BeanUtils.copyProperties(item, baseServiceItemVO);
                baseServiceItemVO.setImage(FileUtil.getImageFullUrl(baseServiceItemVO.getImage()));
                baseServiceItemVO.setLowerPrice(item.getPrice());
                baseServiceItemVO.setPrice(item.getOriginalPrice());

                if (exerciseVipMap != null && exerciseVipMap.get(item.getId()) != null) {
                    ExerciseVipDTO exerciseVipDTO = exerciseVipMap.get(item.getId());
                    if (exerciseVipDTO.getStoreName() != null) {
                        baseServiceItemVO.setStore(exerciseVipDTO.getStoreName() + "通用");
                    } else if (exerciseVipDTO.getCompanyName() != null) {
                        baseServiceItemVO.setStore(exerciseVipDTO.getCompanyName() + "所有门店通用");
                    }
                    if (exerciseVipDTO.getFixedTime() != null) {
                        baseServiceItemVO.setValidDay(DateFormatUtils.format(exerciseVipDTO.getFixedTime(), "yyyy-MM-dd"));
                    } else if (exerciseVipDTO.getReceiveDay() != null) {
                        baseServiceItemVO.setValidDay("购买后" + exerciseVipDTO.getReceiveDay() + "天有效");
                    } else {
                        baseServiceItemVO.setValidDay("永久有效");
                    }
                }
                storeVipCardVOList.add(baseServiceItemVO);
            }
            iPage.setRecords(storeVipCardVOList);
        }
        return iPage;
    }

    /**
     * 获取基础活动信息列表
     * @param now
     * @param storeId
     * @param categoryCode
     * @return
     */
    private List<BaseServiceItemVO> getBaseExerciseList(Date now, Long storeId, String categoryCode) {
        Long experienceClassId = categoryService.getByCode(categoryCode);
        QueryWrapper<Exercise> experienceWrapper = new QueryWrapper<>();
        experienceWrapper.lambda()
                .eq(Exercise::getStoreId, storeId)
                .eq(Exercise::getStatus, DataStatus.NORMAL.getCode())
                .eq(Exercise::getCategoryId, experienceClassId)
                .and(wrapper -> {
                    wrapper.gt(Exercise::getEndTime, now)
                            .or().gt(Exercise::getExerciseEndTime, now);
                })
                .and(wrapper -> {
                    wrapper.lt(Exercise::getStartTime, now)
                            .or().lt(Exercise::getExerciseStartTime, now);
                })
                .orderByDesc(Exercise::getHot, Exercise::getCreateDate);

        List<Exercise> experienceList = exerciseService.list(experienceWrapper);
        if (CollectionUtils.isNotEmpty(experienceList)) {
            Map<Long, Long> experienceCountMap;
            //产品分类 = 拼单、体验课
            if (ExerciseCategory.EXPERIENCE.getCode().equals(categoryCode) ||
                    ExerciseCategory.JOIN_EXERCISE.getCode().equals(categoryCode)) {
                // 获取体验课报名人数
                Collection<Long> exerciseIds = experienceList.stream().map(Exercise::getId).collect(Collectors.toSet());
                experienceCountMap = orderDetailService.getEnrollUserList(1, 10000, exerciseIds, experienceClassId)
                        .getRecords()
                        .stream()
                        .collect(Collectors.groupingBy(
                                UserEnterVO::getProductId,
                                Collectors.counting()
                        ));
            } else {
                experienceCountMap = null;
            }
            return experienceList.stream().map(item -> {
                BaseServiceItemVO baseServiceItemVO = new BaseServiceItemVO();
                BeanUtils.copyProperties(item, baseServiceItemVO);
                baseServiceItemVO.setImage(FileUtil.getImageFullUrl(baseServiceItemVO.getImage()));
                baseServiceItemVO.setLowerPrice(item.getPrice());

                if (now.before(item.getStartTime())) {
                    baseServiceItemVO.setDate(DateFormatUtils.format(item.getStartTime(), "yyyy-MM-dd"));
                } else {
                    baseServiceItemVO.setDate(DateFormatUtils.format(now, "yyyy-MM-dd"));
                }
                //拼单产品
                //显示活动实际开始结束时间
                if (ExerciseCategory.JOIN_EXERCISE.getCode().equals(categoryCode)){
                    baseServiceItemVO.setStartTime(item.getExerciseStartTime());
                    baseServiceItemVO.setEndTime(item.getExerciseEndTime());
                }
                // 设置购买人数
                if (experienceCountMap != null) {
                    baseServiceItemVO.setEnrollNumber(experienceCountMap.getOrDefault(item.getId(), NumberUtils.LONG_ZERO).intValue());
                }
                // 设置分类信息
                if(baseServiceItemVO.getStoreProductCategoryId() != null) {
                    Category category = categoryService.getById(baseServiceItemVO.getStoreProductCategoryId());
                    if (category != null) {
                        baseServiceItemVO.setStoreProductCategoryName(category.getName());
                    }
                }
                if (StringUtils.isEmpty(baseServiceItemVO.getStoreProductCategoryName())) {
                    baseServiceItemVO.setStoreProductCategoryName("其他");
                }
                return baseServiceItemVO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Long getCompanyIdByStoreId(Long storeId) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Store::getId, storeId)
                .eq(Store::getStatus, DataStatus.NORMAL.getCode());
        Store store = getOne(queryWrapper);
        if (store == null){
            return null;
        }
        return store.getCompanyId();
    }

    @Override
    public String getNameByStoreId(Long storeId) {
        Store store = getById(storeId);
        if (store != null){
            return store.getName();
        }
        return "";
    }

    @Override
    public List<Store> getByAPPId(String appId){

        // 根据 appId 查询店铺
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Store::getAppId, appId)
                .eq(Store::getStatus, DataStatus.NORMAL.getCode());

        // 检查列表是否为空
        if (list(queryWrapper) == null) {
            throw new SportException("店铺未查询到");
        }
        return list(queryWrapper);
    }
}
