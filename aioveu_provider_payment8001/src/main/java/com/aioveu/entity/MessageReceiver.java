package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description 消息接收表
 * @Author  luyao
 * @Date: 2025-04-29 11:26:32
 */

@TableName("sport_message_receiver")
@Data
public class MessageReceiver extends IdEntity {

	/**
	 * 消息Id
	 */
  private Long msgConfigId;
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

	/**
	 * 创建人
	 */
	private String createUserId;

	/**
	 * 修改人
	 */
	private String updateUserId;

}
