package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.MessageReceiverDao;
import com.aioveu.entity.MessageConfig;
import com.aioveu.entity.MessageReceiver;
import com.aioveu.entity.User;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.NoticeCodeEnum;
import com.aioveu.exception.SportException;
import com.aioveu.form.MessageReceiverForm;
import com.aioveu.service.MessageConfigService;
import com.aioveu.service.MessageReceiverService;
import com.aioveu.service.UserMpSubscribeService;
import com.aioveu.service.UserService;
import com.aioveu.utils.StringValidatorUtils;
import com.aioveu.vo.MessageReceiverVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class MessageReceiverServiceImpl extends ServiceImpl<MessageReceiverDao, MessageReceiver> implements MessageReceiverService {

    @Autowired
    private UserMpSubscribeService userMpSubscribeService;

    @Autowired
    UserService userService;

    @Autowired
    MessageConfigService messageConfigService;

    @Override
    public List<MessageReceiverVO> getVoList(Long storeId, Long msgConfigId) {
        LambdaQueryWrapper<MessageReceiver> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageReceiver::getStoreId, storeId)
                .eq(MessageReceiver::getMsgConfigId, msgConfigId)
                .eq(MessageReceiver::getStatus,  DataStatus.NORMAL.getCode())
                .orderByDesc(MessageReceiver::getCreateDate);
        List<MessageReceiver> list = list(queryWrapper);
        List<MessageReceiverVO> receiveVOS = new ArrayList<>();
        if (list != null && list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                MessageReceiverVO vo = new MessageReceiverVO();
                BeanUtils.copyProperties(list.get(i),vo);
                receiveVOS.add(vo);
            }
        }
        return receiveVOS;
    }

    @Override
    public List<MessageReceiver> getList(Long storeId, Long msgConfigId) {
        LambdaQueryWrapper<MessageReceiver> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageReceiver::getStoreId, storeId)
                .eq(MessageReceiver::getMsgConfigId, msgConfigId)
                .eq(MessageReceiver::getStatus,  DataStatus.NORMAL.getCode())
                .orderByDesc(MessageReceiver::getCreateDate);
        List<MessageReceiver> list = list(queryWrapper);
        return list;
    }



    @Override
    public Boolean updateStatus(Long id, Integer status) {
        LambdaUpdateWrapper<MessageReceiver> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MessageReceiver::getStatus, status)
                .set(MessageReceiver::getUpdateUserId, OauthUtils.getCurrentUserId())
                .eq(MessageReceiver::getId, id);
        return update(wrapper);
    }

    @Override
    public Boolean add(MessageReceiverForm form) {
        form = verifyParams(form);
        MessageReceiver entity = new MessageReceiver();
        entity.setCreateUserId(OauthUtils.getCurrentUserId());
        BeanUtils.copyProperties(form, entity);
        this.baseMapper.insert(entity);
        return true;
    }

    /**
     * 校验数据有效性
     * 1、短信类型，校验号码
     * 2、服务号、小程序类型，校验openid
     * 校验数据是否已存在
     * @param form
     * @return
     */
    public MessageReceiverForm verifyParams(MessageReceiverForm form){
        if (StringUtils.isBlank(form.getPhone())){
            throw new SportException("缺少必填参数");
        }
        if (!StringValidatorUtils.isValidMobileNumber(form.getPhone().trim())) {
            throw new SportException("号码输入格式不正确");
        }
        form.setPhone(form.getPhone().trim());

        MessageConfig messageConfig = messageConfigService.getById(form.getMsgConfigId());
        if (messageConfig == null){
            throw new SportException("缺少必填参数");
        }
        if (messageConfig.getCanAddReceiver() !=  DataStatus.NORMAL.getCode()){
            throw new SportException("不可添加接受人");
        }

        LambdaQueryWrapper<MessageReceiver> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageReceiver::getStatus, DataStatus.NORMAL.getCode())
                .eq(MessageReceiver::getStoreId, form.getStoreId())
                .eq(MessageReceiver::getMsgConfigId, form.getMsgConfigId())
                .eq(MessageReceiver::getPhone, form.getPhone());

        List<MessageReceiver> list = list(queryWrapper);
        if (list != null && list.size() > 0){
            throw new SportException("该号码已存在，请勿重复提交");
        }
        User user = userService.getByUserPhone(form.getPhone());
        if (user != null){
            form.setName(user.getName());
            form.setUserId(user.getId());
        }

        if (NoticeCodeEnum.wechat_mp.getCode().equals(form.getNoticeCode()) || NoticeCodeEnum.wechat_mini_app.getCode().equals(form.getNoticeCode())){
            if (user == null){
                throw new SportException("用户未订阅");
            }
            if (messageConfig.getConfig() == null || messageConfig.getConfig().get("appId") == null){
                throw new SportException("消息模版配置不对");
            }
            Object appId = messageConfig.getConfig().get("appId");
            String openId = userMpSubscribeService.getOpenIdByUserIdAndAppId(user.getId(), appId.toString());
            if (StringUtils.isBlank(openId)){
                throw new SportException("用户未订阅");
            }
            form.setOpenId(openId);
        }
        return form;
    }
}
