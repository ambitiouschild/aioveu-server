package com.aioveu.data.sync.qyd;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.common.CommonField;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.data.sync.parent.AbstractDataSyncProcessor;
import com.aioveu.data.sync.qyd.resp.QydCourse;
import com.aioveu.data.sync.qyd.resp.QydFieldPlan;
import com.aioveu.data.sync.qyd.resp.QydResponse;
import com.aioveu.data.sync.qyd.resp.QydVenue;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.entity.VenueFieldSyncAlias;
import com.aioveu.entity.VenueSyncAlias;
import com.aioveu.enums.FieldPlanLockChannels;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.exception.SportException;
import com.aioveu.request.RestTemplateUtils;
import com.aioveu.request.response.DynamicProxyIP;
import com.aioveu.utils.EncryptionUtil;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.RedisUtil;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.FieldPlanVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description 小程序订场数据同步到趣运动平台
 * @author: 雒世松
 * @date: 2025/3/21 15:49
 */
@Slf4j
@Component("qydDataSyncProcessor")
public class QydDataSyncProcessor extends AbstractDataSyncProcessor {

    private final String REQUEST_URL = "https://api.saas.guanzhang.me";

    @Override
    public String getPlatformPrefix() {
        return "QYD";
    }

    /**
     * 平台登录
     * @param config
     * @return
     */
    @Override
    public String login(SyncDataAccountConfig config) {
        JSONObject param = new JSONObject();
        param.put("userNameOrEmailAddress", config.getPlatformUsername());
        param.put("password", EncryptionUtil.decryptWithAES(config.getPlatformPassword(), EncryptionUtil.AES_KEY));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Origin", config.getPlatformUrl());

        String url = REQUEST_URL + "/App/Tokenauth/Authenticate";
        DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
        if (dynamicProxyIP == null) {
            log.error("dynamicProxyIP get null");
            return null;
        }
        String proxyIp = dynamicProxyIP.getIp();
        int proxyPort = dynamicProxyIP.getPort();
        JSONObject body = RestTemplateUtils.requestObjectPost(getProxyRestTemplate(), url, param.toJSONString(), proxyIp, proxyPort, headers, new ParameterizedTypeReference<JSONObject>(){});
        if (body == null) {
            log.error("{}登录失败:未获取到返回信息", config.getPlatformUsername());
            return null;
        }
        if (body.getInteger("status") != 0 || !"success".equals(body.getString("msg"))){
            log.error("{}登录失败:{}", config.getPlatformUsername(), body.getString("msg"));
            return null;
        }
        log.info(JacksonUtils.obj2Json(body));
        JSONObject tokenObject = body.getJSONObject("data");
        // 有效期 默认是86400秒 24小时
        Integer expireInSeconds = tokenObject.getInteger("expireInSeconds");
        String token = tokenObject.getString("accessToken");
        if (StringUtils.isNotBlank(token)) {
            saveAuthConfig(token, config.getStoreId(), expireInSeconds - 120);
            return token;
        }
        return null;
    }

