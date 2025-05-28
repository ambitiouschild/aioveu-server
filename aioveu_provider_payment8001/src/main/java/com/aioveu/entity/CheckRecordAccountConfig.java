package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @Description 订单核销平台账号配置表，配置核销平台的账户密码
 * @Author  luyao
 * @Date: 2025-01-08 12:04:12
 */

@TableName("sport_check_record_account_config")
@Data
public class CheckRecordAccountConfig extends StringIdEntity {
	/**
	 * 公司id
	 */
  private Long companyId;
	/**
	 * 门店id
	 */
  private Long storeId;
	/**
	 * 核销平台分类：来沪动 
	 */
  private String platformCode;
	/**
	 * 核销平台处理类
	 */
	private String platformHandler;
	/**
	 * 平台对应门店id 
	 */
  private String platformStoreId;
	/**
	 * 平台对应门店名称 
	 */
  private String platformStoreName;
	/**
	 * 平台登录账户
	 */
  private String platformUsername;
	/**
	 * 平台登录密码
	 */
  private String platformPassword;
	/**
	 * 确认新密码
	 */
	@TableField(exist = false)
	private String newPlatformPassword;
	/**
	 * 平台登录url
	 */
  private String platformUrl;

}
