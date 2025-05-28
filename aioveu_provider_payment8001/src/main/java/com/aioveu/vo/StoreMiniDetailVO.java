package com.aioveu.vo;

import com.aioveu.entity.CouponTemplate;
import lombok.Data;

import java.util.List;

/**
 * @description 店铺详情
 * @author: 雒世松
 * @date: 2025/11/23 17:06
 */
@Data
public class StoreMiniDetailVO extends IdNameVO {

    private String address;

    private String tel;

    private String introduce;

    private String logo;

    private Double longitude;

    private Double latitude;

    private String tags;

    private boolean hasVenue;

    private List<CouponTemplate> couponTemplateList;

    /**
     * 课程服务
     */
    private List<BaseServiceItemVO> productList;

    /**
     * 课程服务总数
     */
    private Integer totalProductCount;

    /**
     * 活动列表
     */
    private List<ExerciseItemVO> exerciseList;

    /**
     * 活动总数
     */
    private Integer totalExerciseCount;

    /**
     * 体验课列表
     */
    private List<BaseServiceItemVO> experienceList;

    /**
     * 按次活动列表
     */
    private List<BaseServiceItemVO> countExerciseList;

    /**
     * 体验课总数
     */
    private Integer totalExperienceCount;


    /**
     * 会员卡列表
     */
    private List<BaseServiceItemVO> vipCardList;

    /**
     * 课程服务
     */
    private List<StoreCoachVO> coachList;

    private List<String> imageList;

    /**
     * 优惠活动列表
     */
    private List<BaseServiceItemVO> couponList;

    /**
     * 拼单产品
     */
    private List<BaseServiceItemVO> joinExerciseList;

}
