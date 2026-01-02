package com.aioveu.system.aioveu09Notice.mapper;

import com.aioveu.system.aioveu09Notice.model.entity.Notice;
import com.aioveu.system.aioveu09Notice.model.query.NoticePageQuery;
import com.aioveu.system.aioveu09Notice.model.vo.NoticeBO;
import com.aioveu.system.aioveu09Notice.model.vo.NoticePageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: NoticeMapper
 * @Description TODO  通知公告Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:03
 * @Version 1.0
 **/

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 获取通知公告分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return 通知公告分页数据
     */
    Page<NoticeBO> getNoticePage(Page<NoticePageVO> page, NoticePageQuery queryParams);

    /**
     * 获取阅读时通知公告详情
     *
     * @param id 通知公告ID
     * @return 通知公告详情
     */
    NoticeBO getNoticeDetail(@Param("id") Long id);
}
