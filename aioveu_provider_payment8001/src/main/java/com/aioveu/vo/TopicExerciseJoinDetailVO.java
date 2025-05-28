package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicExerciseJoinDetailVO {

    private Long topicId;

    private String cover;

    /**
     * 商家数
     */
    private int storeNumber;

    /**
     * 推广人数
     */
    private int pushNumber;

    /**
     * 已参加的活动列表
     */
    private List<IdNameVO> signList;

    /**
     * 商家主题名字
     */
    private String topicName;

    /**
     * 主题子分类
     */
    private List<CategoryBaseVO> categoryName;

    private Date endTime;
}
