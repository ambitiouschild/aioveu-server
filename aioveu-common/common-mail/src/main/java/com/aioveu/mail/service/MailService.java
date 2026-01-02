package com.aioveu.mail.service;

import org.springframework.stereotype.Service;

/**
  *@ClassName: MailService
  *@Description TODO  邮件服务接口层
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/15 16:46
  *@Version 1.0
  **/

/*即使你添加了 mail 依赖，但 Spring 仍然找不到 MailServicebean，可能有以下原因：
包扫描问题：mail 服务的包没有被扫描
Bean 定义问题：MailService 没有被正确标记为 @Service
版本问题：依赖版本不匹配
主启动类扫描范围：没有扫描 mail 包*/

// 必须要有 @Service 注解
public interface MailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param text    邮件内容
     */
    void sendMail(String to, String subject, String text) ;

    /**
     * 发送带附件的邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param text    邮件内容
     * @param filePath 附件路径
     */
    void sendMailWithAttachment(String to, String subject, String text, String filePath);

}
