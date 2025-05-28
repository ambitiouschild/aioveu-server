package com.aioveu;

import com.aioveu.resp.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 3:47
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@FeignClient(value = "seata-storage-service")
public interface StorageFeignApi {

    /**
     * 扣减库存
     */
    @PostMapping(value = "/storage/decrease")
    ResultData decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
