package com.aioveu.data.sync.dp;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.common.CommonField;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.data.sync.dp.resp.*;
import com.aioveu.data.sync.parent.AbstractDataSyncProcessor;
import com.aioveu.data.sync.parent.DataSyncProcessor;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.entity.VenueFieldSyncAlias;
import com.aioveu.entity.VenueSyncAlias;
import com.aioveu.enums.FieldPlanLockChannels;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.exception.SportException;
import com.aioveu.request.RestTemplateUtils;
import com.aioveu.utils.EncryptionUtil;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.RedisUtil;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.CommonResponse;
import com.aioveu.vo.FieldPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.net.URI;
import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description 小程序订场数据同步到点评平台
 * @author: 雒世松
 * @date: 2025/3/28 15:20
 */
@Slf4j
@Component("dpDataSyncProcessor")
public class DpDataSyncProcessor extends AbstractDataSyncProcessor {

    @Value(value = "${sport.python.data-url}")
    private String sportPythonDataUrl;

    @Override
    public boolean initData(SyncDataAccountConfig config) {
        // 缓存场馆id 名称 和 场地id 名称
        clearCache(config.getStoreId());

        String accountId = getAccountId(config);
        if (accountId == null) {
            throw new SportException("账号信息获取失败");
        }
        savePlatformConfig(config.getStoreId(), "accountId", accountId);
        List<CommonVenue> venueFields = getVenueFields(config);
        if (CollectionUtils.isNotEmpty(venueFields)){
            return true;
        }
        log.error("未同步到场馆和场地信息");
        throw new SportException("未同步到场馆和场地信息, 请检查场馆场地名称是否一致");
    }

