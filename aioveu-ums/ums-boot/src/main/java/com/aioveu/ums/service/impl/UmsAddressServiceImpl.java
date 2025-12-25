package com.aioveu.ums.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.mapper.UmsAddressMapper;
import com.aioveu.ums.model.entity.UmsAddress;
import com.aioveu.ums.model.form.AddressForm;
import com.aioveu.ums.service.UmsAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: TODO 会员地址业务实现类   实现会员地址的增删改查等业务逻辑，包含地址的新增、修改、查询列表等功能
 *                      代码详细说明
 *                          1. 类级别注释
 *                              继承关系：继承ServiceImpl获得MyBatis-Plus的通用CRUD功能
 *                              服务标识：@Service注解标识为Spring服务组件
 *                              泛型说明：UmsAddressMapper为数据访问层，UmsAddress为实体类
 *                          2. 关键方法说明
 *                              addAddress方法：
 *                                  事务管理：@Transactional确保地址新增和默认地址更新的原子性
 *                                  业务逻辑：新增地址时，如果设置为默认地址，需要更新其他地址为非默认
 *                                  安全控制：通过SecurityUtils获取当前登录用户，确保数据隔离
 *                              updateAddress方法：
 *                                  更新策略：基于主键ID更新，确保操作精确性
 *                                  默认地址处理：与新增方法类似的默认地址逻辑，保证数据一致性
 *                              listCurrentMemberAddresses方法：
 *                                  查询优化：使用Lambda表达式构建类型安全的查询条件
 *                                  排序策略：默认地址优先显示，提升用户体验
 *                                  空值安全：使用Optional避免NPE，增强代码健壮性
 *                          3. 技术特点
 *                              BeanUtil拷贝：简化对象属性拷贝，提高开发效率
 *                              Lambda表达式：类型安全的条件构建，避免硬编码
 *                              Stream API：函数式编程处理集合数据，代码更简洁
 *                              防御式编程：充分处理边界情况和空值问题
 *                          4. 业务规则
 *                              一个会员只能有一个默认地址
 *                              默认地址在列表中优先显示
 *                              所有地址操作都基于当前登录会员，确保数据安全
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/
@Slf4j
@Service   // 标识为Spring的服务层组件，由Spring容器管理
public class UmsAddressServiceImpl extends ServiceImpl<UmsAddressMapper, UmsAddress> implements UmsAddressService {

    /**
     *       TODO           新增会员地址
     *                  1. 从安全上下文中获取当前登录会员ID
     *                  2. 将表单数据拷贝到实体对象
     *                  3. 保存地址信息
     *                  4. 如果新增的地址设置为默认地址，需要将其他默认地址更新为非默认
     *
     * @param addressForm 地址表单对象，包含前端传递的地址信息
     * @return boolean 操作是否成功
     */
    @Override
    @Transactional    // 声明事务管理，确保数据库操作的原子性
    public boolean addAddress(AddressForm addressForm) {

        log.info("从安全上下文中获取当前登录会员的ID");
        Long memberId = SecurityUtils.getMemberId();

        log.info("创建地址实体对象");
        UmsAddress umsAddress = new UmsAddress();

        log.info("使用BeanUtil工具类将表单对象的属性拷贝到实体对象");
        BeanUtil.copyProperties(addressForm, umsAddress);

        log.info("设置地址关联的会员ID");
        umsAddress.setMemberId(memberId);

        log.info("保存地址信息到数据库");
        boolean result = this.save(umsAddress);
        if (result) {
            // 修改其他默认地址为非默认
            log.info("如果保存成功，并且当前地址被设置为默认地址");
            if (GlobalConstants.STATUS_YES.equals(addressForm.getDefaulted())) {

                log.info("更新该会员的其他默认地址为非默认状态");
                log.info("条件：同一会员的其他地址，且是默认地址，且不是当前新增的地址");
                this.update(new LambdaUpdateWrapper<UmsAddress>()
                        .eq(UmsAddress::getMemberId, memberId)   // 条件：同一会员
                        .eq(UmsAddress::getDefaulted, 1)     // 条件：当前是默认地址
                        .ne(UmsAddress::getId,umsAddress.getId())   // 条件：排除当前新增的地址
                        .set(UmsAddress::getDefaulted, 0)   // 设置：更新为非默认地址(0)
                );
            }
        }
        return result;
    }