    /**
     * 接口请求获取场馆信息
     * @param config
     * @param key
     * @return
     */
    @Override
    public List<CommonVenue> requestVenue(SyncDataAccountConfig config, String key, List<VenueSyncAlias> storeVenueList, Map<Long, List<VenueFieldSyncAlias>> venueFieldMap){
        String auth = getAuthConfig(config);
        //获取门店下的场馆
        Map<String, Object> venusParam = new HashMap<>();
        venusParam.put("venuesId", getPlatformConfig(config.getStoreId(), "platformStoreId") + "");
        venusParam.put("qydOrderType", 0);
        venusParam.put("page", 1);
        venusParam.put("pageSize", 50);

//        DynamicProxyIP dynamicProxyIp = getDynamicProxyIP(config.getStoreId());
//        if (dynamicProxyIp == null) {
//            log.error("dynamicProxyIP get null");
//            return null;
//        }
//        String proxyIp = dynamicProxyIp.getIp();
//        int proxyPort = dynamicProxyIp.getPort();
        String proxyIp = null;
        int proxyPort = 0;

        QydResponse<JSONObject> body = RestTemplateUtils.simpleGet(getProxyRestTemplate(), REQUEST_URL + "/saas/Venues/getCategory?" + HttpUtil.toParams(venusParam), getHeaders(config, auth), proxyIp, proxyPort,
                new ParameterizedTypeReference<QydResponse<JSONObject>>() {});
        if (body == null){
            log.error("获取场馆失败");
            return null;
        }
        if (body.getStatus() != 0 || !"success".equals(body.getMsg())){
            log.error("获取场馆失败:{}",body.getMsg());
            return null;
        }
        log.info(JacksonUtils.obj2Json(body));
        List<QydVenue> qydVenues = body.getData().getJSONArray("items").toJavaList(QydVenue.class);
        List<CommonVenue> venueList = new ArrayList<>(storeVenueList.size());
        //循环场馆，分别获取每个场馆包含的场地信息 跟系统的场馆进行匹配 移除无用的场馆
        for (QydVenue qydVenue : qydVenues) {
            Optional<VenueSyncAlias> first = storeVenueList.stream().filter(item -> item.getAliasName().equals(qydVenue.getCat_name())).findFirst();
            if (!first.isPresent()) {
                log.error("趣运动:{}场馆未匹配到，忽略该场馆的订场同步", qydVenue.getCat_name());
                continue;
            }
            VenueSyncAlias venueSyncAlias = first.get();
            venusParam.put("categoryId", qydVenue.getCat_id());
            venusParam.put("status", 1);
            QydResponse<JSONObject> fieldBody = RestTemplateUtils.simpleGet(getProxyRestTemplate(), REQUEST_URL + "/saas/Course/courtList?" + HttpUtil.toParams(venusParam), getHeaders(config, auth), proxyIp, proxyPort,
                    new ParameterizedTypeReference<QydResponse<JSONObject>>() {});
            if (fieldBody == null) {
                log.error("获取场馆为空");
                return null;
            }
            if (fieldBody.getStatus() != 0 || !"success".equals(fieldBody.getMsg())) {
                log.error("获取场馆为空:{}", fieldBody.getMsg());
                return null;
            }
            List<QydCourse> courses = fieldBody.getData().getJSONArray("items").toJavaList(QydCourse.class);
            if (CollectionUtils.isNotEmpty(courses)) {
                CommonVenue venue = new CommonVenue();
                venue.setAliasId(qydVenue.getCat_id() + "");
                venue.setAliasName(qydVenue.getCat_name());
                venue.setId(venueSyncAlias.getVenueId());
                venue.setName(venueSyncAlias.getVenueName());

                List<VenueFieldSyncAlias> fieldList = venueFieldMap.get(venueSyncAlias.getVenueId());

                List<CommonField> fields = new ArrayList<>(courses.size());
                // 场地别名 匹配第三方场地
                for (QydCourse qydCourse : courses) {
                    Optional<VenueFieldSyncAlias> fieldFirst = fieldList.stream().filter(item -> item.getAliasName().equals(qydCourse.getCourse_name())).findFirst();
                    if (!fieldFirst.isPresent()) {
                        log.error("趣运动场馆:{} 场地{} 未匹配到，忽略该场馆的场地订场同步", qydVenue.getCat_name(), qydCourse.getCourse_name());
                        continue;
                    }
                    VenueFieldSyncAlias venueFieldSyncAlias = fieldFirst.get();
                    CommonField field = new CommonField();
                    field.setAliasId(qydCourse.getCourse_id() + "");
                    field.setAliasName(qydCourse.getCourse_name());
                    field.setId(venueFieldSyncAlias.getFieldId());
                    field.setName(venueFieldSyncAlias.getFieldName());
                    fields.add(field);
                }
                if (CollectionUtils.isNotEmpty(fields)) {
                    venue.setFields(fields);
                    venueList.add(venue);
                } else {
                    log.error("趣运动场馆:{}未匹配到任何场地, 同步忽略", qydVenue.getCat_name());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(venueList)){
            RedisUtil.setList(key, venueList, redisTemplate);
        } else {
            throw new SportException("未匹配到任何场馆");
        }
        return venueList;
    }

    @Override
    public boolean initData(SyncDataAccountConfig config) {
        clearCache(config.getStoreId());
        //校验门店名称是否存在，获取平台门店id并保存
        String platformStoreId = getPlatformStoreId(config);
        if (StringUtils.isBlank(platformStoreId)){
            throw new SportException("门店名称不一致，校验失败");
        }
        savePlatformConfig(config.getStoreId(), "platformStoreId", platformStoreId);
        return true;
    }

    private String getPlatformStoreId(SyncDataAccountConfig config) {
        String auth = getAuthConfig(config);
        DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
        if (dynamicProxyIP == null) {
            log.error("dynamicProxyIP get null");
            return null;
        }
        String proxyIp = dynamicProxyIP.getIp();
        int proxyPort = dynamicProxyIP.getPort();
        QydResponse<JSONObject> body = RestTemplateUtils.simpleGet(getProxyRestTemplate(), REQUEST_URL + "/Saas/Common/getConfig", getHeaders(config, auth), proxyIp, proxyPort,
                new ParameterizedTypeReference<QydResponse<JSONObject>>() {});

        if (body == null){
            log.error("获取场馆失败");
            return null;
        }
        if (body.getStatus() != 0 || !"success".equals(body.getMsg())){
            log.error("获取店铺信息失败:{}",body.getMsg());
            return null;
        }
        JSONArray tenants = body.getData().getJSONObject("custom").getJSONArray("tenants");
        if (tenants!= null && !tenants.isEmpty()) {
            return tenants.toJavaList(JSONObject.class).stream()
                    .filter(jsonObject -> config.getPlatformStoreName().equals(jsonObject.getString("venues_name")))
                    .map(jsonObject -> jsonObject.getString("venues_id"))
                    .findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public List<FieldPlanVO> fullSyncFieldPlatform2Qs(SyncDataAccountConfig config) {
        try {
            //手动执行 全量同步趣运动的锁场信息
            return firstFetchHandle(config);
        }catch (Exception e) {
            log.error("fetchFieldPlansData失败：{}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FieldSyncHandleError> syncFieldQs2Platform(List<FieldPlanDTO> fieldPlanDTOList, SyncDataAccountConfig config, String action) {
        log.info("趣数同步订场数据到趣运动锁场:{}", fieldPlanDTOList.size());
        List<FieldSyncHandleError> errors = new ArrayList<>();
        // 先按场馆分组
        Map<String, List<FieldPlanDTO>> venueMap = fieldPlanDTOList.stream().collect(Collectors.groupingBy(FieldPlanDTO::getVenueName));
        for (List<FieldPlanDTO> fieldList : venueMap.values()) {
            // 再按日期进行分组
            Map<String, List<FieldPlanDTO>> fieldDayMap = fieldList.stream().collect(Collectors.groupingBy(item -> DateFormatUtils.format(item.getFieldDay(), "yyyy-MM-dd")));
            // 对于同一个场馆 同一天的 订场 进行批量统一锁场
            for (List<FieldPlanDTO> fieldPlanTimeList : fieldDayMap.values()) {
                // 批量锁场
                try {
                    fieldHandle(fieldPlanTimeList, config, action);
                }catch (Exception e) {
                    e.printStackTrace();
                    FieldSyncHandleError error = new FieldSyncHandleError();
                    error.setMsg(e.getMessage());
                    error.setFieldPlanDTOList(fieldPlanTimeList);
                    errors.add(error);
                }
            }
        }
        return errors;
    }

    /**
     * 场地处理，解锁或加锁
     * @param fieldPlanList
     * @param config
     * @param handleType
     * @return
     */
    private void fieldHandle(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, String handleType){
        String auth = getAuthConfig(config);
        if (StringUtils.isBlank(auth)){
            log.error("{}获取认证失败", getPlatformPrefix());
            throw new SportException("获取认证失败");
        }
        FieldPlanDTO fieldPlanDTO = fieldPlanList.get(0);
        CommonVenue venue = getVenueByName(fieldPlanDTO.getVenueName(), config);
        if (venue == null){
            log.error("未获取到对应场馆：{}", fieldPlanDTO.getVenueName());
            throw new SportException("未获取到对应场馆:" + fieldPlanDTO.getVenueName());
        }
        QydLockParam param = new QydLockParam();
        //场馆id
        param.setCategoryId(Integer.parseInt(venue.getAliasId()));
        Date date = fieldPlanDTO.getFieldDay();
        param.setCurrentDate(date.getTime() / 1000);

        List<QydLockItemParam> array = new ArrayList<>(fieldPlanList.size());
        for (FieldPlanDTO item : fieldPlanList) {
            CommonField qydCourse = getFieldByName(venue.getFields(), item.getFieldName());
            if (qydCourse == null){
                log.error("未获取到对应场地：{},{}", venue.getName(), item.getFieldName());
                continue;
            }
            QydLockItemParam itemParam = new QydLockItemParam();
            //场地id
            itemParam.setCourtId(Integer.parseInt(qydCourse.getAliasId()));
            itemParam.setBeginTime(getSeconds(getTimeByConfig(config.getPlatformHalfHour(), item.getStartTime(), true).toString()));
            itemParam.setEndTime(getSeconds(getTimeByConfig(config.getPlatformHalfHour(), item.getEndTime(), false).toString()));
            array.add(itemParam);
        }
        if (array.isEmpty()){
            throw new SportException("场馆:" + fieldPlanDTO.getVenueName() + "未匹配到场地");
        }
        param.setItems(array);

        String paramStr = JSONObject.toJSONString(param);
        log.info("{},{},{},时间：{} {}-{},入参：{}", handleType,fieldPlanDTO.getVenueName(),fieldPlanDTO.getFieldName(), DateUtil.format(fieldPlanDTO.getFieldDay(),"yyyy-MM-dd"),fieldPlanDTO.getStartTime().toString(),fieldPlanDTO.getEndTime().toString(),paramStr);
        HttpHeaders headers = getHeaders(config, auth);
        String url = REQUEST_URL + "/saas/Venues/"+ handleType;
        DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
        if (dynamicProxyIP == null) {
            log.error("dynamicProxyIP get null");
            throw new SportException("同步IP获取失败");
        }
        String proxyIp = dynamicProxyIP.getIp();
        int proxyPort = dynamicProxyIP.getPort();
        String stringBody = RestTemplateUtils.requestObjectPost(getProxyRestTemplate(), url, paramStr, proxyIp, proxyPort, headers, new ParameterizedTypeReference<String>(){});
        if (stringBody == null) {
            log.error("{} 请求失败",handleType);
            throw new SportException("请求失败, 未返回内容");
        }
        log.info("qyd " + handleType + "response:{}", stringBody);
        QydResponse<Boolean> body = JSONObject.parseObject(stringBody, QydResponse.class);
        if (body.getStatus() != 0 || !"success".equals(body.getMsg())){
            log.error("{} 请求失败,原因:{}",handleType, body.getMsg());
            throw new SportException(body.getMsg());
        }
        log.info("{}成功", handleType);
    }

    /**
     * 获取趣运动通知消息详情
     * @param config
     * @param qydMsgId
     * @return
     */
    private String queryQydContent(SyncDataAccountConfig config, String qydMsgId, String auth){
        String content = "";
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", qydMsgId);
        try {
            DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
            if (dynamicProxyIP == null) {
                log.error("dynamicProxyIP get null");
                return null;
            }
            String proxyIp = dynamicProxyIP.getIp();
            int proxyPort = dynamicProxyIP.getPort();
            QydResponse<JSONObject> body = RestTemplateUtils.requestObjectPost(getProxyRestTemplate(), REQUEST_URL + "/saas/Message/detail", JSONObject.toJSONString(params), proxyIp, proxyPort,
                    getHeaders(config, auth), new ParameterizedTypeReference<QydResponse<JSONObject>>(){});
            if (body == null) {
                log.error("获取消息失败");
                return null;
            }
            if (body.getStatus() != 0 || !"success".equals(body.getMsg())) {
                log.error("获取消息失败:{}", body.getMsg());
                return null;
            }
            content = body.getData().getString("content");
        }catch (Exception e){
            log.error("queryQydContent {} error",qydMsgId,e.getMessage());
        }
        return content;
    }

    /**
     * 首次获取订场数据 从趣运动的全量订场来同步
     * @param config
     * @return
     */
    private List<FieldPlanVO> firstFetchHandle(SyncDataAccountConfig config) throws ParseException {
        List<CommonVenue> qydVenues = getVenueFields(config);
        if (CollectionUtils.isEmpty(qydVenues)){
            return null;
        }
        String auth = getAuthConfig(config);
        List<FieldPlanVO> list = new ArrayList<>();
        int bookDay = getFieldBookDay();
        DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
        if (dynamicProxyIP == null) {
            log.error("dynamicProxyIP get null");
            return null;
        }
        String proxyIp = dynamicProxyIP.getIp();
        int proxyPort = dynamicProxyIP.getPort();
        // 先遍历场馆
        for (int i = 0; i < qydVenues.size(); i++) {
            CommonVenue qydVenue = qydVenues.get(i);
            // 获取场馆对应的场地
            List<CommonField> fields = qydVenue.getFields();
            if (CollectionUtils.isEmpty(fields)){
                log.error("场馆:{}对应场地为空， 跳过同步", qydVenue.getName());
                continue;
            }
            // 再去查找该场馆未来7天的锁场信息
            for (int t = 0; t < bookDay; t++) {
                Date currentDate = SportDateUtils.getDayNoHour(t);
                try {
                    Map<String, Object> params = new HashMap<>();
                    // categoryId 对应第三方场馆的id
                    params.put("categoryId", qydVenue.getAliasId());
                    params.put("currentDate", currentDate.getTime()/1000);
                    // 获取每天指定场馆的锁场信息
                    String body = RestTemplateUtils.simpleGet(getProxyRestTemplate(), REQUEST_URL + "/saas/Order/getLockStockByDay?"+ HttpUtil.toParams(params), getHeaders(config, auth), proxyIp, proxyPort,
                            new ParameterizedTypeReference<String>(){});
                    if (body == null) {
                        log.error("getLockStockByDay获取数据失败");
                        return null;
                    }
                    QydResponse qydResponse = JSONObject.parseObject(body, QydResponse.class);
                    if (qydResponse.getStatus() != 0 || !"success".equals(qydResponse.getMsg())) {
                        log.error("获取数据失败:{}", qydResponse.getMsg());
                        return null;
                    }
                    Object data = qydResponse.getData();
                    //一般是json格式，返回就是{}，表示空数据
                    if(data instanceof JSONObject){
                        continue;
                    }
                    JSONArray lockList = (JSONArray) data;
                    for (int j = 0; j < lockList.size(); j++) {
                        JSONObject lock = lockList.getJSONObject(j);
                        int courseId = lock.getIntValue("course_id");
                        // 通过锁场信息 找到对应锁场的场地 根据场馆下的场地列表进行匹配
                        CommonField qydCourse = qydVenue.getFields().stream().filter(item -> item.getAliasId().equals(courseId + "")).findFirst().orElse(null);
                        if (qydCourse == null){
                            continue;
                        }
                        FieldPlanVO fieldPlanVO = new FieldPlanVO();
                        Long startSeconds = lock.getLong("start_seconds");
                        Long endSeconds = lock.getLong("end_seconds");
                        fieldPlanVO.setVenueId(qydVenue.getId());
                        fieldPlanVO.setFieldId(qydCourse.getId());
                        fieldPlanVO.setStatus(FieldPlanStatus.Occupy.getCode());
                        fieldPlanVO.setFieldDay(currentDate);
                        fieldPlanVO.setStartTime(Time.valueOf(getTimeStr(startSeconds)));
                        fieldPlanVO.setEndTime(Time.valueOf(getTimeStr(endSeconds)));
                        String phone = lock.getString("phone");
                        if (StringUtils.isNotBlank(phone)){
                            phone = phone.substring(phone.length() - 4);
                        }
                        fieldPlanVO.setRemark(config.getPlatformName() + phone);
                        fieldPlanVO.setLockRemark(config.getPlatformName());
                        fieldPlanVO.setLockChannel(FieldPlanLockChannels.QYD.getCode());
                        list.add(fieldPlanVO);
                    }
                }catch (Exception e){
                    log.error("firstFetchHandle失败{}",e);
                    throw new RuntimeException(e);
                }
            }
        }
        // 保存本次同步锁场的
        stringRedisTemplate.opsForValue().set(LAST_MSG_TIME + getPlatformPrefix() + "_"+ config.getStoreId(), String.valueOf(new Date().getTime() / 1000), 6, TimeUnit.DAYS);
        return list;
    }

    @Override
    public int getFieldBookDay() {
        // 客户端 只能预订7天的订场
        return 7;
    }

    /**
     * 封装header头
     * @param config
     * @param auth
     * @return
     */
    private HttpHeaders getHeaders(SyncDataAccountConfig config, String auth){
        HttpHeaders headers = new HttpHeaders();
        headers.set("TenantId", getPlatformConfig(config.getStoreId(), "platformStoreId") + "");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setOrigin(config.getPlatformUrl());
        headers.setBearerAuth(auth);
        return headers;
    }

    /**
     * 获取最新的订场数据
     * 获取趣运动通知消息订场消息，因为是分页的，所以需要循环分页获取
     * 根据上一次获取消息的最新时间，判断该本次获取的消息是否已经处理过
     * @param config
     * @param lastTime  上一次获取消息时间
     * @param auth 认证信息
     * @return
     */
    @Override
    public List<FieldPlanVO> fetchMsgFieldPlans(SyncDataAccountConfig config, Long lastTime, String auth){
        List<FieldPlanVO> pageList;
        List<FieldPlanVO> totalList = new ArrayList<>();
        int page = 1;
        int pageSize = 20;
        List<CommonVenue> fieldVenues = getVenueFields(config);
        do{
            pageList = fetchFieldPlansByPage(config, lastTime, page, pageSize, auth, fieldVenues);
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
     * 分页获取最新的订场消息数据
     * @param config
     * @param lastTime
     * @param page   页数，从1开始
     * @return
     */
    private List<FieldPlanVO> fetchFieldPlansByPage(SyncDataAccountConfig config, Long lastTime, int page, int pageSize, String auth, List<CommonVenue> fieldVenues) {
        List<FieldPlanVO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", pageSize);
        params.put("unread", 0);
        try {
            DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
            if (dynamicProxyIP == null) {
                log.error("dynamicProxyIP get null");
                return null;
            }
            String proxyIp = dynamicProxyIP.getIp();
            int proxyPort = dynamicProxyIP.getPort();
            // 获取趣运动的消息列表
            QydResponse<JSONObject> body = RestTemplateUtils.requestObjectPost(getProxyRestTemplate(), REQUEST_URL + "/saas/Message/list", JSONObject.toJSONString(params), proxyIp, proxyPort,
                    getHeaders(config, auth), new ParameterizedTypeReference<QydResponse<JSONObject>>(){});
            if (body == null) {
                log.error("获取消息失败");
                return null;
            }
            if (body.getStatus() != 0 || !"success".equals(body.getMsg())) {
                log.error("获取消息失败:{}", body.getMsg());
                return null;
            }
            JSONArray data = body.getData().getJSONArray("data");
            if (data == null || data.isEmpty()) {
                log.info("获取消息为空");
                return null;
            }
            log.info("订场消息:{}", JSONObject.toJSONString(data));
            Set<String> unmatchedFieldSet = new HashSet<>();
            Date now = new Date();
            for (int i = 0; i < data.size(); i++) {
                JSONObject msg = data.getJSONObject(i);
                String title = msg.getString("title");
                Long addTime = msg.getLong("add_time");
                //addTime < lastAddTime 说明获取消息已经处理过了
                //  2025-05-14 11:07
                if (lastTime != null &&  addTime <= lastTime) {
                    log.info("没有最新的订场消息了，上次同步时间:{}", lastTime);
                    break;
                }
                String content = msg.getString("content");
                String msgId = msg.getString("msg_id");

                //获取消息详情
                if (content.endsWith("...")){
                    content = queryQydContent(config, msgId, auth);
                }
                //解析趣运动消息
                QydMsgVo qydMsgVo = QydMsgVo.parseFromText(content);
                if (qydMsgVo == null) {
                    continue;
                }
                List<QydFieldPlan> qydFieldPlanList = qydMsgVo.getFieldPlanList();
                if (CollectionUtils.isEmpty(qydFieldPlanList)){
                    log.info("消息中没有场地信息");
                    continue;
                }
                qydFieldPlanList = qydFieldPlanList.stream().sorted((Comparator.comparing(QydFieldPlan::getStartTime)).reversed()).collect(Collectors.toList());

                for (int j = 0; j < qydFieldPlanList.size(); j++) {
                    QydFieldPlan qydFieldPlan = qydFieldPlanList.get(j);
                    Date fieldDay = DateUtils.parseDate(qydFieldPlan.getFieldDay() + " " + qydFieldPlan.getStartTime() + ":00", "yyyy-MM-dd HH:mm:ss");
                    //订场日期小于当前时间的消息，直接结束
                    if (fieldDay.compareTo(now) <= -1){
                        break;
                    }
                    if (unmatchedFieldSet.contains(qydFieldPlan.getVenueName() + qydFieldPlan.getFieldName())) {
                        continue;
                    }
                    Optional<CommonVenue> firstVenue = fieldVenues.stream().filter(item -> item.getAliasName().equals(qydFieldPlan.getVenueName())).findFirst();
                    if (!firstVenue.isPresent()) {
                        log.error("【{}】场馆，未匹配到门店", qydFieldPlan.getVenueName());
                        continue;
                    }
                    CommonVenue venue = firstVenue.get();
                    List<CommonField> fields = venue.getFields();
                    Optional<CommonField> firstField = fields.stream().filter(item -> item.getAliasName().equals(qydFieldPlan.getFieldName())).findFirst();
                    if (!firstField.isPresent()) {
                        log.error("【{}】场馆，【{}】场地，未匹配到门店", qydFieldPlan.getVenueName(), qydFieldPlan.getFieldName());
                        unmatchedFieldSet.add(qydFieldPlan.getVenueName() + qydFieldPlan.getFieldName());
                        continue;
                    }
                    CommonField field = firstField.get();
                    FieldPlanVO fieldPlanVO = new FieldPlanVO();
                    BeanUtils.copyProperties(qydFieldPlan, fieldPlanVO);
                    //执行锁场、解锁场地
                    if ("场地预订通知".equals(title)) {
                        fieldPlanVO.setStatus(FieldPlanStatus.Occupy.getCode());
                    } else if ("场地退款通知".equals(title)) {
                        fieldPlanVO.setStatus(FieldPlanStatus.Normal.getCode());
                    }
                    fieldPlanVO.setVenueId(venue.getId());
                    fieldPlanVO.setFieldId(field.getId());
                    fieldPlanVO.setFieldDay(DateUtils.parseDate(qydFieldPlan.getFieldDay(), "yyyy-MM-dd"));
                    fieldPlanVO.setStartTime(Time.valueOf(qydFieldPlan.getStartTime() + ":00"));
                    fieldPlanVO.setEndTime(Time.valueOf(qydFieldPlan.getEndTime() + ":00"));
                    //锁场、取消锁场，必须保持remark值一致
                    //取消锁场时，如果remark值一样，才允许取消，否则不允许取消，表示是不同平台的锁场
                    fieldPlanVO.setRemark(config.getPlatformName() + qydMsgVo.getPhoneNo());
                    //用于前端显示平台，后续可以优化到其他字段，需同步改造前端
                    fieldPlanVO.setLockRemark(config.getPlatformName());
                    fieldPlanVO.setLockChannel(FieldPlanLockChannels.QYD.getCode());
                    list.add(fieldPlanVO);
                }
            }
            //缓存最新的日期，下次获取的消息，根这个日期比较，
            //小于这个日期的，表示消息已经处理过了，忽略
            //大于这个日期的，则表示是新消息数据，需要处理。
            if (page == 1){
                String curSyncTime = data.getJSONObject(0).getString("add_time");
                log.info("店铺:{} 更新最新的订场同步时间:{}", config.getStoreId(), curSyncTime);
                // 取消息列表的第一条消息的时间 作为同步的最新时间
                stringRedisTemplate.opsForValue().set(LAST_MSG_TIME + getPlatformPrefix() + "_"+ config.getStoreId(), curSyncTime, 6, TimeUnit.DAYS);
            }
        }catch (Exception e){
            log.error("fetchFieldPlansByPage获取失败：{}",e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 秒数格式化为时分秒
     * @param totalSeconds
     * @return
     */
    public String getTimeStr(Long totalSeconds){
        // 计算小时数
        long hours = totalSeconds / 3600;
        // 计算剩余的秒数（扣除小时部分）
        long remainingSeconds = totalSeconds % 3600;
        // 计算分钟数
        long minutes = remainingSeconds / 60;
        // 计算剩余的秒数（扣除分钟部分）
        long seconds = remainingSeconds % 60;
        return  String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }

    /**
     * 时分秒换成秒数
     * 例如10:00:00转换为36000
     * @param time
     * @return
     */
    private int getSeconds(String time){
        if ("00:00:00".equals(time)){
            time = "24:00:00";
        }
        String[] split = time.split(":");
        return Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1]) * 60 +  Integer.parseInt(split[2]);
    }

    /**
     * 商铺
     */
    @Data
    class QydStore{
        int vc_id;
        String cat_name;
        List<QydVenue> venues;
    }

    /**
     * 锁场参数类
     */
    @Data
    class QydLockParam {

        /**
         * 场馆id
         */
        int categoryId;

        /**
         * 订场日期
         */
        Long currentDate;

        /**
         * 场地信息
         */
        List<QydLockItemParam> items;

    }

    /**
     * 锁场item参数类
     */
    @Data
    class QydLockItemParam{

        int courtId;  //场地id

        Integer beginTime;  //开始时间

        Integer endTime;    //结束时间
    }

    /**
     * 趣运动消息解析对象
     */
    @Data
    static class QydMsgVo {
        private String orderId;
        private String phoneNo;
        List<QydFieldPlan> fieldPlanList;
        public QydMsgVo(String orderId, String phoneNo,List<QydFieldPlan> fieldPlanList) {
            this.orderId = orderId;
            this.phoneNo = phoneNo;
            this.fieldPlanList = fieldPlanList;
        }
        // 静态方法，用于从给定的文本中解析并构建OrderVo对象
        public static QydMsgVo parseFromText(String text) {
            try {
                // 提取订单号
                String orderId = extractValue(text, "订单号：(.*?)，");
                // 提取手机号
                String phoneNo = extractValue(text, "手机尾号：(.*?)，");

                int indexOf1 = StringUtils.ordinalIndexOf(text, "，", 3);
                String fieldStr = text.substring(indexOf1 + 1,text.indexOf(";"));
                String[] fieldArr = fieldStr.split("、");
                List<QydFieldPlan> qydFieldPlanList = new ArrayList<>();
                for (int i = 0; i < fieldArr.length; i++) {
                    QydFieldPlan qydFieldPlan = new QydFieldPlan();
                    String[] fieldInfo = fieldArr[i].split("，");
                    // 提取场馆名
                    qydFieldPlan.setVenueName(fieldInfo[0]);
                    // 提取场地名
                    qydFieldPlan.setFieldName(fieldInfo[1]);
                    // 提取日期
                    qydFieldPlan.setFieldDay(fieldInfo[2]);
                    // 提取开始时间
                    qydFieldPlan.setStartTime(extractValue(fieldArr[i], "\\d{4}-\\d{2}-\\d{2}，(.*?)-"));
                    // 提取结束时间
                    qydFieldPlan.setEndTime(extractValue(fieldArr[i], "-(\\d{1,2}:\\d{2})"));
                    qydFieldPlan.setAmount(new BigDecimal(extractValue(fieldInfo[3], "(\\d+\\.\\d+)元")));

                    qydFieldPlanList.add(qydFieldPlan);
                }

                // 提取金额并转换为BigDecimal类型

                return new QydMsgVo(orderId, phoneNo, qydFieldPlanList);
            }catch (Exception e){
                log.error("消息转换失败{}",e.getMessage());
            }
            return null;
        }
        private static String extractValue(String text, String patternStr) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }
    }

}
