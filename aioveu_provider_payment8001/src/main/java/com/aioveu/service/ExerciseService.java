package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Exercise;
import com.aioveu.entity.Store;
import com.aioveu.form.ExerciseCustomForm;
import com.aioveu.form.ExerciseForm;
import com.aioveu.form.PushExerciseForm;
import com.aioveu.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ExerciseService extends IService<Exercise> {

    /**
     * 获取列表
     * @param page
     * @param size
     * @param storeId
     * @param storeName
     * @param categoryId
     * @param status
     * @return
     */
    IPage<ExerciseManagerItemVO> getManagerAll(int page, int size, Long storeId, String storeName, Long categoryId, Integer status);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);

    /**
     * 管理平台活动详情
     * @param id
     * @return
     */
    ExerciseManagerDetailVO managerDetail(Long id);

    /**
     * 查询健身馆详情
     * @param id
     * @param preview
     * @return
     */
    ExerciseVO getDetail(Long id, Boolean preview);

    /**
     * 查询按次活动详情
     * @param id
     * @return
     */
    ExerciseCountDetailVO getCountDetail(Long id);

    /**
     * 查询所有的活动列表
     * @param param
     * @return
     * @throws Exception
     */
    IPage<BaseServiceItemVO> getAll(Map<String, Object> param) throws Exception;

    /**
     * 根据id复制活动
     * @param id
     * @param name
     * @param storeId
     * @param originalPrice
     * @param price
     */
    Long copy(Long id, String name, Long storeId, Double originalPrice, Double price);

    /**
     * 获取店铺的热门活动
     * @param storeIds
     * @return
     */
    List<ProductSimpleVO> getBatchHotExerciseByStoreIds(List<Long> storeIds);

    /**
     * 活动删除
     * @param id
     * @return
     */
    boolean exerciseDelete(Long id);

    /**
     * 创建或者更新活动
     * @param exercise
     * @return
     */
    boolean createOrUpdate(ExerciseForm exercise);

    /**
     * 通过id复制活动
     * @param id
     * @return
     */
    Long copyById(Long id);

    /**
     * 获取应用的appId
     * @param id
     * @return
     */
    String getAppIdByExerciseId(Long id);

    /**
     * 灵活课包活动创建
     * @param form
     * @return
     */
    boolean exerciseCustom(ExerciseCustomForm form);

    /**
     * 获取灵活课包列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<ExerciseManagerItemVO> getCustomList(int page, int size, Long storeId);

    /**
     * 设置为失效
     * @param exerciseId
     */
    void status2Invalid(Long exerciseId);

    /**
     * 创建或者更新地推活动
     * @param form
     * @return
     */
    boolean createOrUpdatePush(PushExerciseForm form);

    /**
     * 获取地推活动列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<PushExerciseItemVO> getPushList(int page, int size, Long storeId);

    /**
     * 获取地推活动详情
     * @param id
     * @param topicId
     * @return
     */
    PushExerciseForm getPush(Long id, Long topicId);

    /**
     * 根据Id查询主题对象
     * @param id
     * @return
     */
    Exercise selExerciseById(long id);

    /**
     * 批量取消活动的置顶
     * @param exerciseIds
     * @return
     */
    boolean cancelTop(List<Long> exerciseIds);

    /**
     * 获取VIP价格
     * @param id
     * @param username
     * @return
     */
    BigDecimal getVipPrice(Long id, String username);

    /**
     * 获取体验课
     * @return
     */
    List<IdNameVO> getExperience();

    /**
     * 根据活动获取公司id
     * @param exerciseId
     * @return
     */
    Store getCompanyIdById(Long exerciseId);

    /**
     * 获取小程序 店铺详情活动列表
     * @param storeId
     * @param categoryCode
     * @return
     */
    List<BaseServiceItemVO> getMiniStoreExerciseList(Long storeId, String categoryCode);

}