    /**
     *         TODO          修改会员地址
     *                   1. 获取当前登录会员ID
     *                   2. 将表单数据拷贝到实体对象
     *                   3. 更新地址信息
     *                   4. 如果修改后的地址设置为默认地址，需要将其他默认地址更新为非默认
     *
     * @param addressForm 地址表单对象，包含要修改的地址信息
     * @return boolean 操作是否成功
     */
    @Override
    public boolean updateAddress(AddressForm addressForm) {

        log.info("从安全上下文中获取当前登录会员的ID");
        Long memberId = SecurityUtils.getMemberId();


        log.info("创建地址实体对象");
        UmsAddress umsAddress = new UmsAddress();

        log.info("将表单数据拷贝到实体对象");
        BeanUtil.copyProperties(addressForm, umsAddress);

        log.info("根据主键ID更新地址信息");
        boolean result = this.updateById(umsAddress);

        if(result){
            // 修改其他默认地址为非默认
            log.info("如果更新成功，并且当前地址被设置为默认地址");
            if (GlobalConstants.STATUS_YES.equals(addressForm.getDefaulted())) {

                log.info("更新该会员的其他默认地址为非默认状态");
                log.info("条件：同一会员的其他地址，且是默认地址，且不是当前修改的地址");
                this.update(new LambdaUpdateWrapper<UmsAddress>()
                        .eq(UmsAddress::getMemberId, memberId)    // 条件：同一会员
                        .eq(UmsAddress::getDefaulted, 1)      // 条件：当前是默认地址
                        .ne(UmsAddress::getId, umsAddress.getId())   // 条件：排除当前修改的地址
                        .set(UmsAddress::getDefaulted, 0)   // 设置：更新为非默认地址(0)
                );
            }
        }
        return result;
    }

    /**
     *        TODO          获取当前登录会员的地址列表
     *                  1. 获取当前登录会员ID
     *                  2. 查询该会员的所有地址，按默认地址降序排列（默认地址排在前面）
     *                  3. 将实体对象列表转换为DTO对象列表
     *                  4. 处理空值情况，避免NPE
     *
     * @return List<MemberAddressDTO> 会员地址DTO列表
     */
    @Override
    public List<MemberAddressDTO> listCurrentMemberAddresses() {

        log.info("从安全上下文中获取当前登录会员的ID");
        Long memberId = SecurityUtils.getMemberId();

        log.info("构建查询条件：查询该会员的所有地址，按默认地址降序排列（默认地址排在前面）");
        List<UmsAddress> umsAddressList = this.list(new LambdaQueryWrapper<UmsAddress>()
                .eq(UmsAddress::getMemberId, memberId)    // 条件：会员ID相等
                .orderByDesc(UmsAddress::getDefaulted) // 默认地址排在首位  // 排序：默认地址降序（1在前，0在后）
        );

        log.info("使用Optional处理空值，避免NullPointerException");
        // 如果umsAddressList为null，则使用空ArrayList
        List<MemberAddressDTO> memberAddressList = Optional.ofNullable(umsAddressList)
                .orElse(new ArrayList<>())      // 如果为null，返回空列表
                .stream() // 转换为Stream流进行数据处理
                .map(umsAddress -> {

                    // 将每个UmsAddress实体对象转换为MemberAddressDTO对象
                    MemberAddressDTO memberAddressDTO = new MemberAddressDTO();

                    // 拷贝属性值
                    BeanUtil.copyProperties(umsAddress, memberAddressDTO);
                    return memberAddressDTO;
                }).collect(Collectors.toList());    // 将Stream收集为List
        return memberAddressList;
    }
}
