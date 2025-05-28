package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Topic;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.TopicBaseVO;
import com.aioveu.vo.TopicItemVO;
import com.aioveu.vo.TopicStoreItemVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface TopicDao extends BaseMapper<Topic> {


    /**
     * 获取专题列表
     * @param page
     * @param categoryId
     * @param userId
     * @param productCategoryId
     * @return
     */
    IPage<TopicBaseVO> getTopicListByCategoryId(IPage<TopicBaseVO> page, @Param("categoryId") Long categoryId
            , String userId, Long productCategoryId);

    /**
     * 获取管理端主题列表
     * @param page
     * @param categoryId
     * @return
     */
    IPage<TopicItemVO> getManagerList(IPage<TopicItemVO> page, @Param("categoryId") Long categoryId);

    /**
     * 商家端获取专题列表
     * @param page
     * @param categoryId
     * @return
     */
    IPage<TopicStoreItemVO> getStoreCategoryList(IPage<TopicStoreItemVO> page, @Param("categoryId") Long categoryId,Long topicId);

//    /**
//     * 获取商家端主题商家数和推光人数
//     *
//     */
//    TopicStoreItemVO getStoreNumberAndPushNumberList(@Param("categoryId") Long categoryId ,Long topicId);
}
