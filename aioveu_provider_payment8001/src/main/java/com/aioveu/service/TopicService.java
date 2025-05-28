package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Topic;
import com.aioveu.form.TopicForm;
import com.aioveu.vo.*;

import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface TopicService extends IService<Topic> {


    /**
     * 获取专题列表
     * @param page
     * @param size
     * @param categoryId
     * @param userId
     * @return
     */
    IPage<TopicBaseVO> getTopicListByCategoryId(int page, int size, Long categoryId, String userId);

    /**
     * 创建主题
     * @param form
     * @return
     */
    boolean create(TopicForm form);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 更新主题
     * @param form
     * @return
     */
    boolean updateTopic(TopicForm form);

    /**
     * 查找详情
     * @param id
     * @return
     */
    TopicForm getDetailById(Long id);

    /**
     * 管理端的主题列表
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    IPage<TopicItemVO> getList(int page, int size, Long categoryId);

    /**
     * 地推人员推荐的推广主题
     * @param username
     * @return
     */
    TopicBaseVO recommend(String username);

    /**
     * 小程序端主题详情
     * @param id
     * @param pushUserKey 地推人分享的key
     * @param userId
     * @return
     */
    TopicDetailVO miniAppDetail(Long id, String pushUserKey, String userId);

    /**
     * 商家端专题列表
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    IPage<TopicStoreItemVO> getStoreCategoryList(int page, int size, Long categoryId);

    /**
     * 获得存储数量和推广数量
     *
     * @param categoryId 类别id
     * @param topicId    主题id
     * @return {@link IPage}<{@link TopicStoreItemVO}>
     */
    TopicStoreItemVO getStoreNumberAndPushNumberList(Long categoryId,Long topicId);

    /**
     * 上架下架
     * @return
     */
    boolean upAndLow(TopicForm form);
}
