package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
/**
 * @Description 微信开放平台授权小程序提交审核记录，每个appid、user_id只有一条记录
 * @Author  luyao
 * @Date: 2025-02-10 15:26:54
 */

@TableName("sport_wx_open_submit_audit")
@Data
public class WxOpenSubmitAudit extends IdEntity {

	/**
	 * 授权appid
	 */
  private String appId;
	/**
	 * 上传代码编号
	 */
	private Long codeCommitId;
	/**
	 * 提交审核版本描述
	 */
	@TableField("`describe`")
  private String describe;
	/**
	 * 预览信息（小程序页面截图和操作录屏）
	 */
  private String previewInfo;
	/**
	 * 用于声明是否不使用“代码中检测出但是未配置的隐私相关接口，0不使用，1使用
	 */
  private Integer privacyApiNotUse;
	/**
	 * 订单中心path
	 */
  private String orderPath;
	/**
	 * 审核状态
	 * 0 审核成功
	 * 1 审核被拒绝
	 * 2 审核中
	 * 3 已撤回
	 * 4 审核延后
	 * 8 未知
	 */
  private Integer auditStatus;
	/**
	 * 审核失败原因
	 */
  private String failReason;
	/**
	 * 审核结果时间
	 */
  private Date resultDate;
	/**
	 * 反馈内容，不超过200字
	 */
  private String feedbackInfo;
	/**
	 * 审核编号
	 */
  private Long auditId;

}
