package com.aioveu.system.aioveu13Mail.service;

/**
 * @ClassName: MailService
 * @Description TODO  邮件服务接口层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 21:00
 * @Version 1.0
 **/
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
