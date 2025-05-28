package com.aioveu;

import com.aioveu.resp.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 3:52
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@FeignClient(value = "seata-account-service")
public interface AccountFeignApi {

    //扣减账户余额
    @PostMapping("/account/decrease")
    ResultData decrease(@RequestParam("userId") Long userId, @RequestParam("money") Long money);
}
