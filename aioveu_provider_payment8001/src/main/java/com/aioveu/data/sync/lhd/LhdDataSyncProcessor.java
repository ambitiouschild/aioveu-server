package com.aioveu.data.sync.lhd;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.data.sync.FieldSyncHandleError;
import com.aioveu.data.sync.common.CommonField;
import com.aioveu.data.sync.common.CommonVenue;
import com.aioveu.data.sync.lhd.param.LhdFieldPlan;
import com.aioveu.data.sync.lhd.param.LhdLockItemParam;
import com.aioveu.data.sync.lhd.param.LhdLockParam;
import com.aioveu.data.sync.lhd.resp.LhdField;
import com.aioveu.data.sync.lhd.resp.LhdResponse;
import com.aioveu.data.sync.lhd.resp.LhdVenue;
import com.aioveu.data.sync.parent.AbstractDataSyncProcessor;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.SyncDataAccountConfig;
import com.aioveu.entity.VenueFieldSyncAlias;
import com.aioveu.entity.VenueSyncAlias;
import com.aioveu.enums.FieldPlanLockChannels;
import com.aioveu.enums.FieldPlanStatus;
import com.aioveu.exception.SportException;
import com.aioveu.request.RestTemplateUtils;
import com.aioveu.request.response.DynamicProxyIP;
import com.aioveu.utils.DateUtil;
import com.aioveu.utils.EncryptionUtil;
import com.aioveu.utils.RedisUtil;
import com.aioveu.vo.FieldPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 小程序订场数据同步到来沪动平台
 * @author: 雒世松
 * @date: 2025/4/21 15:54
 */
@Slf4j
@Component("lhdDataSyncProcessor")
public class LhdDataSyncProcessor extends AbstractDataSyncProcessor {

    private final String HOST_URI = "51yundong.me";

    private final String REQUEST_AUTH_DOMAIN = "auth." + HOST_URI;

    private final String REQUEST_URL = "https://tenant." + HOST_URI;

    private final String ORDER_REQUEST_URL = "https://order." + HOST_URI;

    private final String LHD_ORDER_CACHE_KEY = "LHD_ORDER_CACHE_";

