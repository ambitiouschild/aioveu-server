package com.aioveu.pms.api;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.pms.model.query.PmsSpuQuery;
import com.aioveu.pms.model.vo.SpuPageVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @ClassName: PmsFeignClientWithoutConfig
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/24 15:06
 * @Version 1.0
 **/

@FeignClient(value = "aioveu-tenant-pms", contextId = "pmsWithoutConfig")
public interface PmsFeignClientWithoutConfig {

    /*
        * 当 Feign 客户端传递一个复杂对象参数（如 PmsSpuQuery）时，
        * 如果没有正确配置，Feign 可能会默认使用 POST 请求，并将对象放在请求体中。
        *
        * @SpringQueryMap的作用：
                •将对象的所有属性展开为 URL 查询参数
                •确保使用 GET 请求
                •例如：/pages?pageNum=1&pageSize=10&categoryId=123
        * */
    @Operation(summary = "商品分页列表")
    @GetMapping("/app-api/v1/spu/pages")  //确保 FeignClient 不返回 IPage
    PageResult<SpuPageVO> listPagedSpuForApp(
            @SpringQueryMap PmsSpuQuery queryParams,
            @RequestHeader("X-Tenant-Id") Long tenantId
    );
}