    @Override
    public String login(SyncDataAccountConfig config) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", config.getPlatformUsername());
        params.put("password", EncryptionUtil.decryptWithAES(config.getPlatformPassword(), EncryptionUtil.AES_KEY));
        params.put("storeId", config.getStoreId());
        String url = sportPythonDataUrl + "/dp-login";
        CommonResponse<Boolean> response = RestTemplateUtils.requestObjectPost(getProxyRestTemplate(), url, params, new ParameterizedTypeReference<CommonResponse<Boolean>>(){});
        if (response.getData()) {
            return "success";
        }
        return null;
    }

    @Override
    public String getPlatformPrefix() {
        return "DP";
    }

    private String getAccountId(SyncDataAccountConfig config) {
        String url = "https://e.dianping.com/merchant/portal/common/accountinfo";
        Tuple2<URI, HttpEntity<?>> bP = getBasicRequestParam(config, url, null, null);
        DpAccountResponse response = RestTemplateUtils.simpleGet(getProxyRestTemplate(), bP.getT1(), null, 0, bP.getT2(), new ParameterizedTypeReference<DpAccountResponse>(){});
        if (response == null) {
            return null;
        }
        if (response.getData() != null) {
            return response.getData().getShopAccount();
        } else {
            log.info("美团店铺获取账号错误:{}", response.getError());
        }
        return null;
    }

    /**
     * 获取基本请求信息
     * @param config
     * @param url
     * @param referer
     * @return
     */
    private Tuple2<URI, HttpEntity<?>> getBasicRequestParam(SyncDataAccountConfig config, String url, String referer, MultiValueMap<String, String> requestParams) {
        String authJson = getAuthConfig(config);
        String cookie = getHeaderCookie(authJson);
        // 准备cookies
        HttpHeaders headers = getHttpHeaders(cookie, referer);
        MultiValueMap<String, String> params = getCommonParams();
        if (requestParams != null) {
            params.putAll(requestParams);
        }
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // 构建URI，添加查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        return Tuples.of(builder.build().encode().toUri(), requestEntity);
    }

    @Override
    public List<CommonVenue> requestVenue(SyncDataAccountConfig config, String key, List<VenueSyncAlias> storeVenueList, Map<Long, List<VenueFieldSyncAlias>> venueFieldMap){
        // 获取该账号下的场馆
        String url = "https://e.dianping.com/sku/api/merchant/dzbook/realtimestockshoplist.json";
        String referer = "https://e.dianping.com/product/merchant/commonbook/current/repertory";
        Tuple2<URI, HttpEntity<?>> bP = getBasicRequestParam(config, url, referer, null);
        DpVenueResponse response = RestTemplateUtils.simpleGet(getProxyRestTemplate(), bP.getT1(), null, 0, bP.getT2(), new ParameterizedTypeReference<DpVenueResponse>(){});
        if (response.getSuccess()) {
            List<DpVenue> venues = response.getData();
            List<CommonVenue> venueList = new ArrayList<>(venues.size());
            for (DpVenue dpVenue : venues) {
                Optional<VenueSyncAlias> first = storeVenueList.stream().filter(item -> item.getAliasName().equals(dpVenue.getShopName())).findFirst();
                if (!first.isPresent()) {
                    log.error("美团点评:{}场馆未匹配到，忽略该场馆的订场同步", dpVenue.getShopName());
                    continue;
                }

                VenueSyncAlias venueSyncAlias = first.get();

                MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
                requestParams.add("shopId", dpVenue.getShopId());
                String fieldUrl = "https://e.dianping.com/sku/api/merchant/dzbook/getstockboardqueryoptions.api";
                String fieldReferer = "https://e.dianping.com/product/merchant/commonbook/current/repertory";
                Tuple2<URI, HttpEntity<?>> fieldBp = getBasicRequestParam(config, fieldUrl, fieldReferer, requestParams);
                DpFieldResponse<JSONArray> fieldResponse = RestTemplateUtils.simpleGet(getProxyRestTemplate(), fieldBp.getT1(), null, 0, fieldBp.getT2(), new ParameterizedTypeReference<DpFieldResponse<JSONArray>>(){});
                if (fieldResponse.getSuccess()) {
                    JSONArray fieldArray = fieldResponse.getData();
                    for (int i = 0; i < fieldArray.size(); i++) {
                        JSONObject fieldObject = fieldArray.getJSONObject(i);
                        if (fieldObject != null && fieldObject.containsKey("optionName") && "siteType".equals(fieldObject.getString("optionName"))) {
                            JSONArray items = fieldObject.getJSONArray("items");
                            // 第一个是全部场地
                            JSONObject jsonObject = items.getJSONObject(0);
                            JSONArray products = jsonObject.getJSONArray("products");
                            List<DpField> fields = new ArrayList<>(products.size());
                            for (int j = 0; j < products.size(); j++) {
                                JSONObject productObject = products.getJSONObject(j);
                                DpField dpField = new DpField();
                                dpField.setProductId(productObject.getLong("productId"));
                                dpField.setProductName(productObject.getString("productName"));
                                fields.add(dpField);
                            }
                            dpVenue.setFields(fields);
                        }
                    }
                }
                List<DpField> fields = dpVenue.getFields();
                if (CollectionUtils.isNotEmpty(fields)) {
                    CommonVenue venue = new CommonVenue();
                    venue.setAliasId(dpVenue.getShopId());
                    venue.setAliasName(dpVenue.getShopName());
                    venue.setId(venueSyncAlias.getVenueId());
                    venue.setName(venueSyncAlias.getVenueName());

                    List<VenueFieldSyncAlias> fieldList = venueFieldMap.get(venueSyncAlias.getVenueId());
                    List<CommonField> commonFields = new ArrayList<>(fields.size());
                    // 场地别名 匹配第三方场地
                    for (DpField dpField : fields) {
                        Optional<VenueFieldSyncAlias> fieldFirst = fieldList.stream().filter(item -> item.getAliasName().equals(dpField.getProductName())).findFirst();
                        if (!fieldFirst.isPresent()) {
                            log.error("点评:{}场地未匹配到，忽略该场馆的场地订场同步", dpField.getProductName());
                            continue;
                        }
                        VenueFieldSyncAlias venueFieldSyncAlias = fieldFirst.get();
                        CommonField field = new CommonField();
                        field.setAliasId(dpField.getProductId() + "");
                        field.setAliasName(dpField.getProductName());
                        field.setId(venueFieldSyncAlias.getFieldId());
                        field.setName(venueFieldSyncAlias.getFieldName());
                        commonFields.add(field);
                    }
                    if (CollectionUtils.isNotEmpty(commonFields)) {
                        venue.setFields(commonFields);
                        venueList.add(venue);
                    } else {
                        log.error("点评场馆:{}未匹配到任何场地, 同步忽略", dpVenue.getShopName());
                    }
                } else {
                    log.error("场馆:{}未匹配到任何场地", dpVenue.getShopName());
                }
            }
            if (CollectionUtils.isNotEmpty(venueList)){
                RedisUtil.setList(key, venueList, redisTemplate);
            } else {
                throw new SportException("美团点评未匹配到任何场馆");
            }
            return venueList;
        } else {
            log.error("美团店铺获取场馆失败:{}", response.getMsg());
        }
        return null;
    }

    /**
     * 订场处理
     * @param fieldPlanList
     * @param config
     * @param optVer 0 锁场 1 解锁
     * @return
     */
    private void fieldHandle(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, String optVer) {
        FieldPlanDTO fieldPlanDTO = fieldPlanList.get(0);
        CommonVenue dpVenue = getVenueByName(fieldPlanDTO.getVenueName(), config);
        if (dpVenue == null) {
            log.error("未获取到对应场馆：{}", fieldPlanDTO.getVenueName());
            throw new SportException("未获取到对应场馆:" + fieldPlanDTO.getVenueName());
        }
        CommonField dpField = getFieldByName(dpVenue.getFields(), fieldPlanDTO.getFieldName());
        if (dpField == null){
            log.error("未获取到对应场地：{},{}", dpVenue.getName(), fieldPlanDTO.getFieldName());
            throw new SportException("未获取到对应场地");
        }
        String accountId = getPlatformConfig(config.getStoreId(), "accountId") + "";
        if (StringUtils.isEmpty(accountId)){
            log.error("未获取到账号id：{}", fieldPlanDTO.getVenueName());
            throw new SportException("未获取到账号id");
        }
        log.info("开始同步:{} 的{} {}的锁场", dpVenue.getName(), DateFormatUtils.format(fieldPlanDTO.getFieldDay(), "yyyy-MM-dd"), dpField.getName());
        String url = "https://e.dianping.com/sku/api/merchant/dzbook/batchproductstockofflineoccupy.json";
        String referer = "https://e.dianping.com/product/merchant/commonbook/current/repertory";
        String authJson = getAuthConfig(config);
        String cookie = getHeaderCookie(authJson);
        // 准备cookies
        HttpHeaders headers = getHttpHeaders(cookie, referer);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        List<Map<String, String>> selectDateList = new ArrayList<>();
        for (FieldPlanDTO item : fieldPlanList) {
            Map<String, String> selectDateMap = new HashMap<>();
            selectDateMap.put("startDate", DateFormatUtils.format(item.getFieldDay(), "yyyy-MM-dd"));
            selectDateMap.put("endDate", DateFormatUtils.format(item.getFieldDay(), "yyyy-MM-dd"));
            selectDateMap.put("startTime", SportDateUtils.timeFormat(getTimeByConfig(config.getPlatformHalfHour(), item.getStartTime(), true), "HH:mm"));
            selectDateMap.put("endTime", SportDateUtils.timeFormat(getTimeByConfig(config.getPlatformHalfHour(), item.getEndTime(), false), "HH:mm"));
            selectDateList.add(selectDateMap);
        }
        // From参数
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("shopid", dpVenue.getAliasId());
        data.add("optver", optVer);
        // 场地id
        data.add("productids", dpField.getAliasId());
        data.add("selectdates", JacksonUtils.obj2Json(selectDateList));
        data.add("occupyDesc", fieldPlanDTO.getRemark());
        data.add("account", accountId);
        if ("1".equals(optVer)) {
            data.add("transType", "offlineOccupy");
            data.add("transId", RandomUtil.randomInt(140000, 149999) + "");
        }
        HttpEntity<?> requestEntity = new HttpEntity<>(data, headers);

        MultiValueMap<String, String> params = getCommonParams();
        // 构建URI，添加查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        DpFieldActionResponse response = RestTemplateUtils.requestFormPost(getProxyRestTemplate(), builder.build().encode().toUri(), requestEntity, new ParameterizedTypeReference<DpFieldActionResponse>(){});
        if (!response.getSuccess()) {
            log.error("美团点评场地操作失败:{}, 原始场地:{}, 操作类型:{}", response.getMsg(), fieldPlanDTO.getId(), optVer);
            throw new SportException(response.getMsg());
        }
    }


    @Override
    public List<FieldPlanVO> fetchMsgFieldPlans(SyncDataAccountConfig config, Long lastTime, String auth) {
        List<FieldPlanVO> pageList;
        List<FieldPlanVO> totalList = new ArrayList<>();
        int page = 0;
        int pageSize = 20;
        List<CommonVenue> fieldVenues = getVenueFields(config);
        do{
            pageList = fetchFieldPlansByPage(config, lastTime, page, pageSize, fieldVenues);
            page ++;
            if (CollectionUtils.isNotEmpty(pageList)){
                totalList.addAll(pageList);
            }
            // 分页查找的数据不为空 并且和页码相等，说明还需要继续分页查找
        } while (CollectionUtils.isNotEmpty(pageList) && pageList.size() == pageSize);
        //反转元素
        Collections.reverse(totalList);
        return totalList;
    }

    /**
     * 解析缓存的cookie
     * @param authJson
     * @return
     */
    private String getHeaderCookie(String authJson) {
        try {
            List<Map<String, Object>> cookieMap = JacksonUtils.objectMapper.readValue(authJson, new TypeReference<List<Map<String, Object>>>(){});
            StringBuilder cookieStr = new StringBuilder();
            for (Map<String, Object> cookie : cookieMap) {
                cookieStr.append(cookie.get("name")).append("=").append(cookie.get("value")).append("; ");
            }
            return cookieStr.toString();
        } catch (JsonProcessingException e) {
            log.error("点评cookie解析失败" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求头
     * @param cookie
     * @param referer
     * @return
     */
    private HttpHeaders getHttpHeaders(String cookie, String referer) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        if (referer != null) {
            headers.set("referer", referer);
        }
        headers.set("origin", "https://e.dianping.com");
        headers.set("Host", "e.dianping.com");
        headers.set("Accept", "*/*");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
        return headers;
    }

    /**
     * 获取功能参数
     * @return
     */
    private MultiValueMap<String, String> getCommonParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("yodaReady", "h5");
        params.add("csecplatform", "4");
        params.add("csecversion", "3.1.0");
        return params;
    }

    /**
     * 分页查找点评订单
     * @param config
     * @param lastTime
     * @param page
     * @param pageSize
     * @param fieldVenues
     * @return
     */
    private List<FieldPlanVO> fetchFieldPlansByPage(SyncDataAccountConfig config, Long lastTime, int page, int pageSize, List<CommonVenue> fieldVenues) {
        List<CommonVenue> venueFields = getVenueFields(config);
        if (CollectionUtils.isEmpty(venueFields)) {
            log.error("未找到美团点评的场馆信息");
            return Collections.emptyList();
        }
        String url = "https://e.dianping.com/trade/order/orderlist.api";
        String referer = "https://e.dianping.com/tra-pc-platform-web/universal-booking-order/index.html";

        CommonVenue dpVenue = venueFields.get(0);

        MultiValueMap<String, String> params = getCommonParams();
        params.add("shopid", dpVenue.getAliasId());
        params.add("shopidstr", dpVenue.getAliasId());
        params.add("bizname", "commonbook");
        params.add("searchstring", "");

        // 1 待接单 3已消费 2 已接单 未消费 0 全部
        params.add("currordertype", "0");
        params.add("start", page + "");
        params.add("limit", pageSize + "");

        Date date = new Date();
        String day = DateFormatUtils.format(date, "yyyy-MM-dd");
        try {
            Date start = DateUtils.parseDate(day + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.parseDate(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            params.add("orderstarttime", start.getTime() + "");
            params.add("orderendtime", end.getTime() + "");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Tuple2<URI, HttpEntity<?>> bp = getBasicRequestParam(config, url, referer, params);
        DpOrderResponse response = RestTemplateUtils.simpleGet(getProxyRestTemplate(), bp.getT1(), null, 0, bp.getT2(), new ParameterizedTypeReference<DpOrderResponse>(){});
        if (response == null) {
            log.error("未查询到美团点评订单");
            return Collections.emptyList();
        }
        List<DpOrder> orderList = response.getList();
        if (CollectionUtils.isEmpty(orderList)) {
            log.info("暂无美团点评订单");
            return Collections.emptyList();
        }
        List<FieldPlanVO> fieldPlanList = new ArrayList<>();
        Long firstOrderTime = null;
        for (DpOrder order : orderList) {
            try {
                Date orderTime;
                if (order.getDisplayStatus() == 5 || order.getDisplayStatus() == 6) {
                    // 对于退款订单 根据退款时间来计算订单时间
                    orderTime = DateUtils.parseDate(order.getRefundTime(), "yyyy-MM-dd HH:mm:ss");
                } else {
                    orderTime = DateUtils.parseDate(order.getOrderDate(), "yyyy-MM-dd HH:mm:ss");
                }
                // 对比时间
                if (newFieldOrder(lastTime, orderTime)) {
                    if (order.getDisplayStatus() == 2 || order.getDisplayStatus() == 1 || order.getDisplayStatus() == 5 || order.getDisplayStatus() == 6) {
                        if (firstOrderTime == null && page == 0) {
                            firstOrderTime = orderTime.getTime();
                        }
                        List<DpFieldPlan> dpFieldPlanList = getFieldPlanFromDpOrder(order);
                        for (DpFieldPlan dpFieldPlan : dpFieldPlanList) {
                            Optional<CommonVenue> firstVenue = fieldVenues.stream().filter(item -> item.getAliasName().equals(dpFieldPlan.getVenueName())).findFirst();
                            if (!firstVenue.isPresent()) {
                                log.error("美团店铺未匹配到场地场馆：{}", dpFieldPlan.getVenueName());
                                continue;
                            }
                            CommonVenue venue = firstVenue.get();
                            List<CommonField> fields = venue.getFields();
                            Optional<CommonField> firstField = fields.stream().filter(item -> item.getAliasName().equals(dpFieldPlan.getFieldName())).findFirst();
                            if (!firstField.isPresent()) {
                                log.error("美团店铺未匹配到场地场馆：{}, {}", dpFieldPlan.getFieldName(), dpFieldPlan.getVenueName());
                                continue;
                            }
                            CommonField field = firstField.get();
                            FieldPlanVO item = new FieldPlanVO();
                            BeanUtils.copyProperties(dpFieldPlan, item);
                            if (order.getDisplayStatus() == 2 || order.getDisplayStatus() == 1) {
                                // 2 未消费 1 待接单 用户已下单 可以直接锁场
                                item.setStatus(FieldPlanStatus.Occupy.getCode());
                            } else if (order.getDisplayStatus() == 5 || order.getDisplayStatus() == 6) {
                                // 已申请退款了，或者商家拒单了 应该解锁对应场地
                                item.setStatus(FieldPlanStatus.Normal.getCode());
                            }
                            item.setVenueId(venue.getId());
                            item.setFieldId(field.getId());
                            item.setFieldDay(DateUtils.parseDate(dpFieldPlan.getFieldDay(), "yyyy-MM-dd"));
                            item.setStartTime(Time.valueOf(dpFieldPlan.getStartTime() + ":00"));
                            item.setEndTime(Time.valueOf(dpFieldPlan.getEndTime() + ":00"));
                            fieldPlanList.add(item);
                        }
                    } else {
                        log.debug("非同步订单，不进行同步:{}， 订单状态:{}", order.getUnifiedOrderId(), order.getDisplayStatusText());
                    }
                } else {
                    log.debug("历史订单，不进行同步:{}, 时间:{}", order.getUnifiedOrderId(), order.getOrderDate());
                }
            } catch (ParseException e) {
                log.error("订单解析异常" + e);
                e.printStackTrace();
            }
        }
        if (firstOrderTime != null){
            // 取消息列表的第一条消息的时间 作为同步的最新时间
            stringRedisTemplate.opsForValue().set(LAST_MSG_TIME + getPlatformPrefix() + "_"+ config.getStoreId(), firstOrderTime + "", 6, TimeUnit.DAYS);
        }
        return fieldPlanList;
    }

    /**
     * 点评订单转订场计划
     * @param order
     * @return
     */
    private List<DpFieldPlan> getFieldPlanFromDpOrder(DpOrder order) {
        String bookStartTime = order.getBookStartTime();
        String[] bookSplit = bookStartTime.split("\n");
        String dayStr = bookSplit[0];
        List<DpFieldPlan> dpFieldPlanList = new ArrayList<>(bookSplit.length);
        for (int i = 1; i < bookSplit.length; i++) {
            String book = bookSplit[i];
            String[] fieldSplit = book.split("\\|");
            String fieldName = fieldSplit[0];
            String timeStr = fieldSplit[1];
            String[] timeSplit = timeStr.split("-");
            DpFieldPlan dpFieldPlan = new DpFieldPlan();
            dpFieldPlan.setFieldName(fieldName);
            dpFieldPlan.setFieldDay(dayStr);
            dpFieldPlan.setAmount(order.getActualAmount());
            dpFieldPlan.setStartTime(timeSplit[0]);
            dpFieldPlan.setEndTime(timeSplit[1]);
            dpFieldPlan.setVenueName(order.getShopName());
            if (StringUtils.isNotEmpty(order.getShopRemark())) {
                dpFieldPlan.setRemark(order.getShopRemark());
            }
            if (StringUtils.isNotEmpty(order.getUserRemark()) && !"无".equals(order.getUserRemark())) {
                dpFieldPlan.setRemark(dpFieldPlan.getRemark() + order.getUserRemark());
            }
            dpFieldPlan.setLockRemark(order.getChannelName() + " " + order.getEncryptedPhone());
            dpFieldPlan.setLockChannelName(order.getChannelName());
            dpFieldPlan.setLockChannel(FieldPlanLockChannels.DP.getCode());
            dpFieldPlanList.add(dpFieldPlan);
        }
        return dpFieldPlanList;
    }

    @Override
    public List<FieldPlanVO> fullSyncFieldPlatform2Qs(SyncDataAccountConfig config) {
        List<CommonVenue> venueFields = getVenueFields(config);
        if (CollectionUtils.isEmpty(venueFields)){
            return null;
        }
        List<VenueFieldSyncAlias> fieldVenues = venueFieldSyncAliasService.findByStoreIdAndPlatform(config.getStoreId(), config.getPlatformCode());
        Map<String, VenueFieldSyncAlias> quVenueMap = new HashMap<>(fieldVenues.size());
        for (VenueFieldSyncAlias item : fieldVenues) {
            quVenueMap.put(item.getVenueAliasName() + "_" + item.getAliasName(), item);
        }
        String url = "https://e.dianping.com/sku/api/merchant/dzbook/productweekstock.json";
        String referer = "https://e.dianping.com/product/merchant/commonbook/current/repertory";
        List<FieldPlanVO> list = new ArrayList<>();
        for (CommonVenue dpVenue : venueFields) {
            List<CommonField> fields = dpVenue.getFields();
            Map<String, VenueFieldSyncAlias> venueFieldMap = new HashMap<>(fieldVenues.size());
            for (CommonField dpField : fields) {
                VenueFieldSyncAlias venueFieldVO = quVenueMap.get(dpVenue.getName() + "_" + dpField.getName());
                if (venueFieldVO != null) {
                    venueFieldMap.put(dpField.getAliasId(), venueFieldVO);
                } else {
                    log.error("【{}】场馆，【{}】场地，未匹配到门店", dpVenue.getName(), dpField.getName());
                }
            }
            int bookDay = getFieldBookDay();
            for (int t = 0; t < bookDay; t++) {
                try {
                    Date currentDate = SportDateUtils.getDayNoHour(t);
                    String dayStr = DateFormatUtils.format(currentDate, "yyyy-MM-dd");
                    MultiValueMap<String, String> params = getCommonParams();
                    params.add("shopid", dpVenue.getAliasId());

                    Map<String, Object> extMap = new HashMap<>();
                    extMap.put("siteType", 0);
                    extMap.put("date", currentDate.getTime());
                    params.add("extOption", JacksonUtils.obj2Json(extMap));

                    Tuple2<URI, HttpEntity<?>> bp = getBasicRequestParam(config, url, referer, params);
                    DpFieldResponse<JSONObject> response = RestTemplateUtils.simpleGet(getProxyRestTemplate(), bp.getT1(), null, 0, bp.getT2(), new ParameterizedTypeReference<DpFieldResponse<JSONObject>>(){});
                    if (response == null) {
                        continue;
                    }
                    if (!response.getSuccess()) {
                        log.error("美团点评全量同步请求错误:{}", response.getMsg());
                    }
                    JSONObject data = response.getData();
                    JSONArray lstRow = data.getJSONArray("lstRow");
                    for (int a= 0; a < lstRow.size(); a++) {
                        JSONObject row = lstRow.getJSONObject(a);
                        JSONArray lstCell = row.getJSONArray("lstCell");
                        for (int b = 0; b < lstCell.size(); b++) {
                            JSONObject lock = lstCell.getJSONObject(b);
                            // 对比时间 如果已经过期的，不需要管
                            String fieldDayTime = dayStr + " " + row.getString("eperiod");
                            Date fieldDay = DateUtils.parseDate(fieldDayTime, "yyyy-MM-dd HH:mm");
                            if (fieldDay.getTime() < new Date().getTime()) {
                                log.info("订场已过期:{}", fieldDayTime);
                                continue;
                            }
                            Integer stockStatus  = lock.getInteger("stockStatus");
                            if (stockStatus == 5) {
                                log.info("订场已过期:{}", fieldDayTime);
                            } else if (stockStatus == 3 || stockStatus == 2) {
                                // stockStatus == 3后台锁场 stockStatus == 2 线上订单
                                VenueFieldSyncAlias venueFieldVO = venueFieldMap.get(lock.getLong("productId"));
                                if (venueFieldVO == null) {
                                    log.error("未找到匹配的场地:{}", lock.getLong("productId"));
                                    continue;
                                }
                                FieldPlanVO fieldPlanVO = new FieldPlanVO();
                                fieldPlanVO.setVenueId(venueFieldVO.getVenueId());
                                fieldPlanVO.setFieldId(venueFieldVO.getFieldId());
                                fieldPlanVO.setStatus(FieldPlanStatus.Occupy.getCode());
                                fieldPlanVO.setFieldDay(currentDate);
                                fieldPlanVO.setStartTime(Time.valueOf(row.getString("speriod") + ":00"));
                                fieldPlanVO.setEndTime(Time.valueOf(row.getString("eperiod") + ":00"));
                                String occupyDesc = stockStatus == 2 ? "线上订单" : "后台锁场" + lock.getString("occupyDesc");
                                fieldPlanVO.setRemark(config.getPlatformName() + " " + occupyDesc);
                                fieldPlanVO.setLockRemark(config.getPlatformName());
                                fieldPlanVO.setLockChannel(FieldPlanLockChannels.DP.getCode());
                                list.add(fieldPlanVO);
                            } else if (stockStatus == 1) {
                                // 订场正常
                            }
                        }
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 保存本次同步锁场的
        saveLastSyncTime(config.getStoreId(), new Date().getTime() + "");
        return list;
    }

    @Override
    public List<FieldSyncHandleError> syncFieldQs2Platform(List<FieldPlanDTO> fieldPlanDTOList, SyncDataAccountConfig config, String action) {
        log.info("趣数同步订场数据到美团点评:{}", fieldPlanDTOList.size());
        List<FieldSyncHandleError> errors = new ArrayList<>();
        // 先按场馆分组
        Map<String, List<FieldPlanDTO>> venueMap = fieldPlanDTOList.stream().collect(Collectors.groupingBy(FieldPlanDTO::getVenueName));
        for (List<FieldPlanDTO> fieldList : venueMap.values()) {
            // 再按场地分组
            Map<String, List<FieldPlanDTO>> fieldMap = fieldList.stream().collect(Collectors.groupingBy(FieldPlanDTO::getFieldName));
            for (List<FieldPlanDTO> fieldPlanList : fieldMap.values()) {
                // 再按日期进行分组
                Map<String, List<FieldPlanDTO>> fieldDayMap = fieldPlanList.stream().collect(Collectors.groupingBy(item -> DateFormatUtils.format(item.getFieldDay(), "yyyy-MM-dd")));
                // 对于同一个场馆 同一个场地的 同一天的 订场 进行批量统一锁场
                for (List<FieldPlanDTO> fieldPlanTimeList : fieldDayMap.values()) {
                    try {
                        fieldHandle(fieldPlanTimeList, config, DataSyncProcessor.LOCK.equals(action) ? "0" : "1");
                    }catch (Exception e) {
                        e.printStackTrace();
                        FieldSyncHandleError error = new FieldSyncHandleError();
                        error.setMsg(e.getMessage());
                        error.setFieldPlanDTOList(fieldPlanTimeList);
                        errors.add(error);
                    }
                }
            }
        }
        return errors;
    }

    @Override
    public int getFieldBookDay() {
        return 7;
    }
}
