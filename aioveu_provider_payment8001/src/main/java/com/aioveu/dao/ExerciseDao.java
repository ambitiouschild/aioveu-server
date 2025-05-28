package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Exercise;
import com.aioveu.entity.Store;
import com.aioveu.vo.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface ExerciseDao extends BaseMapper<Exercise> {


    /**
     * 查询所有场馆
     * @param page
     * @param params
     * @return
     */
    IPage<BaseServiceItemVO> getAll(IPage<BaseServiceItemVO> page, @Param("params") Map<String, Object> params);

    /**
     * 查询健身馆详情
     * @param id
     * @param preview
     * @return
     */
    ExerciseVO getDetail(Long id, Boolean preview);

    /**
     * 列表
     * @param page
     * @param storeId
     * @param categoryId
     * @param storeName
     * @param status
     * @return
     */
    IPage<ExerciseManagerItemVO> getManagerAll(IPage<ExerciseManagerItemVO> page,
                                               Long storeId, Long categoryId, String storeName, Integer status);

    /**
     * 获取店铺推荐活动
     * @param storeIds
     * @return
     */
    List<ProductSimpleVO> getBatchHotExerciseByStoreIds(List<Long> storeIds);

    /**
     * 获取应用的appId
     * @param id
     * @return
     */
    String getAppIdByExerciseId(Long id);

    /**
     * 地推活动列表
     * @param page
     * @param storeId
     * @param categoryId
     * @return
     */
    IPage<PushExerciseItemVO> getPushList(IPage<PushExerciseItemVO> page, Long storeId, Long categoryId);

    /**
     * 获取体验课
     * @param categoryId
     * @return
     */
    List<IdNameVO> getExperience(Long categoryId);

    /**
     * 根据活动获取公司id
     * @param exerciseId
     * @return
     */
    Store getCompanyIdById(Long exerciseId);

    /**
     * 查询按次活动详情
     * @param id
     * @return
     */
    ExerciseCountDetailVO getCountDetail(Long id);

}
