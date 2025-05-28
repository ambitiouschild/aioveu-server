package com.aioveu.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * 表名：aioveu_store
 * 表注释：店铺表
*/
@Table(name = "aioveu_store")
public class Pay {
    /**
     * Id主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 公司Id
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 分类编号
     */
    @Column(name = "category_code")
    private String categoryCode;

    /**
     * 店铺logo
     */
    private String logo;

    /**
     * 主题logo
     */
    @Column(name = "topic_logo")
    private String topicLogo;

    /**
     * 店铺介绍
     */
    private String introduce;

    /**
     * 标签
     */
    private String tags;

    /**
     * 推荐顺序
     */
    @Column(name = "recommend_order")
    private Byte recommendOrder;

    /**
     * 场馆地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 店铺电话
     */
    private String tel;

    /**
     * 可提现余额
     */
    private BigDecimal balance;

    /**
     * 商圈Id
     */
    @Column(name = "business_area_id")
    private Integer businessAreaId;

    /**
     * 区域Id
     */
    @Column(name = "region_id")
    private Integer regionId;

    /**
     * 城市Id
     */
    @Column(name = "city_id")
    private Integer cityId;

    /**
     * 省份Id
     */
    @Column(name = "province_id")
    private Integer provinceId;

    /**
     * 微信小程序appId
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 店铺路径
     */
    private String path;

    /**
     * 状态 默认1 正常 0 删除
     */
    private Byte status;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 获取Id主键
     *
     * @return id - Id主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置Id主键
     *
     * @param id Id主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取公司Id
     *
     * @return companyId - 公司Id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 设置公司Id
     *
     * @param companyId 公司Id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取分类编号
     *
     * @return categoryCode - 分类编号
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * 设置分类编号
     *
     * @param categoryCode 分类编号
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * 获取店铺logo
     *
     * @return logo - 店铺logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置店铺logo
     *
     * @param logo 店铺logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 获取主题logo
     *
     * @return topicLogo - 主题logo
     */
    public String getTopicLogo() {
        return topicLogo;
    }

    /**
     * 设置主题logo
     *
     * @param topicLogo 主题logo
     */
    public void setTopicLogo(String topicLogo) {
        this.topicLogo = topicLogo;
    }

    /**
     * 获取店铺介绍
     *
     * @return introduce - 店铺介绍
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置店铺介绍
     *
     * @param introduce 店铺介绍
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /**
     * 获取标签
     *
     * @return tags - 标签
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置标签
     *
     * @param tags 标签
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 获取推荐顺序
     *
     * @return recommendOrder - 推荐顺序
     */
    public Byte getRecommendOrder() {
        return recommendOrder;
    }

    /**
     * 设置推荐顺序
     *
     * @param recommendOrder 推荐顺序
     */
    public void setRecommendOrder(Byte recommendOrder) {
        this.recommendOrder = recommendOrder;
    }

    /**
     * 获取场馆地址
     *
     * @return address - 场馆地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置场馆地址
     *
     * @param address 场馆地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取店铺电话
     *
     * @return tel - 店铺电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置店铺电话
     *
     * @param tel 店铺电话
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取可提现余额
     *
     * @return balance - 可提现余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置可提现余额
     *
     * @param balance 可提现余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取商圈Id
     *
     * @return businessAreaId - 商圈Id
     */
    public Integer getBusinessAreaId() {
        return businessAreaId;
    }

    /**
     * 设置商圈Id
     *
     * @param businessAreaId 商圈Id
     */
    public void setBusinessAreaId(Integer businessAreaId) {
        this.businessAreaId = businessAreaId;
    }

    /**
     * 获取区域Id
     *
     * @return regionId - 区域Id
     */
    public Integer getRegionId() {
        return regionId;
    }

    /**
     * 设置区域Id
     *
     * @param regionId 区域Id
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取城市Id
     *
     * @return cityId - 城市Id
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * 设置城市Id
     *
     * @param cityId 城市Id
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取省份Id
     *
     * @return provinceId - 省份Id
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * 设置省份Id
     *
     * @param provinceId 省份Id
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取微信小程序appId
     *
     * @return appId - 微信小程序appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置微信小程序appId
     *
     * @param appId 微信小程序appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取店铺路径
     *
     * @return path - 店铺路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置店铺路径
     *
     * @param path 店铺路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取状态 默认1 正常 0 删除
     *
     * @return status - 状态 默认1 正常 0 删除
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态 默认1 正常 0 删除
     *
     * @param status 状态 默认1 正常 0 删除
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return createDate - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取更新时间
     *
     * @return updateDate - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}