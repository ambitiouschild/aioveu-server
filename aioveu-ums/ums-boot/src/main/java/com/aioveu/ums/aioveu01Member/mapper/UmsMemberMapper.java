package com.aioveu.ums.aioveu01Member.mapper;

import com.aioveu.ums.aioveu01Member.model.query.UmsMemberQuery;
import com.aioveu.ums.aioveu01Member.model.vo.UmsMemberVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 会员Mapper接口
 * @Date  2026/1/12 14:44
 * @Param
 * @return
 **/

@Mapper
public interface UmsMemberMapper extends BaseMapper<UmsMember> {

    @Select("<script>" +
            " SELECT * from ums_member " +
            " <if test ='nickname !=null and nickname.trim() neq \"\" ' >" +
            "       WHERE nick_name like concat('%',#{nickname},'%')" +
            " </if>" +
            " ORDER BY update_time DESC, create_time DESC" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(property = "addressList", column = "id", many = @Many(select = "com.aioveu.ums.aioveu02Address.mapper.UmsAddressMapper.listByUserId"))
    })
    List<UmsMember> list(Page<UmsMember> page, String nickname);

    /**
     * 获取会员分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<UmsMemberVO>} 会员分页列表
     */
    Page<UmsMemberVO> getUmsMemberPage(Page<UmsMemberVO> page, UmsMemberQuery queryParams);

}
