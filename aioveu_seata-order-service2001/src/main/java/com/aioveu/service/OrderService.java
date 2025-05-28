package main.java.com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import main.java.com.aioveu.entities.Order;

/**
 *@ClassName: $ {NAME}
 *@Author: 雒世松
 *@Date: 2025/5/29 3:44
 *@Param:
 *@Return:
 *@Description: TODO
 **/


public interface OrderService extends IService<Order>{

    /**
     * 创建订单
     */
    void create(Order order);

}

