package com.aioveu.common.rabbitmq.producer.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/**
 * @ClassName: JsonUtils
 * @Description TODO JsonUtils是一个自定义的工具类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 15:27
 * @Version 1.0
 **/
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String toJson(Object obj) {
        if (obj == null) {
            return "{}";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败, class={}", obj.getClass().getName(), e);
            return "{}"; // ✅ 绝不能返回 null
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败, json={}", json, e);
            return null;
        }
    }
}