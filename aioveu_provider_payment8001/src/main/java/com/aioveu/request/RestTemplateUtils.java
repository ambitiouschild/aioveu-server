package com.aioveu.request;

import com.aioveu.request.handler.CustomErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 21:05
 */
@Slf4j
public class RestTemplateUtils {

    public static <T> T simpleGet(RestTemplate restTemplate, String url, ParameterizedTypeReference<T> reference) {
        HttpHeaders headers = new HttpHeaders();
        return request(restTemplate, URI.create(url), HttpMethod.GET, new HttpEntity<>(headers), null, 0, reference);
    }

    public static <T> T simpleGet(RestTemplate restTemplate, String url, HttpHeaders headers, String proxyIp, int proxyPort, ParameterizedTypeReference<T> reference) {
        return request(restTemplate, URI.create(url), HttpMethod.GET, new HttpEntity<>(headers), proxyIp, proxyPort, reference);
    }

    public static <T> T simpleGet(RestTemplate restTemplate, URI uri, String proxyIp, int proxyPort, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> reference) {
        if (requestEntity == null) {
            HttpHeaders headers = new HttpHeaders();
            requestEntity = new HttpEntity<>(headers);
        }
        return request(restTemplate, uri, HttpMethod.GET, requestEntity, proxyIp, proxyPort, reference);
    }

    public static <T> T requestFormGet(RestTemplate restTemplate, String url, MultiValueMap<String, String> params, ParameterizedTypeReference<T> reference){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, null, 0, reference);
    }

    public static <T> T requestObjectPost(RestTemplate restTemplate, String url, Map<String, Object> params, ParameterizedTypeReference<T> reference){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, null, 0,reference);
    }

    public static <T> T requestObjectPost(RestTemplate restTemplate, String url, String params, ParameterizedTypeReference<T> reference){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, null, 0,reference);
    }

    public static <T> T requestObjectPost(RestTemplate restTemplate, String url, String params, String proxyIp, int proxyPort, HttpHeaders headers, ParameterizedTypeReference<T> reference){
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, null, 0,reference);
    }

    public static <T> T requestFormPost(RestTemplate restTemplate, URI uri, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> reference){
        return request(restTemplate, uri, HttpMethod.POST, requestEntity, null, 0, reference);
    }


    /**
     * POST代理请求
     * @param restTemplate
     * @param url
     * @param params
     * @param proxyIp
     * @param proxyPort
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T requestFormPost(RestTemplate restTemplate, String url, MultiValueMap<String, String> params, String proxyIp, int proxyPort, ParameterizedTypeReference<T> reference){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, proxyIp, proxyPort, reference);
    }

    /**
     * POST代理请求
     * @param restTemplate
     * @param url
     * @param params
     * @param proxyIp
     * @param proxyPort
     * @param headers
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T requestFormPost(RestTemplate restTemplate, String url, MultiValueMap<String, String> params, String proxyIp, int proxyPort, HttpHeaders headers, ParameterizedTypeReference<T> reference){
        HttpEntity<?> requestEntity = new HttpEntity<>(params, headers);
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, proxyIp, proxyPort, reference);
    }

    /**
     * POST代理请求
     * @param restTemplate
     * @param url
     * @param requestEntity
     * @param proxyIp
     * @param proxyPort
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T requestFormPost(RestTemplate restTemplate, String url, HttpEntity<?> requestEntity, String proxyIp, int proxyPort, ParameterizedTypeReference<T> reference){
        return request(restTemplate, URI.create(url), HttpMethod.POST, requestEntity, proxyIp, proxyPort, reference);
    }

    public static <T> T request(RestTemplate restTemplate, URI url, HttpMethod httpMethod, HttpEntity<?> requestEntity, String proxyIp, int proxyPort, ParameterizedTypeReference<T> reference) {
        log.debug("request url:" + url);
        log.debug("request body params:" + requestEntity.getBody());
        restTemplate.setErrorHandler(new CustomErrorHandler());
        try {
            if (proxyIp != null) {
                // 设置代理
                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
                requestFactory.setProxy(proxy);
                // 将 RequestFactory 设置到 RestTemplate
                restTemplate.setRequestFactory(requestFactory);
            }
            ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, requestEntity, reference);
            if (response.getStatusCode()== HttpStatus.OK){
                return response.getBody();
            } else {
                log.error("code:" + response.getStatusCode() + " message:" + response.getBody().toString());
            }
        }catch (Exception e) {
            log.error("服务异常:"+e);
        }
        return null;
    }

    public static InputStream downFile(String src, RestTemplate restTemplate) throws IOException {
        return downFile(URI.create(src), restTemplate);
    }

    /**
     * 从网络上下载文件
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream downFile(URI url, RestTemplate restTemplate) throws IOException {
        ResponseEntity<Resource> resourceResponseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Resource.class);
        if (resourceResponseEntity.getStatusCode()== HttpStatus.OK){
            return resourceResponseEntity.getBody().getInputStream();
        }else {
            throw new FileNotFoundException();
        }
    }
}
