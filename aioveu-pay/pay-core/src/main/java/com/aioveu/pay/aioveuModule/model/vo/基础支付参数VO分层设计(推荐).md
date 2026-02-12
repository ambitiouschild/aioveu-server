方案3：枚举+通用设计（最灵活）

## 推荐使用方案3（通用设计）

**修改你的 PaymentParamsVO**：

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "支付参数响应对象")
public class PaymentParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基础信息 ====================
    @Schema(description = "响应码", example = "00000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    
    @Schema(description = "响应消息", example = "成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private String msg;
    
    @Schema(description = "支付单号", example = "PAY20250212170724515329", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentNo;
    
    @Schema(description = "商户订单号", example = "202502110001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;
    
    @Schema(description = "支付金额(分)", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long amount;
    
    @Schema(description = "商品标题", example = "iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subject;
    
    @Schema(description = "商品描述", example = "苹果手机 iPhone 15 Pro 256GB")
    private String body;
    
    // ==================== 支付信息 ====================
    @Schema(description = "支付类型: JSAPI-小程序/公众号 APP-APP支付 NATIVE-扫码支付 H5-H5支付")
    private String payType;
    
    @Schema(description = "支付渠道: WECHAT-微信 ALIPAY-支付宝")
    private String channel;
    
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
    
    // 微信支付快捷方法
    public String getAppId() {
        return getPayParamAsString("appId");
    }
    
    public String getTimeStamp() {
        return getPayParamAsString("timeStamp");
    }
    
    public String getNonceStr() {
        return getPayParamAsString("nonceStr");
    }
    
    public String getPackageStr() {
        return getPayParamAsString("package");
    }
    
    public String getSignType() {
        return getPayParamAsString("signType");
    }
    
    public String getPaySign() {
        return getPayParamAsString("paySign");
    }
    
    public String getH5Url() {
        return getPayParamAsString("h5Url");
    }
    
    public String getQrCodeUrl() {
        return getPayParamAsString("qrCodeUrl");
    }
    
    // 支付宝快捷方法
    public String getOrderInfo() {
        return getPayParamAsString("orderInfo");
    }
}
```

## Service层使用示例

```
@Service
@Slf4j
public class WeChatPayServiceImpl implements WeChatPayService {
    
    @Override
    public PaymentParamsVO jsapiPay(PaymentRequestDTO request) {
        try {
            // 1. 调用微信支付
            PrepayResponse response = jsapiService.prepay(prepayRequest);
            
            // 2. 生成支付参数
            Map<String, String> rawParams = generateJsapiPayParams(response.getPrepayId());
            
            // 3. 构建返回结果
            PaymentParamsVO result = PaymentParamsVO.builder()
                    .code("00000")
                    .msg("支付参数生成成功")
                    .paymentNo(request.getOrderNo())
                    .orderNo(request.getOrderNo())
                    .amount(request.getTotalAmount().multiply(new BigDecimal("100")).longValue())
                    .subject(request.getSubject())
                    .body(request.getBody())
                    .payType("JSAPI")
                    .channel("WECHAT")
                    .prepayId(response.getPrepayId())
                    .createTime(System.currentTimeMillis())
                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();
            
            // 4. 添加支付参数
            Map<String, Object> payParams = new HashMap<>();
            payParams.put("appId", rawParams.get("appId"));
            payParams.put("timeStamp", rawParams.get("timeStamp"));
            payParams.put("nonceStr", rawParams.get("nonceStr"));
            payParams.put("package", rawParams.get("package"));
            payParams.put("signType", rawParams.get("signType"));
            payParams.put("paySign", rawParams.get("paySign"));
            
            result.setPayParams(payParams);
            
            return result;
            
        } catch (Exception e) {
            log.error("JSAPI支付失败", e);
            
            return PaymentParamsVO.builder()
                    .code("50000")
                    .msg("支付参数生成失败: " + e.getMessage())
                    .paymentNo(request.getOrderNo())
                    .orderNo(request.getOrderNo())
                    .amount(request.getTotalAmount().multiply(new BigDecimal("100")).longValue())
                    .subject(request.getSubject())
                    .build();
        }
    }
}
```

## 前端使用示例

```
// 调用支付
const handlePay = async () => {
  const response = await pay({
    orderSn: orderSn.value,
    paymentMethod: 'JSAPI',
    paymentAmount: amount.value
  })
  
  if (response.code === '00000') {
    const data = response.data
    
    // 获取支付参数
    const payParams = data.payParams
    
    uni.requestPayment({
      provider: 'wxpay',
      timeStamp: payParams.timeStamp,
      nonceStr: payParams.nonceStr,
      package: payParams.package,
      signType: payParams.signType || 'RSA',
      paySign: payParams.paySign,
      
      success: (res) => {
        console.log('支付成功')
      },
      
      fail: (err) => {
        console.error('支付失败:', err)
      }
    })
  }
}
```

## 总结修改建议

**从你的VO中删除**：

1. 所有重复的支付参数字段（appId, timeStamp等）
2. 所有渠道特定的字段（aliAppId等）
3. 所有状态字段（payStatus, success等）
4. 所有业务字段（bizType, bizId等）

**保留**：

1. 基础信息（paymentNo, orderNo, amount等）
2. 支付信息（payType, channel, prepayId等）
3. 统一的payParams映射

**添加**：

1. 响应码和消息
2. Map类型的payParams
3. 快捷访问方法

这样设计的优势：

1. ✅ **结构清晰**：基础信息+支付参数分离
2. ✅ **可扩展**：支持任意支付参数
3. ✅ **类型安全**：通过Map管理
4. ✅ **前后端一致**：统一数据结构
5. ✅ **易于维护**：新增支付渠道只需扩展Map