package com.aioveu.sms.aioveu01Advert.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 广告实体对象
 * @Date  2026/1/12 10:35
 * @Param
 * @return
 **/

@Data
@TableName("sms_advert")
public class SmsAdvert extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @TableId(type= IdType.AUTO)
//    private Integer id;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 开始时间
     */
    @JsonFormat( pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    /**
     * 状态(1:开启；0:关闭)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 跳转URL
     */
    private  String redirectUrl;

    /**
     * 备注
     */
    private String remark;

}
