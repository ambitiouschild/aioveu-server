package com.aioveu.system.handler;

import com.aioveu.common.result.Result;
import com.aioveu.system.model.vo.UserInfoVO;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description: TODO 用户接口降级逻辑
 * @Author: 雒世松
 * @Date: 2025/6/5 17:14
 * @param
 * @return:
 **/

@Slf4j
public class UserBlockHandler {

    /**
     * 获取当前登录用户信息的熔断降级处理
     * @param blockException
     * @return
     */
    public static Result<UserInfoVO> handleGetCurrentUserBlock(BlockException blockException) {
        return Result.success(new UserInfoVO());
    }


    public static  Result handleGetUserByUsernameBlock(String username,BlockException blockException){
        log.info("降级了：{}",username);
        return Result.failed("降级了");
    }
}
