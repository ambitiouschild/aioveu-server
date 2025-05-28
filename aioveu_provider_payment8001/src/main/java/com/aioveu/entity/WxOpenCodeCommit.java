package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @Description 微信开放平台授权小程序上传代码记录
 * @Author  luyao
 * @Date: 2025-02-10 15:26:38
 */

@TableName("sport_wx_open_code_commit")
@Data
public class WxOpenCodeCommit extends IdEntity {

	/**
	 * 授权appid
	 */
  private String appId;
	/**
	 * 代码模版编号
	 */
  private Long templateId;
	/**
	 * 上传版本号
	 */
  private String version;
	/**
	 * 上传描述
	 */
	@TableField("`describe`")
  private String describe;
	/**
	 * 上传ext信息
	 */
  	private String extJson;

  	private Integer commitStatus;
	/**
	 * 上传人用户id
	 */
  	private String createUserId;
	/**
	 * 上传人账号
	 */
  	private String createUserName;

}
