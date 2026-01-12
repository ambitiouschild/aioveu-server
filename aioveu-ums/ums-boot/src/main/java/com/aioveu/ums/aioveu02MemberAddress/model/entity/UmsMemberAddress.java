package com.aioveu.ums.aioveu02MemberAddress.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: TODO 会员收货地址实体对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:58
 * @param
 * @return:
 **/

@Data
@TableName("ums_member_address")
public class UmsMemberAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 收货人姓名
     */
    private String consigneeName;
    /**
     * 收货人联系方式
     */
    private String consigneeMobile;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区/县
     */
    private String district;
    /**
     * 街道
     */
    private String street;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 是否默认地址(0=否,1=是)
     */
    private Integer defaulted;
    /**
     * 地址标签(家,公司,学校等)
     */
    private String addressTag;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 状态(0=已删除,1=正常)
     */
    private Integer status;

}
