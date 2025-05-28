package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.ResultEnum;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.ProductOrderDao;
import com.aioveu.entity.Company;
import com.aioveu.entity.ProductOrder;
import com.aioveu.entity.RoleUser;
import com.aioveu.entity.User;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PayCategoryEnum;
import com.aioveu.enums.PayType;
import com.aioveu.exception.SportException;
import com.aioveu.feign.vo.WeChatPayParamVO;
import com.aioveu.form.ProductOrderForm;
import com.aioveu.service.*;
import com.aioveu.utils.*;
import com.aioveu.utils.word.WordUtil;
import com.aioveu.vo.ProductOrderManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderDao, ProductOrder> implements ProductOrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value(value = "${sport.agreement.path}")
    private String sportAgreementPath;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private EntSealGenerateService entSealGenerateService;

    @Override
    public IPage<ProductOrderManagerVO> getManagerAll(int page, int size, String id, String name, Long categoryId, Integer status) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        String userId = null;
        if (!OauthUtils.isSuperAdmin()) {
            userId = currentUser.getUserId();
        }
        return this.baseMapper.getManagerAll(new Page<>(page, size), id, name, categoryId, status, userId);
    }

    @Override
    public ProductOrderManagerVO managerDetail(String id) {
        ProductOrder order = getById(id);
        if (order != null) {
            ProductOrderManagerVO vo = new ProductOrderManagerVO();
            BeanUtils.copyProperties(order, vo);
            return vo;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductOrderManagerVO createProductOrder(ProductOrderForm form, String userId) {
        if(form.getAmount() == null || form.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SportException("价格不能为空并且必须大于0");
        }
        log.info("签单下单:{}", JacksonUtils.obj2Json(form));
        ProductOrder order = new ProductOrder();

        String orderId = IdUtils.getStrNumberId("PO");
        order.setId(orderId);
        order.setName(form.getName());
        //企业收账订单
        order.setCategoryId(form.getCategoryId());
        order.setUserId(userId);
        order.setRemark(form.getRemark());
        order.setCustomerName(form.getCustomerName());
        order.setCustomerTel(form.getCustomerTel());
        order.setCompanyName(form.getCompanyName());
        order.setStartDay(form.getStartDay());
        order.setCustomerEmail(form.getCustomerEmail());
        order.setAdminPhone(form.getAdminPhone());
        order.setCustomerAddress(form.getCustomerAddress());
        order.setAmount(form.getAmount());
        order.setCouponAmount(BigDecimal.ZERO);
        order.setConsumeAmount(order.getAmount());
        order.setActualAmount(order.getConsumeAmount());
        order.setCanRefundAmount(order.getActualAmount());
        order.setTerms(form.getTerms());
        order.setSoftPrice(form.getSoftPrice());

        Company company = companyService.getCompanyByMiniAppId(SportConstant.QU_SHU_MINI_APP);
        if (company == null || StringUtils.isEmpty(company.getMiniAppId()) || StringUtils.isEmpty(company.getMiniAppPayId())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常!");
        }
        order.setAppId(company.getMiniAppPayId());
        if (!save(order)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        WeChatPayParamVO weChatPayParamVO = getPayCodeUrl(order, company);
        order.setPrepayId(weChatPayParamVO.getPrepayId());
        order.setPayType(company.getPayType());
        order.setNonceStr(weChatPayParamVO.getNonceStr());

        ProductOrder po = new ProductOrder();
        po.setId(orderId);
        po.setPrepayId(weChatPayParamVO.getPrepayId());
        po.setPayType(company.getPayType());
        po.setNonceStr(weChatPayParamVO.getNonceStr());
        updateById(po);

        ProductOrderManagerVO vo = new ProductOrderManagerVO();
        BeanUtils.copyProperties(order, vo);
        vo.setCodeUrl(weChatPayParamVO.getCodeUrl());

        User user = userService.quickRegisterByPhone(order.getAdminPhone(), order.getAdminPhone());
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(user.getId());
        roleUser.setRoleCode("TO_SIGN");
        roleUser.setCompanyId(0L);
        roleUser.setStoreId(0L);
        roleUserService.saveUserRole(roleUser);

        return vo;
    }

    /**
     * 获取支付二维码
     * @param order
     * @param company
     * @return
     */
    private WeChatPayParamVO getPayCodeUrl(ProductOrder order, Company company) {
        String key = "QIAN_DAN:" + order.getId();
        String payCodeUrl = stringRedisTemplate.opsForValue().get(key);
        WeChatPayParamVO weChatPayParamVO;
        if (StringUtils.isNotEmpty(payCodeUrl)) {
            weChatPayParamVO = new WeChatPayParamVO();
            weChatPayParamVO.setCodeUrl(payCodeUrl);
        } else {
            //TODO 2025 签单暂时使用原始微信
            company.setPayType(PayType.Wechat.getCode());
            String noticeUrl;
            String tradeType;
            String payAppId = "";
            if (PayType.HF.getCode().equals(company.getPayType())) {
                noticeUrl = "hf-pay-callback";
                tradeType = "T_NATIVE";
                payAppId = company.getHuiFuId();
            } else {
                noticeUrl = "product-wechat-pay";
                tradeType = "NATIVE";
                payAppId = company.getMiniAppPayId();
            }

            ProductOrder update = new ProductOrder();
            update.setId(order.getId());
            update.setPayOrderId(IdUtils.getStrNumberId("PP"));
            if (updateById(update)) {
                weChatPayParamVO = orderService.getPayParam(null, null, company.getMiniAppId(), payAppId,
                        update.getPayOrderId(), order.getName(), order.getActualAmount(), noticeUrl, company.getPayType(), tradeType, PayCategoryEnum.PRODUCT_ORDER.getCode(), 0);
                // 付款二维码有效期 2小时
                stringRedisTemplate.opsForValue().set(key, weChatPayParamVO.getCodeUrl(), 2, TimeUnit.HOURS);
                String expirationTime = DateFormatUtils.format(DateUtils.addHours(new Date(), 2), "yyyy-MM-dd HH:mm:ss");
                stringRedisTemplate.opsForValue().set(key, weChatPayParamVO.getCodeUrl(), 2, TimeUnit.HOURS);
                String timeKey = "QIAN_DAN_TIME:" + order.getId();
                stringRedisTemplate.opsForValue().set(timeKey, expirationTime, 2, TimeUnit.HOURS);
            } else {
                throw new SportException("签单状态更新异常");
            }
        }
        return weChatPayParamVO;
    }

    @Override
    public synchronized boolean updateOrder2Success(String orderPayId, Date payFinishTime) {
        log.info("{}进行状态更新", orderPayId);
        if (payFinishTime == null) {
            return false;
        }
        QueryWrapper<ProductOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ProductOrder::getPayOrderId, orderPayId);
        ProductOrder order = getOne(wrapper);
        if (order != null) {
            ProductOrder po = new ProductOrder();
            po.setId(order.getId());
            po.setPayFinishTime(payFinishTime);
            if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                po.setStatus(OrderStatus.PAY.getCode());
            } else if (order.getStatus() == 20) {
                po.setStatus(OrderStatus.ORDER_FINISH.getCode());
            }
            return updateById(po);
        }
        return true;
    }

    @Override
    public Map<String, String> getPayCode(String id) {
        ProductOrder order = getById(id);
        if (order == null) {
            throw new SportException(id + "错误");
        }
        if (Objects.equals(order.getStatus(), OrderStatus.UN_PAY.getCode()) ||
                Objects.equals(order.getStatus(), 20)) {
            Company company = companyService.getCompanyByMiniAppId(SportConstant.QU_SHU_MINI_APP);
            if (company == null || StringUtils.isEmpty(company.getMiniAppId()) || StringUtils.isEmpty(company.getMiniAppPayId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常!");
            }

            String timeKey = "QIAN_DAN_TIME:" + order.getId();
            String expirationTime = stringRedisTemplate.opsForValue().get(timeKey);
            WeChatPayParamVO weChatPayParamVO = getPayCodeUrl(order, company);
            Map<String, String> result = new HashMap<>();
            result.put("codeUrl", weChatPayParamVO.getCodeUrl());
            result.put("expirationTime", expirationTime);
            return result;
        } else {
            throw new SportException(id + "状态错误");
        }
    }

    @Override
    public Map<String, Object> getAgreementUrl(String id) {
        ProductOrder order = getById(id);
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotEmpty(order.getAgreementUrl())) {
            if (order.getAgreementUrl().endsWith(".pdf")) {
                result.put("preview", FileUtil.getImageFullUrl(order.getAgreementUrl()));
            } else {
                String preview = FileUtil.getAgreementBase64(order.getAgreementUrl());
                result.put("preview", "https://manager.highyundong.com/preview/onlinePreview?url=" + preview);
            }
            result.put("status", order.getStatus());
            return result;
        }
        String agreementFile = sportAgreementPath + "qu-shu-agreement-v3.docx";
        File tempPath = new File(sportAgreementPath + "/soft-agreement-temp");
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        }
        // OSS地址的待签署的协议文件
        String ossFile = "doc/soft-agreement/un-sign/" + id + ".docx";
        String outFile = sportAgreementPath + "/soft-agreement-temp/" + id + "_tmp" + ".docx";

        User user = userService.getById(order.getUserId());

        BigDecimal bigDecimal = order.getSoftPrice().setScale(2, RoundingMode.HALF_UP);

        Map<String, String> param = new HashMap<>();
        param.put("companyName", order.getCompanyName());
        param.put("customerName", order.getCustomerName());
        param.put("customerTel", order.getCustomerTel());
        param.put("customerAddress", order.getCustomerAddress());
        param.put("customerEmail", order.getCustomerEmail());
        param.put("adminPhone", order.getAdminPhone());
        param.put("saleName", user.getName());
        param.put("salePhone", user.getPhone());
        param.put("saleEmail", user.getMail());
        param.put("startDay", DateFormatUtils.format(order.getStartDay(), "yyyy-MM-dd"));
        param.put("amount", bigDecimal.toString());
        param.put("terms", order.getTerms() == null ? "" : order.getTerms());
//        param.put("terms", "1.1.4 条款1" + "\n" + "1.1.5 我是条款2");
//        param.put("terms", "1.1.4 条款1");
        param.put("cAmount", NumberToChineseMoneyUtil.convertToChinese(bigDecimal.doubleValue()));

        try {
            // 生成本地待签署的软件协议
            WordUtil.createAgreement2File(agreementFile, param, outFile);
            // 上传到OSS
            OssUtil.uploadSingleImage(ossFile, Files.newInputStream(Paths.get(outFile)));
            // 更新地址
            ProductOrder po = new ProductOrder();
            po.setId(order.getId());
            po.setAgreementUrl(ossFile);
            updateById(po);

            String preview = FileUtil.getAgreementBase64(ossFile);

            result.put("preview", "https://manager.highyundong.com/preview/onlinePreview?url=" + preview);
            result.put("status", order.getStatus());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String agreementSign(String id, String chapterPath) {
        String agreementPath = sportAgreementPath + "/soft-agreement-temp/" + id + "_tmp" + ".docx";
        File agreementFile = new File(agreementPath);
        if (!agreementFile.exists()) {
            throw new SportException("协议不存在");
        }
        Map<String, String> param = new HashMap<>();
        param.put("甲方章", chapterPath);
        param.put("合同日期", DateFormatUtils.format(new Date(), "yyyy年MM月dd日"));
        try {
            String outFile = sportAgreementPath + "/soft-agreement/sign/" + id + ".docx";
            FileUtil.checkPath(sportAgreementPath + "/soft-agreement/sign");
            WordUtil.createAgreement2File(agreementPath, param, outFile);
            return outFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> uploadChapter(MultipartFile image, String id) {
        ProductOrder order = getById(id);
        if (order == null) {
            throw new SportException("id错误");
        }
        if (order.getStatus() == 20) {
            throw new SportException("协议已签署");
        }
        String uploadPath = sportAgreementPath + "/soft-agreement-temp/";
        // 创建上传目录
        FileUtil.checkPath(uploadPath);
        String fileName = id + "_chapter" + ".docx";
        // 生成唯一的文件名
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            // 保存文件
            Files.write(filePath, image.getBytes());
            // 签署协议
            String agreementUrl = agreementSign(id, uploadPath + fileName);
            String preview = FileUtil.getAgreementBase64(agreementUrl);
            Map<String, String> result = new HashMap<>();
            result.put("download", FileUtil.getImageFullUrl(agreementUrl));
            result.put("preview", "https://manager.highyundong.com/preview/onlinePreview?url=" + preview);
            File chapterFile = new File(uploadPath + fileName);
            // 删除本地公司章
            chapterFile.deleteOnExit();
            // 更新签单状态 和 协议
            ProductOrder update = new ProductOrder();
            update.setId(id);
            if (order.getStatus().equals(OrderStatus.PAY.getCode())) {
                update.setStatus(OrderStatus.ORDER_FINISH.getCode());
            } else {
                update.setStatus(20);
            }
            update.setAgreementUrl(agreementUrl);
            updateById(update);

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductOrderManagerVO> getMyOrder() {
        User user = userService.getById(OauthUtils.getCurrentUserId());
        QueryWrapper<ProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductOrder::getAdminPhone, user.getPhone())
                .ne(ProductOrder::getStatus, DataStatus.DELETE.getCode())
                .orderByDesc(ProductOrder::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            ProductOrderManagerVO vo = new ProductOrderManagerVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setStatus(item.getStatus());
            vo.setAmount(item.getAmount());
            vo.setStartDay(item.getStartDay());
            vo.setCreateDate(item.getCreateDate());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> sign(String id) {
        ProductOrder order = getById(id);
        if (order == null) {
            throw new SportException("id错误");
        }
        if (order.getStatus() == 20) {
            throw new SportException("协议已签署");
        }
        if (StringUtils.isEmpty(order.getSeal())) {
            throw new SportException("请先生成电子公章");
        }

        String localPdf = sportAgreementPath + "/soft-agreement/sign/" + id + ".pdf";
        // 签署协议 本地word文件
        String agreementFile = agreementSign(id, FileUtil.getImageFullUrl(order.getSeal()));
        WordUtil.convertDocxToPdf(agreementFile, localPdf);

        String ossFile = "doc/soft-agreement/sign/" + id + ".pdf";
        try {
            OssUtil.uploadSingleImage(ossFile, Files.newInputStream(Paths.get(localPdf)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> result = new HashMap<>();
        result.put("download", FileUtil.getImageFullUrl(ossFile));
        result.put("preview", FileUtil.getImageFullUrl(ossFile));
        // 更新签单状态 和 协议
        ProductOrder update = new ProductOrder();
        update.setId(id);
        if (order.getStatus().equals(OrderStatus.PAY.getCode())) {
            update.setStatus(OrderStatus.ORDER_FINISH.getCode());
        } else {
            update.setStatus(20);
        }
        update.setAgreementUrl(ossFile);
        updateById(update);

        // 删除临时文件 pdf temp docx
        FileUtil.deleteFile(localPdf);
        FileUtil.deleteFile(sportAgreementPath + "/soft-agreement/sign/" + id + ".docx");
        FileUtil.deleteFile(sportAgreementPath + "/soft-agreement-temp/" + id + "_tmp" + ".docx");
        return result;
    }

    @Override
    public String createSeal(String name, String id) {
        ProductOrder order = getById(id);
        if (order == null) {
            throw new SportException("id错误");
        }
        if (StringUtils.isEmpty(name)) {
            throw new SportException("公司名称不能为空");
        }
        byte[] bytes = entSealGenerateService.generateEntSeal(name, "专用章");
        String seal = "doc/soft-agreement/seal/" + id + "_seal" + ".png";
        OssUtil.uploadSingleImage(seal, bytes);

        ProductOrder update = new ProductOrder();
        update.setId(id);
        update.setSeal(seal);
        update.setCompanyName(name);
        updateById(update);

        return FileUtil.getImageFullUrl(seal);
    }

    @Override
    public Map<String, Object> getAgreementStatus(String id) {
        Map<String, Object> agreementStatus = getAgreementUrl(id);
        if ("20".equals(agreementStatus.get("status")) || "6".equals(agreementStatus.get("status"))) {
            return agreementStatus;
        }
        ProductOrder order = getById(id);
        if (StringUtils.isNotEmpty(order.getSeal())) {
            agreementStatus.put("seal", FileUtil.getImageFullUrl(order.getSeal()));
        } else {
            agreementStatus.put("seal", "");
        }
        agreementStatus.put("companyName", order.getCompanyName());
        return agreementStatus;
    }
}
