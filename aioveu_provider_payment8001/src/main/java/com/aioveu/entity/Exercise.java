package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise")
@Data
public class Exercise extends IdNameEntity {

    /**
     * 状态 默认0 后台人员下架 1 正常 2 后台下架 3 商户下架 10 隐藏 15 失效
     */

    private Long storeId;

    /**
     * 活动开始报名时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 活动实际开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date exerciseStartTime;

    /**
     * 活动实际结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date exerciseEndTime;

    private String image;

    private String video;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private String description;

    private String process;

    private String suitablePeople;

    private String requirement;

    private String qa;

    private String careful;

    private String remark;

    /**
     * 分类id
     */
    private Long categoryId;

    private Integer limitNumber;

    /**
     * 公众号通知openId
     */
    private String serviceOpenId;

    /**
     * 单个用户的限制人数 0 不限制
     */
    private Integer singleNumber;

    /**
     * 优惠券领取类型 默认0 1支付成功领取 2核销领取
     */
    private Integer couponReceiveType;

    /**
     * 产品标签
     */
    private String tags;

    /**
     * 是否需要地址
     */
    private Boolean needAddress;

    private Boolean hot;

    /**
     * 报名人数
     */
    private Integer enrollNumber;

    /**
     * 是否需要定位
     */
    private Boolean needLocation;

    /**
     * 赠送余额
     */
    private BigDecimal sendBalance;

    /**
     * 店铺产品分类
     */
    private Long storeProductCategoryId;

    private Boolean top;

    /**
     * 协议模板地址
     */
    private String agreementTemplate;
    
}