    @Override
    public List<FieldSyncHandleError> syncFieldQs2Platform(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, String action) {
        log.info("趣数同步订场数据到来沪动锁场:{}", fieldPlanList.size());
        List<FieldSyncHandleError> errors = new ArrayList<>();
        // 先按场馆分组
        Map<String, List<FieldPlanDTO>> venueMap = fieldPlanList.stream().collect(Collectors.groupingBy(FieldPlanDTO::getVenueName));
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

    @Override
    public String getPlatformPrefix() {
        return "LHD";
    }

    @Override
    public List<FieldPlanVO> fetchMsgFieldPlans(SyncDataAccountConfig config, Long lastTime, String auth) {
        List<FieldPlanVO> pageList;
        List<FieldPlanVO> totalList = new ArrayList<>();
        int page = 1;
        int pageSize = 20;
        long pages = 1;
        String endDayStr;
        if (lastTime == null) {
            endDayStr = DateFormatUtils.format(DateUtils.addDays(new Date(), 6), "yyyy-MM-dd");
        } else {
            Date date = new Date(lastTime);
            endDayStr = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        do{
            IPage<FieldPlanVO> listIPage = fetchFieldPlansByPage(config, endDayStr, page, pageSize);
            pageList = listIPage.getRecords();
            pages = listIPage.getPages();
            page ++;
            if (CollectionUtils.isNotEmpty(pageList)){
                totalList.addAll(pageList);
            }
        }while (page <= pages);
        //反转元素
        Collections.reverse(totalList);
        return totalList;
    }

    /**
     * 分页获取最新的订场消息数据
     * @param config
     * @param endDayStr
     * @param page   页数，从1开始
     * @return
     */
    private IPage<FieldPlanVO> fetchFieldPlansByPage(SyncDataAccountConfig config, String endDayStr, int page, int pageSize) {
        IPage<FieldPlanVO> poolIPage = new Page<>(page, pageSize);
        List<VenueFieldSyncAlias> fieldVenues = venueFieldSyncAliasService.findByStoreIdAndPlatform(config.getStoreId(), config.getPlatformCode());
        if (CollectionUtils.isEmpty(fieldVenues)) {
            log.error("{}门店未获取到场地信息", config.getStoreId());
            return poolIPage;
        }

        String stadiumId = getPlatformStoreId(config.getStoreId());

        String auth = getAuthConfig(config);
        List<FieldPlanVO> list = new ArrayList<>();
        poolIPage.setRecords(list);
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        String beginTime = DateUtil.getDayByDate(new Date());
        params.put("orderTimeBing", beginTime);
        params.put("orderTimeEnd", endDayStr);
        params.put("orderType", 1);
        params.put("stadiumId", stadiumId);
        //运动时间类型
        params.put("timeType", 3);
//        if (beginTime.equals(endDayStr)){
//            params.put("timeType", 1);//订单时间类型
//        }
        try {
            HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(params), getHeaders(config, auth));
            ResponseEntity<String> venuesResponse = restTemplate.exchange(ORDER_REQUEST_URL + "/v3/orders/consume/report?"+HttpUtil.toParams(params), HttpMethod.GET, formEntity, String.class);
            LhdResponse<JSONObject> body = JSONObject.parseObject(venuesResponse.getBody(), LhdResponse.class);
            if (body == null) {
                log.error("获取订单失败");
                return poolIPage;
            }
            if (!body.responseSuccess()) {
                log.error("获取订单失败:{}", body.getMessage());
                return poolIPage;
            }
            //记录总数
            int totalCount = body.getData().getInteger("totalCount");
            poolIPage.setTotal(totalCount);

            JSONArray data = body.getData().getJSONArray("dataList");
            if (data == null || data.isEmpty()) {
                log.info("获取消息为空");
                return poolIPage;
            }
            Set<String> unmatchedFieldSet = new HashSet<>();

            Date now = new Date();

            for (int i = 0; i < data.size(); i++) {
                JSONObject orderMsg = data.getJSONObject(i);
                //支付状态，为0表示未支付
                int orderPayType = orderMsg.getInteger("orderPayType");
                if (orderPayType == 0){
                    continue;
                }
                //订单状态,3 已支付，5 部分退款，6全额退款，7已完成
                int orderStatus = orderMsg.getInteger("orderStatus");
                if (orderStatus != 3
                        && orderStatus != 5
                        && orderStatus != 6
                        && orderStatus != 7) {
                    continue;
                }
                //执行锁场、解锁场地
                int status = FieldPlanStatus.Normal.getCode();
                if (orderStatus == 3 || orderStatus == 7) {
                    status = FieldPlanStatus.Occupy.getCode();
                }

                List<LhdFieldPlan> orderField = orderMsg.getJSONArray("orderField").toJavaList(LhdFieldPlan.class);

                if (CollectionUtils.isEmpty(orderField)){
                    log.info("订单中没有场地信息");
                    continue;
                }
                String lhdOrderNo = orderMsg.getString("orderNo");

                //判断订单是否已经处理过，通过缓存订单号和状态
                //状态一致，就不处理
                //不一致，表示新数据，需要处理，并添加缓存，时间24小时
                Date fieldDate = DateUtil.getDayDateByDateHHMM(orderMsg.getString("resourceDate"));
//                String key = LHD_ORDER_CACHE_KEY + config.getPlatformStoreId()+":"+ DateUtil.getDayByString(fieldDate);
//                Object value = redisTemplate.hGet(key, lhdOrderNo);
//                if (value != null && Integer.valueOf(value.toString()) == status){
//                    continue;
//                } else{
//                    redisTemplate.hSet(key, lhdOrderNo, status);
//                    //计算订场日期与今天的间隔天数，按照 天数+1 缓存到redis中
//                    int distanceOfTwoDate = DateUtil.getDistanceOfTwoDate(DateUtil.getDayByString(beginTime), fieldDate);
//                    redisTemplate.expire(key,   (distanceOfTwoDate + 1) * 24 * 60 * 60);
//                }

                for (int j = 0; j < orderField.size(); j++) {
                    LhdFieldPlan lhdFieldPlan = orderField.get(j);
                    Date fieldDay = DateUtil.getDayDateByDateHHMM(lhdFieldPlan.getResourceDate() + " " + lhdFieldPlan.getBeginTime() + ":00");
                    //订场日期小于当前时间的消息，直接结束
                    if (fieldDay.compareTo(now) <= -1){
                        continue;
                    }
                    if (unmatchedFieldSet.contains(lhdFieldPlan.getStadiumItemName() + lhdFieldPlan.getFieldName())) {
                        continue;
                    }
                    VenueFieldSyncAlias venueFieldVO = fieldVenues.stream().filter(item -> item.getAliasName().equals(lhdFieldPlan.getFieldName()) && item.getVenueAliasName().equals(lhdFieldPlan.getStadiumItemName())).findFirst().orElse(null);
                    if (venueFieldVO == null) {
                        log.error("【{}】场馆，【{}】场地，未匹配到门店", lhdFieldPlan.getStadiumItemName(), lhdFieldPlan.getFieldName());
                        unmatchedFieldSet.add(lhdFieldPlan.getStadiumItemName() + lhdFieldPlan.getFieldName());
                        continue;
                    }
                    FieldPlanVO fieldPlanVO = new FieldPlanVO();
                    BeanUtils.copyProperties(lhdFieldPlan, fieldPlanVO);
                    fieldPlanVO.setStatus(status);
                    fieldPlanVO.setVenueId(venueFieldVO.getVenueId());
                    fieldPlanVO.setFieldId(venueFieldVO.getFieldId());
                    fieldPlanVO.setFieldDay(DateUtil.getDayByString(lhdFieldPlan.getResourceDate()));
                    fieldPlanVO.setStartTime(Time.valueOf(lhdFieldPlan.getBeginTime() + ":00"));
                    fieldPlanVO.setEndTime(Time.valueOf(lhdFieldPlan.getEndTime() + ":00"));
                    String phone = orderMsg.getString("userPhone");
                    fieldPlanVO.setRemark(config.getPlatformName() + phone.substring(phone.length()-4));
                    //用于前端显示平台，后续可以优化到其他字段，需同步改造前端
                    fieldPlanVO.setLockRemark(config.getPlatformName());
                    fieldPlanVO.setLockChannel(FieldPlanLockChannels.LHD.getCode());
                    list.add(fieldPlanVO);
                }
            }
        }catch (Exception e){
            log.error("fetchFieldPlansByPage获取失败：{}",e);
            throw new SportException("分页获取来沪动的订单失败");
        }
        return poolIPage;
    }

    /**
     * 获取平台店铺id
     * @param storeId
     * @return
     */
    private String getPlatformStoreId(Long storeId) {
        return getPlatformConfig(storeId, "stadiumId") + "";
    }

    @Override
    public List<CommonVenue> requestVenue(SyncDataAccountConfig config, String key, List<VenueSyncAlias> storeVenueList, Map<Long, List<VenueFieldSyncAlias>> venueFieldMap) {
        Map<String, Object> venusParam = new HashMap<>();
        venusParam.put("bookMode", 1);
        HttpEntity<String> formEntity = new HttpEntity<>(getHeaders(config, null));
        ResponseEntity<String> venuesResponse = restTemplate.exchange(REQUEST_URL + "/v3/stadiumitem/tenants/stadiums/" + getPlatformStoreId(config.getStoreId()) + "/stadium_items?" + HttpUtil.toParams(venusParam), HttpMethod.GET, formEntity, String.class);
        LhdResponse<JSONObject> body = JSONObject.parseObject(venuesResponse.getBody(), LhdResponse.class);
        if (body == null) {
            log.error("获取场馆失败");
            return null;
        }
        if (!body.responseSuccess()) {
            log.error("获取场馆失败:{}", body.getMessage());
            return null;
        }
        List<LhdVenue> lhdVenueList = body.getData().getJSONArray("dataList").toJavaList(LhdVenue.class);
        List<CommonVenue> venueList = new ArrayList<>(lhdVenueList.size());
        for (LhdVenue lhdVenue : lhdVenueList) {
            Optional<VenueSyncAlias> first = storeVenueList.stream().filter(item -> item.getAliasName().equals(lhdVenue.getStadiumName())).findFirst();
            if (!first.isPresent()) {
                log.error("趣运动:{}场馆未匹配到，忽略该场馆的订场同步", lhdVenue.getStadiumName());
                continue;
            }

            VenueSyncAlias venueSyncAlias = first.get();

            CommonVenue venue = new CommonVenue();
            venue.setAliasId(lhdVenue.getId());
            venue.setAliasName(lhdVenue.getStadiumItemName());
            venue.setId(venueSyncAlias.getVenueId());
            venue.setName(venueSyncAlias.getVenueName());

            Map<String, Object> filedParams = new HashMap<>();
            filedParams.put("pageNo", 1);
            filedParams.put("pageSize", 20);
            filedParams.put("stadiumItemId", lhdVenue.getId());
            formEntity = new HttpEntity<>(getHeaders(config, null));
            ResponseEntity<String> fieldsResponse = restTemplate.exchange(REQUEST_URL + "/v3/fields?" + HttpUtil.toParams(filedParams), HttpMethod.GET, formEntity, String.class);
            LhdResponse<JSONObject> fieldBody = JSONObject.parseObject(fieldsResponse.getBody(), LhdResponse.class);
            if (fieldBody == null) {
                log.error("获取场地为空");
            }else if (!fieldBody.responseSuccess()) {
                log.error("获取场地为空:{}", fieldBody.getMessage());
            }else {
                List<LhdField> lhdFields = fieldBody.getData().getJSONArray("dataList").toJavaList(LhdField.class);

                List<VenueFieldSyncAlias> fieldList = venueFieldMap.get(venueSyncAlias.getVenueId());

                List<CommonField> fields = new ArrayList<>(lhdFields.size());
                // 场地别名 匹配第三方场地
                for (LhdField lhdField : lhdFields) {
                    Optional<VenueFieldSyncAlias> fieldFirst = fieldList.stream().filter(item -> item.getAliasName().equals(lhdField.getFieldName())).findFirst();
                    if (!fieldFirst.isPresent()) {
                        log.error("来沪动:{}场地未匹配到，忽略该场馆的场地订场同步", lhdField.getFieldName());
                        continue;
                    }
                    VenueFieldSyncAlias venueFieldSyncAlias = fieldFirst.get();
                    CommonField field = new CommonField();
                    field.setAliasId(lhdField.getId());
                    field.setAliasName(lhdField.getFieldName());
                    field.setId(venueFieldSyncAlias.getFieldId());
                    field.setName(venueFieldSyncAlias.getFieldName());
                    fields.add(field);
                }
                if (CollectionUtils.isNotEmpty(fields)) {
                    venue.setFields(fields);
                    venueList.add(venue);
                } else {
                    log.error("来沪动场馆:{}未匹配到任何场地, 同步忽略", lhdVenue.getStadiumItemName());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(venueList)){
            RedisUtil.setList(key, venueList, redisTemplate);
        } else {
            throw new SportException("来沪动未匹配到任何场馆");
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
        savePlatformConfig(config.getStoreId(), "stadiumId", platformStoreId);
        return true;
    }

    private String getPlatformStoreId(SyncDataAccountConfig config) {
        String auth = getAuthConfig(config);
        HttpEntity<String> formEntity = new HttpEntity<>(getHeaders(config, auth));
        ResponseEntity<String> responseEntity = restTemplate.exchange(REQUEST_URL + "/v3/stadium/tenants/stadiums", HttpMethod.GET, formEntity, String.class);
        LhdResponse<JSONObject> body = JSONObject.parseObject(responseEntity.getBody(), LhdResponse.class);
        if (body == null){
            log.error("获取店铺失败");
            return null;
        }
        if (!body.responseSuccess() || body.getData().getIntValue("totalCount") == 0){
            log.error("获取店铺信息失败:{}",body.getMessage());
            return null;
        }
        JSONArray dataList = body.getData().getJSONArray("dataList");

        for (int i = 0; i < dataList.size(); i++) {
            JSONObject store = dataList.getJSONObject(i);
            String storeName = store.getString("stadiumName");
            if ("false".equals(store.getString("disabled")) && config.getPlatformStoreName().equals(storeName)){
                return store.getString("id");
            }
        }
        return null;
    }

    /**
     * 封装header头
     * @param config
     * @return
     */
    public HttpHeaders getHeaders(SyncDataAccountConfig config, String auth){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setOrigin(config.getPlatformUrl());
        headers.setBearerAuth(auth);
        return headers;
    }

    @Override
    public List<FieldPlanVO> fullSyncFieldPlatform2Qs(SyncDataAccountConfig config) {
        return Collections.emptyList();
    }

    @Override
    public int getFieldBookDay() {
        return 7;
    }

    @Override
    public String login(SyncDataAccountConfig config) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","b10d5084f75a4fd7a070d0667f91b151");
        params.add("client_secret","e2bb72c7e2794781865be949064afd8e");
        params.add("grant_type","password");
        params.add("isSecret","true");
        params.add("source","backend");
        params.add("username",config.getPlatformUsername());
        params.add("password", EncryptionUtil.encodeBase64(EncryptionUtil.decryptWithAES(config.getPlatformPassword(), EncryptionUtil.AES_KEY)));
        String url = "https://"+ REQUEST_AUTH_DOMAIN +"/v3/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Host", REQUEST_AUTH_DOMAIN);

        DynamicProxyIP dynamicProxyIP = getDynamicProxyIP(config.getStoreId());
        if (dynamicProxyIP == null) {
            log.error("dynamicProxyIP get null");
            return null;
        }
        String proxyIp = dynamicProxyIP.getIp();
        int proxyPort = dynamicProxyIP.getPort();
        String response = RestTemplateUtils.requestFormPost(getProxyRestTemplate(), url, params, proxyIp, proxyPort, headers, new ParameterizedTypeReference<String>(){});
        LhdResponse<JSONObject> body = JSONObject.parseObject(response, LhdResponse.class);
        if (body == null) {
            log.error("{}登录失败:未获取到返回信息", config.getPlatformUsername());
            return null;
        }
        if (!body.responseSuccess()){
            log.error("{}登录失败:{}", config.getPlatformUsername(), body.getMessage());
            return null;
        }
        JSONObject data = body.getData();
        String accessToken = data.getString("access_token");
        if (StringUtils.isNotBlank(accessToken)){
            saveAuthConfig(accessToken, config.getStoreId(), 23 * 60 * 60);
            return accessToken;
        }
        return null;
    }

    /**
     * 场地处理，解锁或加锁
     * @param fieldPlanList
     * @param config
     * @param handleType
     * @return
     */
    private void fieldHandle(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, String handleType){
        FieldPlanDTO fieldPlanDTO = fieldPlanList.get(0);
        CommonVenue lhdVenue = getVenueByName(fieldPlanDTO.getVenueName(), config);
        if (lhdVenue == null){
            log.error("未获取到对应场馆：{}", fieldPlanDTO.getVenueName());
            throw new SportException("未获取到对应场馆:" + fieldPlanDTO.getVenueName());
        }
        if (LOCK.equals(handleType)) {
            fieldLockHandle(fieldPlanList, config, lhdVenue, fieldPlanDTO);
        } else {
            fieldUnLockHandle(fieldPlanList, config, lhdVenue, fieldPlanDTO);
        }
    }

    /**
     * 批量锁场
     * @param fieldPlanList
     * @param config
     * @param lhdVenue
     * @param fieldPlanDTO
     * @return
     */
    private void fieldLockHandle(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, CommonVenue lhdVenue, FieldPlanDTO fieldPlanDTO) {
        LhdLockParam param = new LhdLockParam();
        //场馆id
        param.setStadiumItemId(lhdVenue.getAliasId());
        List<LhdLockItemParam> array = new ArrayList<>(fieldPlanList.size());
        for (FieldPlanDTO item : fieldPlanList) {
            CommonField lhdField = getFieldByName(lhdVenue.getFields(), item.getFieldName());
            if (lhdField == null){
                log.error("未获取到对应场地：{},{}", lhdVenue.getName(), item.getFieldName());
                throw new SportException("未获取到对应场地");
            }
            LhdLockItemParam itemParam = new LhdLockItemParam();
            itemParam.setFieldId(lhdField.getAliasId());
            itemParam.setFieldName(lhdField.getName());
            itemParam.setBeginTime(getTimeByConfig(config.getPlatformHalfHour(), item.getStartTime(), true).toString().substring(0,5));
            itemParam.setEndTime(getTimeByConfig(config.getPlatformHalfHour(), item.getEndTime(), false).toString().substring(0,5));
            String lockDateStr = DateUtil.getDayByDate(item.getFieldDay());
            itemParam.setLockDate(lockDateStr);
            array.add(itemParam);
        }
        param.setLockFieldRecords(array);

        String paramStr = JSONObject.toJSONString(param);
        log.info("来沪动lock,{},{},{}-{},入参：{}",fieldPlanDTO.getVenueName(),fieldPlanDTO.getFieldName(), fieldPlanDTO.getStartTime().toString(),fieldPlanDTO.getEndTime().toString(),paramStr);
        HttpEntity<String> formEntity = new HttpEntity<>( paramStr, getHeaders(config, null));
        ResponseEntity<String> venuesResponse = restTemplate.postForEntity(REQUEST_URL + "/v3/lock/field/rules/locks", formEntity, String.class);
        LhdResponse<Boolean> body = JSONObject.parseObject(venuesResponse.getBody(), LhdResponse.class);
        if (body == null){
            log.error("锁场失败");
            throw new SportException("锁场失败, 未返回请求内容");
        }
        if (!body.responseSuccess() ){
            log.error("锁场失败,原因:{}", body.getMessage());
            throw new SportException(body.getMessage());
        }
        log.info("锁场成功");
    }

    /**
     * 批量解锁场地
     * @param fieldPlanList
     * @param config
     * @param lhdVenue
     * @param fieldPlanDTO
     * @return
     */
    private void fieldUnLockHandle(List<FieldPlanDTO> fieldPlanList, SyncDataAccountConfig config, CommonVenue lhdVenue, FieldPlanDTO fieldPlanDTO) {
        try {
            // 订场按场地进行分组
            Map<String, List<FieldPlanDTO>> fieldGroup = fieldPlanList.stream().collect(Collectors.groupingBy(FieldPlanDTO::getFieldName));

            String lockDateStr = DateUtil.getDayByDate(fieldPlanDTO.getFieldDay());
            HttpEntity<String> formEntity = new HttpEntity<>(getHeaders(config, null));
            Map<String,Object> param = new HashMap<>();
            param.put("resourceDate", lockDateStr);
            param.put("stadiumItemId", lhdVenue.getAliasId());
            ResponseEntity<String> lockResponse = restTemplate.exchange(REQUEST_URL + "/v3/field_holdon_rule/resources/new?"+HttpUtil.toParams(param) , HttpMethod.GET, formEntity, String.class);
            LhdResponse<JSONObject> lockBody = JSONObject.parseObject(lockResponse.getBody(), LhdResponse.class);
            if (lockBody == null) {
                log.error("获取锁场数据失败");
                throw new SportException("获取锁场数据失败");
            }
            if (!lockBody.responseSuccess()){
                log.error("获取锁场数据失败,原因:{}", lockBody.getMessage());
                throw new SportException(lockBody.getMessage());
            }
            Map<String, String> lockFieldMap = new HashMap<>();
            for (String fieldName : fieldGroup.keySet()) {
                CommonField lhdField = getFieldByName(lhdVenue.getFields(), fieldName);
                if (lhdField == null){
                    log.error("未获取到对应场地：{},{}", lhdVenue.getName(), fieldName);
                    continue;
                }
                lockFieldMap.put(lhdField.getAliasId(), fieldName);
            }
            JSONArray orderList = lockBody.getData().getJSONArray("orderList");
            Map<String, FieldPlanDTO> unLockFieldIdMap = new HashMap<>();
            //获取场地id
            for (int i = 0; i < orderList.size(); i++) {
                JSONObject jsonObject = orderList.getJSONObject(i);
                // 场地匹配
                if (lockFieldMap.get(jsonObject.getString("fieldId")) != null){
                    // 来沪动的订单锁场信息
                    List<FieldPlanDTO> fieldPlanDTOList = fieldGroup.get(lockFieldMap.get(jsonObject.getString("fieldId")));
                    JSONArray lockInfos = jsonObject.getJSONArray("lockInfo");
                    for (int j = 0; j < lockInfos.size(); j++) {
                        JSONObject lockInfo = lockInfos.getJSONObject(j);
                        for (FieldPlanDTO item : fieldPlanDTOList) {
                            if(item.getStartTime().toString().startsWith(lockInfo.getString("beginTime"))
                                    && item.getEndTime().toString().startsWith(lockInfo.getString("endTime"))){
                                unLockFieldIdMap.put(lockInfo.getString("fieldHoldonRuleItemId"), item);
                                break;
                            }
                        }
                    }
                }
            }
            if (unLockFieldIdMap.isEmpty()){
                log.error("未匹配到锁场id");
                throw new SportException("未匹配到锁场id");
            }
            for (Map.Entry<String, FieldPlanDTO> entry : unLockFieldIdMap.entrySet()){
                FieldPlanDTO item = entry.getValue();
                log.info("来沪动unlock,{},{},时间：{} {}-{},入参：{}", item.getVenueName(), item.getFieldName(), lockDateStr, item.getStartTime().toString(), item.getEndTime().toString(), entry.getKey());
                ResponseEntity<String> venuesResponse = restTemplate.exchange(REQUEST_URL + "/v3/lock/field/rules/unlocks/" + entry.getKey(), HttpMethod.DELETE, formEntity, String.class);
                LhdResponse<Boolean> body = JSONObject.parseObject(venuesResponse.getBody(), LhdResponse.class);
                if (body == null) {
                    log.error("解锁失败");
                    continue;
                }
                if (!body.responseSuccess()) {
                    log.error("解锁失败,原因:{}", body.getMessage());
                }
                log.info("解锁成功");
            }
        } catch (Exception e){
            throw new SportException("解锁失败:"+e.getMessage());
        }
    }
}
