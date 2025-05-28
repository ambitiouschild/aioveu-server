package main.java.com.aioveu.controller;

import com.aioveu.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import main.java.com.aioveu.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 3:45
 * @Param:
 * @Return:
 * @Description: TODO
 **/
@Slf4j
@RequestMapping("/api/v1/agreement")
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     */
    @GetMapping("/order/create")
    public ResultData create(Order order)
    {
        orderService.create(order);
        return ResultData.success(order);
    }
}
