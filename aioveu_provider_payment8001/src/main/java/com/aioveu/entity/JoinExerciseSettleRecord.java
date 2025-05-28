package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description 拼单产品返现结算记录表，一个拼单产品只记录一条
 * @Author  luyao
 * @Date: 2024-10-08 09:24:04
 */

@TableName("sport_join_exercise_settle_record")
@Data
public class JoinExerciseSettleRecord extends IdEntity {

	/**
	 * 入队列的消息Id
	 */
  private String messageId;
	/**
	 * 拼单产品Id
	 */
  private String exerciseId;
	/**
	 * 分类id
	 */
  private Long categoryId;
	/**
	 * 拼单产品名称
	 */
  private String exerciseName;
	/**
	 * 店铺id
	 */
  private Long storeId;
	/**
	 * 计划结算时间，即拼单结束时间
	 */
  private Date exerciseEndDate;
	/**
	 * 实际结算总人数
	 */
  private Integer settleNumber;
	/**
	 * 实际结算返还总金额
	 */
  private BigDecimal settleAmount;

	/**
	 * 总结算人数
	 */
  private Integer totalNumber;

	/**
	 * 总结算金额
	 */
  private BigDecimal totalAmount;

	/**
	 * 单人结算金额
	 */
  private BigDecimal singleAmount;


}
