package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @Description 微信开放平台授权小程序线上版本
 * @Author  luyao
 * @Date: 2025-02-10 15:26:46
 */

@TableName("sport_wx_open_online_version")
@Data
public class WxOpenOnlineVersion extends IdEntity {

	/**
	 * 授权appid
	 */
  private String appId;
	/**
	 * 上传代码编号
	 */
  private Long codeCommitId;

	/**
	 * 审批id
	 */
	private Long submitAuditId;

}
