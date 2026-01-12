package com.aioveu.ums.aioveu02MemberAddress.mapper;

import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.aioveu.ums.aioveu02MemberAddress.model.query.UmsMemberAddressQuery;
import com.aioveu.ums.aioveu02MemberAddress.model.vo.UmsMemberAddressVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 会员收货地址Mapper接口
 * @Date  2026/1/12 15:32
 * @Param
 * @return
 **/

@Mapper
public interface UmsMemberAddressMapper extends BaseMapper<UmsMemberAddress> {

    @Select("<script>" +
            " SELECT * from ums_address where member_id =#{userId} " +
            "</script>")
    List<UmsMemberAddress> listByUserId(Long userId);

    /**
     * 获取会员收货地址分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<UmsMemberAddressVO>} 会员收货地址分页列表
     */
    Page<UmsMemberAddressVO> getUmsMemberAddressPage(Page<UmsMemberAddressVO> page, UmsMemberAddressQuery queryParams);

}
