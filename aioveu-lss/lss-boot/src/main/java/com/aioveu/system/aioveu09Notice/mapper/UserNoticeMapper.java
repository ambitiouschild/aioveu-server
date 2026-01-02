package com.aioveu.system.aioveu09Notice.mapper;

import com.aioveu.system.aioveu02User.model.entity.UserNotice;
import com.aioveu.system.aioveu09Notice.model.query.NoticePageQuery;
import com.aioveu.system.aioveu09Notice.model.vo.NoticePageVO;
import com.aioveu.system.aioveu09Notice.model.vo.UserNoticePageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: UserNoticeMapper
 * @Description TODO  用户公告状态Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:01
 * @Version 1.0
 **/
@Mapper
public interface UserNoticeMapper extends BaseMapper<UserNotice> {

    /**
     * 分页获取我的通知公告
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return 通知公告分页列表
     */
    IPage<UserNoticePageVO> getMyNoticePage(Page<NoticePageVO> page, @Param("queryParams") NoticePageQuery queryParams);
}
