package com.aioveu.vo;

import com.aioveu.entity.StringIdEntity;
import lombok.Data;

/**
 * @Description 订单核销平台账号配置表，配置核销平台的账户密码
 * @Author  luyao
 * @Date: 2024-11-27 10:38:50
 */

@Data
public class CheckRecordAccountConfigVO extends StringIdEntity {

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
	 * 平台登录url
	 */
  private String platformUrl;

}
