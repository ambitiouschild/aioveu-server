package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * @Description 消息选项
 * @Author  luyao
 * @Date: 2025-04-25 17:32:26
 */

@TableName("sport_message_option")
@Data
public class MessageOption extends IdEntity {

	/**
	 * 名称
	 */
  private String name;
	/**
	 * 编号
	 */
  private String code;
	/**
	 * 公司id
	 */
  private Long companyId;
	/**
	 * 店铺id
	 */
  private Long storeId;

}
