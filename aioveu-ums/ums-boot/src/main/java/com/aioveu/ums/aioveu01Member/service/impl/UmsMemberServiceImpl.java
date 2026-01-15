package com.aioveu.ums.aioveu01Member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.ums.aioveu01Member.converter.UmsMemberConverter;
import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.aioveu01Member.model.query.UmsMemberQuery;
import com.aioveu.ums.aioveu02MemberAddress.converter.UmsMemberAddressConverter;
import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.constant.MemberConstants;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.pms.model.vo.ProductHistoryVO;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
import com.aioveu.ums.aioveu01Member.mapper.UmsMemberMapper;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import com.aioveu.ums.aioveu01Member.model.vo.UmsMemberVO;
import com.aioveu.ums.aioveu02MemberAddress.service.UmsMemberAddressService;
import com.aioveu.ums.aioveu01Member.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Description: TODO 会员业务实现类   实现会员相关的核心业务逻辑，包括会员信息管理、地址管理、浏览历史记录等
 *                  核心功能详细说明：
 *                   1. 会员信息管理
 *                      分页查询：支持按昵称模糊查询会员列表
 *                      会员注册：将注册信息保存到数据库，返回会员ID
 *                      认证查询：通过openid或手机号获取会员认证信息
 *                   2. 浏览历史功能
 *                      Redis存储：使用ZSet有序集合存储浏览记录
 *                      自动清理：只保留最近10条记录，避免数据过多
 *                      时间排序：按浏览时间倒序排列，最新记录在前
 *                   3. 安全认证集成
 *                      当前用户：通过SecurityUtils获取登录会员信息
 *                      字段过滤：查询时只选择必要字段，提高安全性
 *                   4. 数据转换策略
 *                      专用转换器：使用MemberConvert、AddressConvert进行对象转换
 *                      BeanUtil工具：简单属性复制使用Hutool工具
 *                   5. 异常处理
 *                      业务异常：会员不存在时抛出明确的BizException
 *                      断言校验：关键业务操作使用断言确保执行成功
 *                      这个实现类很好地遵循了单一职责原则，每个方法都有明确的职责，同时通过依赖注入和转换器模式实现了良好的解耦。

 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Service   // 标记为Spring的服务层组件，由Spring容器管理
