package com.aioveu.common.annotation;

import com.aioveu.common.enums.LogModuleEnum;

import java.lang.annotation.*;

/**
 * @ClassName: annotation
 * @Description TODO  日志注解
 * @Author 可我不敌可爱
 * @Date 2025/12/12 21:52
 * @Version 1.0
 **/

/*真正原因：
        你的 @Log(value = "退款商品明细分页列表", module = LogModuleEnum.REFUND)注解
→ 触发了 AOP 切面执行
→ 切面中打印了包含端口信息的日志
→ IDEA 识别到了端口号
→ 在运行配置中显示了端口
        解决方案：
        在所有微服务中统一添加端口日志输出
        或者修改 @Log注解的切面，让它始终输出端口
        或者在启动类中明确输出端口信息*/


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Log {

    /*
     * @Author 可我不敌可爱
     * @Description //TODO  日志描述
     * @Date  2025/12/12 21:53
     * @Param []
     * @return java.lang.String
     **/
    String value() default "";

    /**
     * 日志模块
     *
     * @return 日志模块
     */

    LogModuleEnum module();

    /**
     * 是否记录请求参数
     *
     * @return 是否记录请求参数
     */
    boolean params() default true;

    /**
     * 是否记录响应结果
     * <br/>
     * 响应结果默认不记录，避免日志过大
     * @return 是否记录响应结果
     */
    boolean result() default false;
}
