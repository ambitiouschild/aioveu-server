package com.aioveu.ums.aioveu01Member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import org.apache.ibatis.annotations.*;

import java.util.List;


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


}