@RequiredArgsConstructor   // Lombok注解，为final字段生成构造函数
@Slf4j  // Lombok注解，提供日志记录功能
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {

    // 依赖注入开始
    private final RedisTemplate redisTemplate;  // Redis操作模板，用于缓存操作
    private final UmsMemberConverter umsMemberConverter; // 会员对象转换器，用于DTO/Entity/VO之间的转换

    private final UmsMemberAddressConverter umsMemberAddressConverter;  // 地址对象转换器
    private final UmsMemberAddressService addressService;  // 会员地址服务
    // 依赖注入结束



    /**
     *   todo  分页查询会员列表（支持按昵称模糊查询）
     *
     * @param page 分页参数对象，包含页码、每页大小等信息
     * @param nickname 会员昵称（可选，用于模糊查询）
     * @return 分页结果，包含会员数据列表和分页信息
     */
    @Override
    public IPage<UmsMember> list(Page<UmsMember> page, String nickname) {

        log.info("调用Mapper层的自定义分页查询方法");
        List<UmsMember> list = this.baseMapper.list(page, nickname);

        log.info("将查询结果设置到分页对象中");
        page.setRecords(list);
        return page;
    }


    /**
     *   TODO           添加商品浏览历史记录到Redis
     *              使用ZSet实现，按时间戳排序，只保留最近10条记录
     *
     * @param product 商品浏览历史VO对象，包含商品信息
     * @param userId 会员ID
     */
    @Override
    public void addProductViewHistory(ProductHistoryVO product, Long userId) {

        log.info("验证用户ID是否有效");
        if (userId != null) {

            log.info("生成Redis键：USER_PRODUCT_HISTORY + userId");
            String key = MemberConstants.USER_PRODUCT_HISTORY + userId;

            log.info("将商品信息添加到ZSet，分数为当前时间戳（实现按时间排序）");
            redisTemplate.opsForZSet().add(key, product, System.currentTimeMillis());

            log.info("获取当前ZSet的大小（记录条数）");
            Long size = redisTemplate.opsForZSet().size(key);

            log.info("如果记录数超过10条，移除最早的记录（保持最多10条）");
            if (size > 10) {
                redisTemplate.opsForZSet().removeRange(key, 0, size - 11);
            }
        }
    }


    /**
     *  todo 获取会员的商品浏览历史记录（最近10条）
     *
     * @param userId 会员ID
     * @return 商品浏览历史记录集合，按时间倒序排列（最近浏览的在前面）
     */
    @Override
    public Set<ProductHistoryVO> getProductViewHistory(Long userId) {

        log.info("从ZSet中获取最近10条记录，reverseRange实现倒序排列");
        return redisTemplate.opsForZSet().reverseRange(MemberConstants.USER_PRODUCT_HISTORY + userId,
                0,   // 起始索引
                9);    // 结束索引（共10条记录）
    }

    /**
     *        TODO           根据微信openid获取会员认证信息
     *                  主要用于微信登录认证场景
     *
     * @param openid 微信用户唯一标识
     * @return 会员认证信息DTO，包含会员ID、openid、状态等关键信息
     *         如果会员不存在返回null
     */
    @Override
    public MemberAuthDTO getMemberByOpenid(String openid) {


        log.info("构建查询条件：按openid精确匹配，只查询必要字段");
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getOpenid, openid)
                .select(UmsMember::getId,     // 会员ID
                        UmsMember::getOpenid,  // 微信openid
                        UmsMember::getStatus  // 会员状态
                )
        );

        log.info("会员不存在时返回null，由调用方处理");
        if (entity == null) {
           return null;
        }

        log.info("使用转换器将Entity转换为认证DTO");
        return umsMemberConverter.entity2OpenidAuthDTO(entity);
    }

    /**
     *        todo                              根据手机号获取会员认证信息
     *                                      主要用于手机号登录认证场景
     *
     * @param mobile 手机号码
     * @return 会员认证信息DTO
     * @throws BizException 当会员不存在时抛出业务异常
     */
    @Override
    public MemberAuthDTO getMemberByMobile(String mobile) {

        log.info("构建查询条件：按手机号精确匹配");
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getMobile, mobile)
                .select(UmsMember::getId,   // 会员ID
                        UmsMember::getMobile,   // 手机号
                        UmsMember::getStatus   // 会员状态
                )
        );
        log.info("会员不存在时抛出业务异常");
        if (entity == null) {
            throw new BizException(ResultCode.USER_NOT_EXIST);
        }
        return umsMemberConverter.entity2MobileAuthDTO(entity);
    }

    /**
     *  TODO 新增会员（会员注册）
     *
     * @param memberRegisterDTO 会员注册信息DTO
     * @return 新增会员的ID
     * @throws AssertionError 当保存失败时抛出断言异常
     */
    @Override
    public Long addMember(MemberRegisterDto memberRegisterDTO) {

        log.info("DTO转换为Entity");
        UmsMember umsMember = umsMemberConverter.dto2Entity(memberRegisterDTO);

        log.info("保存到数据库");
        boolean result = this.save(umsMember);

        log.info("使用断言确保保存成功，失败时抛出异常信息");
        Assert.isTrue(result, "新增会员失败");

        log.info("返回新会员的ID");
        return umsMember.getId();
    }

    /**
     *     todo         获取当前登录会员的详细信息
     *              从安全上下文中获取当前会员ID，查询会员基本信息
     *
     * @return 会员信息VO对象，包含前端展示所需字段
     */
    @Override
    public UmsMemberVO getCurrMemberInfo() {

        log.info("从安全工具类获取当前登录会员ID");
        Long memberId = SecurityUtils.getMemberId();

        log.info("构建查询条件：按会员ID查询，只选择需要的字段");
        UmsMember umsMember = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getId, memberId)
                .select(UmsMember::getId,    // 会员ID
                        UmsMember::getNickName,  // 昵称
                        UmsMember::getAvatarUrl,  // 头像
                        UmsMember::getMobile,   // 手机号
                        UmsMember::getBalance  // 账户余额
                )
        );


        UmsMemberVO umsMemberVO = new UmsMemberVO();
        BeanUtil.copyProperties(umsMember, umsMemberVO);
        log.info("创建VO对象并复制属性:{}", umsMemberVO);
        return umsMemberVO;
    }

    /**
     *  TODO 获取指定会员的地址列表
     *
     * @param memberId 会员ID
     * @return 会员地址DTO列表
     */
    @Override
    public List<MemberAddressDTO> listMemberAddress(Long memberId) {


        log.info("查询该会员的所有地址记录");
        List<UmsMemberAddress> entities = addressService.list(
                new LambdaQueryWrapper<UmsMemberAddress>()
                        .eq(UmsMemberAddress::getMemberId, memberId)   // 按会员ID过滤
        );

        log.info("将地址Entity列表转换为DTO列表");
        List<MemberAddressDTO> list = umsMemberAddressConverter.entity2Dto(entities);
        return list;
    }


    /**
     * 获取会员分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<UmsMemberVO>} 会员分页列表
     */
    @Override
    public IPage<UmsMemberVO> getUmsMemberPage(UmsMemberQuery queryParams) {
        Page<UmsMemberVO> pageVO = this.baseMapper.getUmsMemberPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取会员表单数据
     *
     * @param id 会员ID
     * @return 会员表单数据
     */
    @Override
    public UmsMemberForm getUmsMemberFormData(Long id) {
        UmsMember entity = this.getById(id);
        return umsMemberConverter.toForm(entity);
    }

    /**
     * 新增会员
     *
     * @param formData 会员表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveUmsMember(UmsMemberForm formData) {
        UmsMember entity = umsMemberConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新会员
     *
     * @param id   会员ID
     * @param formData 会员表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateUmsMember(Long id,UmsMemberForm formData) {
        UmsMember entity = umsMemberConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除会员
     *
     * @param ids 会员ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUmsMembers(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的会员数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
