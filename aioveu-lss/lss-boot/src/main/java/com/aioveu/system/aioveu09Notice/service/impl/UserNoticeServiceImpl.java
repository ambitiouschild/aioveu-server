package com.aioveu.system.aioveu09Notice.service.impl;

import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.system.aioveu02User.model.entity.UserNotice;
import com.aioveu.system.aioveu09Notice.mapper.UserNoticeMapper;
import com.aioveu.system.aioveu09Notice.model.query.NoticePageQuery;
import com.aioveu.system.aioveu09Notice.model.vo.NoticePageVO;
import com.aioveu.system.aioveu09Notice.model.vo.UserNoticePageVO;
import com.aioveu.system.aioveu09Notice.service.UserNoticeService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserNoticeServiceImpl
 * @Description TODO 用户公告状态服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:13
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class UserNoticeServiceImpl extends ServiceImpl<UserNoticeMapper, UserNotice> implements UserNoticeService {

    private final UserNoticeMapper userNoticeMapper;

    /**
     * 全部标记为已读
     *
     * @return 是否成功
     */
    @Override
    public boolean readAll() {
        Long userId = SecurityUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<UserNotice>()
                .eq(UserNotice::getUserId, userId)
                .eq(UserNotice::getIsRead, 0)
                .set(UserNotice::getIsRead, 1)
        );
    }

    /**
     * 我的通知公告分页列表
     *
     * @param page        分页对象
     * @param queryParams 查询参数
     * @return 通知公告分页列表
     */
    @Override
    public IPage<UserNoticePageVO> getMyNoticePage(Page<NoticePageVO> page, NoticePageQuery queryParams) {
        return this.getBaseMapper().getMyNoticePage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
    }
}
