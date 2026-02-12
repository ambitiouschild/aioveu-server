package com.aioveu.pay.aioveuModule.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PaymentParamsVO
 * @Description TODO 支付参数VO - 用于前端调起支付  推荐使用方案3（通用设计）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:47
 * @Version 1.0
 **/

/*       TODO  问题：
            ❌ 违反 RESTful 设计：应该是结构化的 JSON，而不是查询字符串
            ❌ 前端需要额外解析：增加前端复杂度
            ❌ 可读性差：调试时不易阅读
            ❌ 类型不安全：所有值都是字符串，需要手动转换
            ❌ 扩展性差：新增参数需要修改字符串拼接逻辑*/

/*    TODO 立即修改为结构化JSON格式，因为：
            ✅ 符合RESTful规范
            ✅ 前端处理简单
            ✅ 类型安全
            ✅ 易于扩展
            ✅ 调试方便
            ✅ 文档清晰
             不要使用查询字符串格式，除非有特殊原因（如需要直接拼接到URL中）。
             即使H5支付需要URL，也应该单独返回h5Url字段，而不是让前端拼接。*/

/*   TODO 添加：
        1.响应码和消息
        2.Map类型的payParams
        3.快捷访问方法
        4.这样设计的优势：
        ✅ 结构清晰：基础信息+支付参数分离
        ✅ 可扩展：支持任意支付参数
        ✅ 类型安全：通过Map管理
        ✅ 前后端一致：统一数据结构
        ✅ 易于维护：新增支付渠道只需扩展Map*/


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "支付参数响应对象")
public class PaymentParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基础信息 ====================
    @Schema(description = "支付单号(系统内部)", example = "P202502110001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentNo;

    @Schema(description = "商户订单号", example = "202502110001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "支付金额(元)", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "商品标题", example = "iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subject;

    @Schema(description = "商品描述", example = "苹果手机 iPhone 15 Pro 256GB")
    private String body;

    // ==================== 支付信息 ====================

    @Schema(description = "支付渠道: WECHAT-微信 ALIPAY-支付宝")
    private String channel;


    @Schema(description = "支付类型: JSAPI-小程序/公众号 APP-APP支付 NATIVE-扫码支付 H5-H5支付")
    private String payType;

    @Schema(description = "预支付ID(微信)", example = "wx20250212170724abc123")
    private String prepayId;

    // ==================== 支付参数映射 ====================
    @Schema(description = "支付参数映射表")
    private Map<String, Object> payParams;

    // ==================== 时间信息 ====================
    @Schema(description = "创建时间戳(毫秒)", example = "1739257200000")
    private Long createTime;

    @Schema(description = "过期时间戳(毫秒)", example = "1739259000000")
    private Long expireTime;

    // ==================== 快捷方法 ====================
    public Object getPayParam(String key) {
        return payParams != null ? payParams.get(key) : null;
    }

    public String getPayParamAsString(String key) {
        Object value = getPayParam(key);
        return value != null ? value.toString() : null;
    }

    public void addPayParam(String key, Object value) {
        if (payParams == null) {
            payParams = new HashMap<>();
        }
        payParams.put(key, value);
    }

    //Lombok的 @Builder为这些getter方法自动生成了对应的setter，即使没有对应的字段！
    // 微信支付快捷方法
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getAppId() {
//        return getPayParamAsString("appId");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getTimeStamp() {
//        return getPayParamAsString("timeStamp");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getNonceStr() {
//        return getPayParamAsString("nonceStr");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getPackageStr() {
//        return getPayParamAsString("package");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getSignType() {
//        return getPayParamAsString("signType");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getPaySign() {
//        return getPayParamAsString("paySign");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getH5Url() {
//        return getPayParamAsString("h5Url");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    public String getQrCodeUrl() {
//        return getPayParamAsString("qrCodeUrl");
//    }
//
//    @JsonIgnore  // 关键：不让Jackson序列化这个方法
//    // 支付宝快捷方法
//    public String getOrderInfo() {
//        return getPayParamAsString("orderInfo");
//    }


}
