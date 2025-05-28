package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 拼单规则配置表
 * @Author  luyao
 * @Date: 2024-10-08 09:11:06
 */

@TableName("sport_join_exercise_rule")
@Data
public class JoinExerciseRule extends IdEntity {

	/**
	 * 拼单产品Id
	 */
  private Long exerciseId;
	/**
	 * 店铺id
	 */
  private Long storeId;
	/**
	 * 拼单人数
	 */
  private Long joinNumber;
	/**
	 * 拼单价格
	 */
  private BigDecimal joinPrice;


}
