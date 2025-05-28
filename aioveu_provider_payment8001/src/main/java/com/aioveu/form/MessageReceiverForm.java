package com.aioveu.form;

import lombok.Data;

/**
 * @Description 消息接收表
 * @Author  luyao
 * @Date: 2025-04-29 11:26:32
 */

@Data
public class MessageReceiverForm {

	/**
	 * 消息Id
	 */
  private Long msgConfigId;

	/**
	 * 消息编号
	 */
	private String msgCode;

	/**
	 * 通知类型
	 */
	private String noticeCode;
	/**
	 * 公司Id
	 */
  private Long companyId;
	/**
	 * 店铺Id
	 */
  private Long storeId;
	/**
	 * 昵称
	 */
  private String name;
	/**
	 * 用户id
	 */
  private String userId;
	/**
	 * 电话
	 */
  private String phone;

	/**
	 * 微信openId
	 */
	private String openId;
	/**
	 * 邮箱
	 */
	private String mail;


}
