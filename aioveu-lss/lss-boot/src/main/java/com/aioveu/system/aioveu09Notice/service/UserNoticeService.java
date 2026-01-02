package com.aioveu.system.aioveu09Notice.service;

import com.aioveu.system.aioveu02User.model.entity.UserNotice;
import com.aioveu.system.aioveu09Notice.model.query.NoticePageQuery;
import com.aioveu.system.aioveu09Notice.model.vo.NoticePageVO;
import com.aioveu.system.aioveu09Notice.model.vo.UserNoticePageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
  *@ClassName: UserNoticeService
  *@Description TODO  用户公告状态服务类
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/31 17:21
  *@Version 1.0
  **/
public interface UserNoticeService extends IService<UserNotice> {

    /**
     * 全部标记为已读
     *
     * @return 是否成功
     */
    boolean readAll();

    /**
     * 分页获取我的通知公告
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return 我的通知公告分页列表
     */
    IPage<UserNoticePageVO> getMyNoticePage(Page<NoticePageVO> page, NoticePageQuery queryParams);
}
