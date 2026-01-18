package com.aioveu.pms.aioveu06Spu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.pms.aioveu06Spu.model.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.pms.aioveu06Spu.constant.ProductConstants;
import com.aioveu.pms.aioveu07SpuAttribute.converter.PmsSpuAttributeConverter;
import com.aioveu.pms.aioveu06Spu.converter.PmsSpuConverter;
import com.aioveu.pms.aioveu06Spu.enums.AttributeTypeEnum;
import com.aioveu.pms.aioveu06Spu.mapper.PmsSpuMapper;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.aioveu.pms.aioveu06Spu.model.entity.PmsSpu;
import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import com.aioveu.pms.aioveu06Spu.model.form.PmsSpuForm;
import com.aioveu.pms.aioveu06Spu.model.query.PmsSpuQuery;
import com.aioveu.pms.model.vo.*;
import com.aioveu.pms.aioveu05Sku.service.PmsSkuService;
import com.aioveu.pms.aioveu07SpuAttribute.service.PmsSpuAttributeService;
import com.aioveu.pms.aioveu06Spu.service.PmsSpuService;
import com.aioveu.ums.api.MemberFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO 商品业务实现类
 *                   * 负责商品(SPU)的增删改查、详情展示、分页查询等核心业务逻辑
 *                   * 包含商品属性、规格、SKU的关联管理和用户浏览历史记录功能
 *                   核心业务功能解析
 *                      1. 商品详情展示策略
 *                          APP端详情 (getSpuDetailForApp)：包含完整的商品信息+用户行为记录
 *                          管理端详情 (getSpuDetail)：主要用于商品编辑，返回原始数据
 *                          数据转换：使用BeanUtil进行安全的对象属性拷贝
 *                      2. 商品信息层级结构
 *                          SPU（商品）
 *                          ├── 基本信息（名称、价格、图片等）
 *                          ├── 属性列表（参数属性：材质、产地等）
 *                          ├── 规格列表（销售规格：颜色、尺寸等）
 *                          └── SKU列表（具体库存单元）
 *                      3. 事务性操作设计
 *                          新增商品 (addSpu)：SPU + 属性 + 规格 + SKU 原子性保
 *                          修改商品 (updateSpuById)：完整的事务性更新
 *                          删除商品 (removeBySpuIds)：级联删除所有关联数据
 *                      关键技术实现
 *                      1. 临时ID处理机制
 *                          // 前端生成的临时ID格式：temp_123
 *                          // 保存时转换为数据库ID并建立映射
 *                          Map<String, Long> tempWithNewSpecIdMap = new HashMap<>();
 *                          tempWithNewSpecIdMap.put("temp_123", 456L); // 临时ID -> 数据库ID
 *                      2. 规格分组处理
 *                          // 将平铺的规格数据按名称分组，构建树形结构
 *                          Map<String, List<PmsSpuAttribute>> specValueMap = specSourceList.stream()
 *                              .collect(Collectors.groupingBy(PmsSpuAttribute::getName));
 *                      3. 差异数据识别算法
 *                          // 识别需要删除的数据：数据库中存在但表单中不存在
 *                          List<Long> removeIds = dbIds.stream()
 *                              .filter(dbId -> !formIds.contains(dbId))
 *                              .collect(Collectors.toList());
 *                      性能优化策略
 *                          1.选择性查询：使用select()指定需要的字段，减少数据传输
 *                          2.批量操作：使用saveOrUpdateBatch减少数据库交互
 *                          3.流式处理：使用Stream API进行内存数据加工
 *                          4.缓存应用：浏览历史通过Feign调用，可考虑本地缓存
 *                      异常处理机制
 *                          1.参数校验：使用Assert进行前置条件检查
 *                          2.存在性验证：商品操作前验证数据存在性
 *                          3.事务回滚：@Transactional保证数据一致性
 *                          4.空值处理：使用CollectionUtil进行集合空值判断
 *                      扩展性设计
 *                          1.转换器模式：使用Converter隔离实体和VO的转换逻辑
 *                          2.Feign客户端：通过微服务调用实现功能解耦
 *                          3.枚举类型：使用AttributeTypeEnum管理属性类型
 *                          4.常量类：ProductConstants统一管理业务常量
 *                      这个商品服务实现类设计合理，涵盖了商品管理的核心业务流程，具有良好的可维护性和扩展性。
 * @Author: 雒世松
 * @Date: 2025/6/5 18:33
 * @param
 * @return:
 **/

@Slf4j
@Service
@RequiredArgsConstructor    // Lombok注解：自动注入final修饰的依赖
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {


    // 依赖注入：SKU服务，用于管理商品库存单元
    private final PmsSkuService pmsSkuService;

    // 依赖注入：商品属性服务，用于管理商品属性和规格
    private final PmsSpuAttributeService pmsSpuAttributeService;

    // 依赖注入：会员Feign客户端，用于调用会员服务的远程接口
    private final MemberFeignClient memberFeignClient;

    // 依赖注入：商品转换器，用于实体和表单之间的转换
    private final PmsSpuConverter pmsSpuConverter;

    // 依赖注入：商品属性转换器，用于属性实体和表单之间的转换
    private final PmsSpuAttributeConverter pmsSpuAttributeConverter;

    /**
     *          TODO            Admin后台-商品分页列表
     *                      为管理后台提供商品的分页查询功能，包含完整的商品信息
     *
     * @param queryParams 查询参数对象，包含分页参数和筛选条件
     * @return IPage<PmsSpuPageVO> 分页结果，包含商品列表和分页信息
     */
    @Override
    public IPage<PmsSpuVO> listPagedSpu(PmsSpuQuery queryParams) {

        log.info("创建分页对象，设置当前页和每页大小");
        Page<PmsSpuVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());

        log.info("调用Mapper层自定义方法获取分页数据");
        List<PmsSpuVO> list = this.baseMapper.listPagedSpu(page, queryParams);

        log.info("将查询结果设置到分页对象中");
        page.setRecords(list);
        return page;
    }

    /**
     *         TODO             APP端-商品分页列表
     *                      为移动端应用提供商品的分页查询功能，数据格式适配移动端需求
     *
     * @param queryParams 查询参数对象
     * @return IPage<SpuPageVO> 适配APP端的分页结果
     */
    @Override
    public IPage<SpuPageVO> listPagedSpuForApp(PmsSpuQuery queryParams) {

        log.info("创建分页对象");
        Page<SpuPageVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());

        log.info("调用Mapper层获取APP端专用数据");
        List<SpuPageVO> list = this.baseMapper.listPagedSpuForApp(page, queryParams);

        log.info("将查询结果设置到分页对象中");
        page.setRecords(list);
        return page;
    }

    /**
     *      TODO                App端-获取商品详情
     *                      为移动端提供完整的商品详情信息，包括基本信息、属性、规格、SKU等
     *                      同时记录用户的浏览历史
     *
     * @param spuId 商品ID
     * @return SpuDetailVO 商品详情视图对象
     */
    @Override
    public SpuDetailVO getSpuDetailForApp(Long spuId) {

        log.info("1. 查询商品基本信息并校验存在性");
        PmsSpu pmsSpu = this.getById(spuId);
        Assert.isTrue(pmsSpu != null, "商品不存在");

        log.info("创建商品详情VO对象");
        SpuDetailVO spuDetailVO = new SpuDetailVO();

        // 商品基本信息
        log.info("2. 设置商品基本信息");
        SpuDetailVO.GoodsInfo goodsInfo = new SpuDetailVO.GoodsInfo();

        log.info("使用BeanUtil拷贝属性，排除album字段单独处理");
        BeanUtil.copyProperties(pmsSpu, goodsInfo, "album");


        log.info("构建商品相册：主图 + 相册图片");
        List<String> album = new ArrayList<>();

        log.info("添加主图");
        if (StrUtil.isNotBlank(pmsSpu.getPicUrl())) {
            album.add(pmsSpu.getPicUrl());
        }

        log.info("添加相册图片");
        if (pmsSpu.getAlbum() != null && pmsSpu.getAlbum().length > 0) {
            album.addAll(Arrays.asList(pmsSpu.getAlbum()));
            goodsInfo.setAlbum(album);
        }
        spuDetailVO.setGoodsInfo(goodsInfo);

        // 商品属性列表
        log.info("3. 设置商品属性列表（参数属性）");
        List<SpuDetailVO.Attribute> attributeList = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                        .eq(PmsSpuAttribute::getType, AttributeTypeEnum.ATTR.getValue())  // 查询属性类型
                        .eq(PmsSpuAttribute::getSpuId, spuId)  // 匹配商品ID
                        .select(PmsSpuAttribute::getId, PmsSpuAttribute::getName, PmsSpuAttribute::getValue))  // 只选择需要的字段
                .stream().
                // 将属性实体转换为VO对象
                map(item -> {
                    SpuDetailVO.Attribute attribute = new SpuDetailVO.Attribute();
                    BeanUtil.copyProperties(item, attribute);
                    return attribute;
                }).collect(Collectors.toList());
        spuDetailVO.setAttributeList(attributeList);


        // 商品规格列表
        log.info("3. 设置商品属性列表（参数属性）");
        List<PmsSpuAttribute> specSourceList = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                .eq(PmsSpuAttribute::getType, AttributeTypeEnum.SPEC.getValue())    // 查询规格类型
                .eq(PmsSpuAttribute::getSpuId, spuId)
                .select(PmsSpuAttribute::getId, PmsSpuAttribute::getName, PmsSpuAttribute::getValue));

        List<SpuDetailVO.Specification> specList = new ArrayList<>();

        log.info("按规格名称分组，构建规格Map：key=规格名，value=规格值列表）");
        // 规格Map [key:"颜色",value:[{id:1,value:"黑"},{id:2,value:"白"}]]
        log.info("规格Map [key:\"颜色\",value:[{id:1,value:\"黑\"},{id:2,value:\"白\"}]]");
        Map<String, List<PmsSpuAttribute>> specValueMap = specSourceList.stream().collect(Collectors.groupingBy(PmsSpuAttribute::getName));

        log.info("遍历规格分组，构建规格树形结构");
        for (Map.Entry<String, List<PmsSpuAttribute>> entry : specValueMap.entrySet()) {
            String specName = entry.getKey();
            List<PmsSpuAttribute> specValueSourceList = entry.getValue();

            // 规格映射处理
            SpuDetailVO.Specification spec = new SpuDetailVO.Specification();
            spec.setName(specName);
            if (CollectionUtil.isNotEmpty(specValueSourceList)) {

                // 转换规格值列表
                List<SpuDetailVO.Specification.Value> specValueList = specValueSourceList.stream().map(item -> {
                    SpuDetailVO.Specification.Value specValue = new SpuDetailVO.Specification.Value();
                    specValue.setId(item.getId());
                    specValue.setValue(item.getValue());
                    return specValue;
                }).collect(Collectors.toList());
                spec.setValues(specValueList);
                specList.add(spec);
            }
        }
        spuDetailVO.setSpecList(specList);

        // 商品SKU列表
        log.info("5. 设置商品SKU列表");
        List<PmsSku> skuSourceList = pmsSkuService.list(new LambdaQueryWrapper<PmsSku>().eq(PmsSku::getSpuId, spuId));
        if (CollectionUtil.isNotEmpty(skuSourceList)) {
            List<SpuDetailVO.Sku> skuList = skuSourceList.stream().map(item -> {
                SpuDetailVO.Sku sku = new SpuDetailVO.Sku();
                BeanUtil.copyProperties(item, sku);
                return sku;
            }).collect(Collectors.toList());
            spuDetailVO.setSkuList(skuList);
        }

        // 添加用户浏览历史记录
        log.info("6. 记录用户浏览历史（仅当用户已登录时）");
        Long memberId = SecurityUtils.getMemberId();
        if (memberId != null) {
            ProductHistoryVO vo = new ProductHistoryVO();
            vo.setId(goodsInfo.getId());
            vo.setName(goodsInfo.getName());
            log.info("使用相册第一张图片作为浏览历史图片");
            vo.setPicUrl(goodsInfo.getAlbum() != null ? goodsInfo.getAlbum().get(0) : null);
            log.info("调用会员服务记录浏览历史");
            memberFeignClient.addProductViewHistory(vo);
        }
        return spuDetailVO;
    }


    /**
     *         TODO             管理后台-获取商品详情
     *                      为管理后台提供商品详情，包含完整的商品信息用于编辑
     *
     * @param spuId 商品ID
     * @return PmsSpuDetailVO 商品详情视图对象
     */
    @Override
    public PmsSpuDetailVO getSpuDetail(Long spuId) {
        PmsSpuDetailVO pmsSpuDetailVO = new PmsSpuDetailVO();

        log.info("查询商品基本信息");
        PmsSpu entity = this.getById(spuId);
        Assert.isTrue(entity != null, "商品不存在");

        log.info("拷贝基本信息，单独处理相册字段");
        BeanUtil.copyProperties(entity, pmsSpuDetailVO, "album");
        pmsSpuDetailVO.setSubPicUrls(entity.getAlbum());

        // 商品属性列表
        log.info("查询商品属性列表");
        List<PmsSpuAttribute> attrList = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                .eq(PmsSpuAttribute::getSpuId, spuId)
                .eq(PmsSpuAttribute::getType, AttributeTypeEnum.ATTR.getValue()));
        pmsSpuDetailVO.setAttrList(attrList);

        // 商品规格列表
        log.info("查询商品规格列表");
        List<PmsSpuAttribute> specList = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                .eq(PmsSpuAttribute::getSpuId, spuId)
                .eq(PmsSpuAttribute::getType, AttributeTypeEnum.SPEC.getValue()));
        pmsSpuDetailVO.setSpecList(specList);

        // 商品SKU列表
        log.info("查询商品SKU列表");
        List<PmsSku> skuList = pmsSkuService.list(new LambdaQueryWrapper<PmsSku>().eq(PmsSku::getSpuId, spuId));
        pmsSpuDetailVO.setSkuList(skuList);
        return pmsSpuDetailVO;

    }

    /**
     *      TODO        新增商品
     *              保存商品基本信息、属性、规格和SKU信息
     *
     * @param formData 商品表单数据
     * @return boolean 操作是否成功
     *
     * @Transactional 注解保证事务性：商品基本信息、属性、规格、SKU的保存要么全部成功，要么全部回滚
     */
    @Override
    @Transactional
    public boolean addSpu(PmsSpuForm formData) {

        log.info("1. 转换表单数据为实体并保存商品基本信息");
        PmsSpu entity = pmsSpuConverter.form2Entity(formData);

        boolean result = this.save(entity);

        if (result) {
            Long spuId = entity.getId();

            log.info("2. 保存商品属性");
            // 保存属性
            List<PmsSpuAttributeForm> attrList = formData.getAttrList();
            this.saveSpuAttrs(spuId, attrList);
            // 保存规格
            log.info("3. 保存商品规格，并获取临时规格ID到数据库ID的映射");
            List<PmsSpuAttributeForm> specList = formData.getSpecList();

            log.info("保存商品规格方法saveSpuSpecs");
            Map<String, Long> tempWithNewSpecIdMap = this.saveSpuSpecs(spuId, specList);
            // 保存SKU
            log.info("4. 保存SKU信息，使用规格ID映射替换临时ID");
            List<PmsSku> skuList = formData.getSkuList();

            log.info("保存SKU，需要替换提交表单中的临时规格ID，saveSku");
            this.saveSku(spuId, skuList, tempWithNewSpecIdMap);

        }
        // 无异常返回true
        return result;
    }


    /**
     *         TODO                 修改商品
     *                          更新商品基本信息及其关联的属性、规格和SKU信息
     *
     * @param spuId 商品ID
     * @param formData 商品表单数据
     * @return boolean 操作是否成功
     */
    @Transactional
    @Override
    public boolean updateSpuById(Long spuId, PmsSpuForm formData) {

        log.info("1. 转换并更新商品基本信息");
        PmsSpu entity = pmsSpuConverter.form2Entity(formData);

        boolean result = this.updateById(entity);


        if (result) {

            // 属性保存
            log.info("2. 更新商品属性");
            List<PmsSpuAttributeForm> attrList = formData.getAttrList();
            this.saveSpuAttrs(spuId, attrList);

            // 保存商品规格值
            log.info("3. 更新商品规格，获取ID映射");
            List<PmsSpuAttributeForm> specList = formData.getSpecList();
            Map<String, Long> specTempIdIdMap = this.saveSpuSpecs(spuId, specList);

            // SKU保存
            log.info("4. 更新SKU信息");
            List<PmsSku> skuList = formData.getSkuList();
            this.saveSku(spuId, skuList, specTempIdIdMap);
        }

        return result;
    }


    /**
     *       TODO           删除商品（支持批量删除）
     *                  删除商品及其关联的SKU、属性、规格信息
     *
     * @param ids 商品ID，多个以英文逗号(,)分割
     * @return boolean 操作是否成功
     */
    @Override
    @Transactional
    public boolean removeBySpuIds(String ids) {

        log.info("分割商品ID字符串");
        String[] spuIds = ids.split(",");

        for (String spuId : spuIds) {

            log.info("1. 删除关联的SKU信息");
            pmsSkuService.remove(new LambdaQueryWrapper<PmsSku>().eq(PmsSku::getSpuId, spuId));
            // 规格
            log.info("2. 删除商品规格");
            pmsSpuAttributeService.remove(new LambdaQueryWrapper<PmsSpuAttribute>().eq(PmsSpuAttribute::getSpuId, spuId));
            // 属性
            log.info("3. 删除商品属性");
            pmsSpuAttributeService.remove(new LambdaQueryWrapper<PmsSpuAttribute>().eq(PmsSpuAttribute::getSpuId, spuId));
            // SPU
            log.info("4. 删除商品基本信息");
            this.removeById(spuId);
        }
        // 无异常直接返回true
        return true;
    }

    /**
     *     TODO                 获取秒杀商品列表
     *                      用于秒杀活动页面，返回最近创建的商品信息
     *
     * @return List<SeckillingSpuVO> 秒杀商品列表
     */
    @Override
    public List<SeckillingSpuVO> listSeckillingSpu() {

        log.info("查询商品基本信息，按创建时间降序排列息");
        List<PmsSpu> entities = this.list(new LambdaQueryWrapper<PmsSpu>()
                .select(PmsSpu::getId, PmsSpu::getName, PmsSpu::getPicUrl, PmsSpu::getPrice)
                .orderByDesc(PmsSpu::getCreateTime)
        );

        log.info("转换为秒杀专用VO对象");
        return pmsSpuConverter.entity2SeckillingVO(entities);
    }


    /**
     *         TODO                 保存SKU信息（私有方法）
     *                          处理SKU的新增、修改和删除，替换临时规格ID为数据库ID
     *
     * @param spuId 商品ID
     * @param skuList SKU列表
     * @param specTempIdIdMap 临时规格ID到数据库规格ID的映射
     * @return boolean 操作是否成功
     */
    private boolean saveSku(Long spuId, List<PmsSku> skuList, Map<String, Long> specTempIdIdMap) {

        // 删除SKU
        log.info("1. 删除此次提交中不存在的SKU");
        log.info("获取表单中提交的SKU ID列表");
        List<Long> formSkuIds = skuList.stream().map(PmsSku::getId).toList();

        log.info("获取数据库中现有的SKU ID列表");
        List<Long> dbSkuIds = pmsSkuService.list(new LambdaQueryWrapper<PmsSku>().eq(PmsSku::getSpuId, spuId)
                        .select(PmsSku::getId)).stream().map(PmsSku::getId)
                .toList();

        log.info("计算需要删除的SKU ID：数据库中存在但表单中不存在的");
        List<Long> removeSkuIds = dbSkuIds.stream()
                .filter(dbSkuId -> !formSkuIds.contains(dbSkuId))
                .collect(Collectors.toList());

        log.info("执行删除操作");
        if (CollectionUtil.isNotEmpty(removeSkuIds)) {
            pmsSkuService.removeByIds(removeSkuIds);
        }

        // 新增/修改SKU
        log.info("新增/修改SKU");
        List<PmsSku> pmsSkuList = skuList.stream().map(sku -> {
            // 临时规格ID转换
            // 处理规格ID：将临时ID替换为数据库ID
            String specIds = Arrays.stream(sku.getSpecIds().split("\\|"))
                    .map(specId -> specId.startsWith(ProductConstants.SPEC_TEMP_ID_PREFIX) ?
                            specTempIdIdMap.get(specId) + "" : specId)  // 临时ID替换，已有ID保持不变
                    .collect(Collectors.joining("_"));  // 重新拼接规格ID字符串
            sku.setSpecIds(specIds);
            sku.setSpuId(spuId);
            return sku;
        }).collect(Collectors.toList());

        log.info("批量保存或更新SKU");
        return pmsSkuService.saveOrUpdateBatch(pmsSkuList);
    }


    /**
     *      TODO            保存商品属性（私有方法）
     *                  处理商品属性的新增、修改和删除
     *                  属性（attr）只需要区分 null 和非 null
     *                                                          // 关键逻辑：
     *                                      // 1. 删除此次提交移除的商品规格
     *                                      // 2. 新增此次提交的新加的商品规格
     *                                      // 3. 修改此次提交的需要修改的商品规格
     *                                      为什么要分开处理？
     *
     *                                      效率：批量操作比逐条判断更快
     *
     *                                      清晰：逻辑分离，便于维护和调试
     *
     *                                      事务：可以更好地控制事务边界
     *
     *
     * @param spuId 商品ID
     * @param attrList 商品属性表单列表
     * @return boolean 操作是否成功
     */
    private boolean saveSpuAttrs(Long spuId, List<PmsSpuAttributeForm> attrList) {

        // 1. 【删除】此次提交移除的商品规格
        log.info("1. 删除此次提交移除的商品属性");
        log.info("获取表单中保留的属性ID（非空ID）");
        // 1.1 此次提交保留的商品属性ID
        List<Long> retainAttrIds = attrList.stream()
                .filter(item -> item.getId() != null)
                .map(item -> Convert.toLong(item.getId()))
                .toList();

        // 1.2 获取原商品属性ID集合
        log.info("获取数据库中原有的属性ID");
        List<Long> originAttrIds = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                        .eq(PmsSpuAttribute::getSpuId, spuId).eq(PmsSpuAttribute::getType, AttributeTypeEnum.ATTR.getValue())
                        .select(PmsSpuAttribute::getId))
                .stream()
                .map(PmsSpuAttribute::getId)
                .toList();

        log.info("计算需要删除的属性ID：原属性ID - 保留的属性ID");
        // 1.3 需要删除的商品属性：原商品属性-此次提交保留的属性
        List<Long> removeAttrValIds = originAttrIds.stream()
                .filter(id -> !retainAttrIds.contains(id))
                .collect(Collectors.toList());

        log.info("执行删除操作");
        if (CollectionUtil.isNotEmpty(removeAttrValIds)) {
            pmsSpuAttributeService.removeByIds(removeAttrValIds);
        }

        // 新增或修改商品属性
        log.info("2. 新增或修改商品属性");
        List<PmsSpuAttribute> entities = attrList.stream().map(item -> {
            PmsSpuAttribute entity = pmsSpuAttributeConverter.form2Entity(item);
            entity.setId(Convert.toLong(item.getId()));   // 设置ID，新增时为null
            entity.setSpuId(spuId);
            entity.setType(AttributeTypeEnum.ATTR.getValue());   // 设置为属性类型
            return entity;
        }).collect(Collectors.toList());

        log.info("批量保存或更新");
        if (CollectionUtil.isNotEmpty(entities)) {
            return pmsSpuAttributeService.saveOrUpdateBatch(entities);
        }
        return true;
    }

    /**
     *        TODO              保存商品规格（私有方法）
     *                          处理商品规格的新增、修改和删除，返回临时ID到数据库ID的映射
     *                           规格需要区分是否是临时ID
     *                           规格有临时ID的概念：以 ProductConstants.SPEC_TEMP_ID_PREFIX开头（可能是 temp_或类似前缀）
     *                           // 前端传的ID格式：
 *                               // 新增规格：temp_123456789_abc123
 *                               // 已有规格：1001 (数据库ID)
     *                               为什么用临时ID？
     *                                  前端友好：前端可以在用户操作时立即生成ID，不需要等待后端响应
     *                                  关联关系：SKU需要引用规格ID，前端生成时可以建立关联
     *                                  幂等性：支持重复提交，不会产生重复数据
     *                                  // 后端可以：
     *                                  1. 一次性删除不在此列表中的规格
     *                                  2. 批量新增带临时ID的规格
     *                                  3. 批量更新已有ID的规格
     *                                  4. 建立临时ID到数据库ID的映射
     *
     *
     *
     * @param spuId 商品ID
     * @param specList 商品规格表单列表
     * @return Map<String, Long> 临时规格ID到数据库规格ID的映射
     */
    private Map<String, Long> saveSpuSpecs(Long spuId, List<PmsSpuAttributeForm> specList) {


        log.info("1. 删除此次提交移除的商品规格");
        log.info("获取表单中保留的规格ID（非临时ID）");
        // 1. 【删除】此次提交移除的商品规格
        // 1.1 此次提交保留的规格
        List<Long> retainSpuSpecIds = specList.stream()
                .filter(item -> !item.getId().startsWith(ProductConstants.SPEC_TEMP_ID_PREFIX))
                .map(item -> Convert.toLong(item.getId()))
                .toList();

        // 1.2 原商品规格
        log.info("获取数据库中原有的规格ID");
        List<Long> originSpuSpecIds = pmsSpuAttributeService.list(new LambdaQueryWrapper<PmsSpuAttribute>()
                        .eq(PmsSpuAttribute::getSpuId, spuId)
                        .eq(PmsSpuAttribute::getType, AttributeTypeEnum.SPEC.getValue())
                        .select(PmsSpuAttribute::getId))
                .stream().map(PmsSpuAttribute::getId)
                .toList();

        // 1.3 需要删除的商品规格：原商品规格-此次提交保留的规格
        log.info("计算需要删除的规格ID：原规格ID - 保留的规格ID");
        log.info("差异数据识别算法");
        List<Long> removeSpuSpecIds = originSpuSpecIds.stream()
                .filter(id -> !retainSpuSpecIds.contains(id))
                .collect(Collectors.toList());


        log.info("执行删除操作");
        if (CollectionUtil.isNotEmpty(removeSpuSpecIds)) {
            // 删除商品的规格
            pmsSpuAttributeService.removeByIds(removeSpuSpecIds);
        }

        // 2. 【新增】此次提交的新加的商品规格
        // 临时规格ID和持久化数据库得到的规格ID的映射，用于替换SKU临时的规格ID
        log.info("2. 新增此次提交的新规格");
        log.info("创建临时ID到数据库ID的映射表");
        Map<String, Long> tempWithNewSpecIdMap = new HashMap<>();

        log.info("筛选出新增的规格（ID以临时前缀开头）");
        List<PmsSpuAttributeForm> newSpecList = specList.stream()
                .filter(item -> item.getId().startsWith(ProductConstants.SPEC_TEMP_ID_PREFIX))
                .collect(Collectors.toList());


        if (CollectionUtil.isNotEmpty(newSpecList)) {
            newSpecList.forEach(item -> {
                PmsSpuAttribute entity = pmsSpuAttributeConverter.form2Entity(item);
                entity.setSpuId(spuId);
                entity.setType(AttributeTypeEnum.SPEC.getValue());   // 设置为规格类型
                pmsSpuAttributeService.save(entity);
                // 记录临时ID到数据库ID的映射
                log.info("前端生成的临时ID格式：temp_123");
                log.info("保存时转换为数据库ID并建立映射,临时ID -> 数据库ID");
                tempWithNewSpecIdMap.put(item.getId(), entity.getId());
            });
        }

        //  3. 【修改】此次提交的需要修改的商品规格
        log.info(" 3. 修改此次提交的已有规格");
        List<PmsSpuAttribute> pmsSpuAttributeList = specList.stream()
                .filter(item -> !item.getId().startsWith(ProductConstants.SPEC_TEMP_ID_PREFIX))
                .map(item -> {
                    PmsSpuAttribute entity = pmsSpuAttributeConverter.form2Entity(item);
                    entity.setId(Convert.toLong(item.getId()));
                    entity.setSpuId(spuId);
                    entity.setType(AttributeTypeEnum.SPEC.getValue());
                    return entity;
                }).collect(Collectors.toList());


        if (CollectionUtil.isNotEmpty(pmsSpuAttributeList)) {
            pmsSpuAttributeService.updateBatchById(pmsSpuAttributeList);
        }
        return tempWithNewSpecIdMap;
    }

    /**
     * 获取商品分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PmsSpuVO>} 商品分页列表
     */
    @Override
    public IPage<PmsSpuVO> getPmsSpuPage(PmsSpuQuery queryParams) {
        Page<PmsSpuVO> pageVO = this.baseMapper.getPmsSpuPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取商品表单数据
     *
     * @param id 商品ID
     * @return 商品表单数据
     */
    @Override
    public PmsSpuForm getPmsSpuFormData(Long id) {
        PmsSpu entity = this.getById(id);
        return pmsSpuConverter.toForm(entity);
    }

    /**
     * 新增商品
     *
     * @param formData 商品表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePmsSpu(PmsSpuForm formData) {
        PmsSpu entity = pmsSpuConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新商品
     *
     * @param id   商品ID
     * @param formData 商品表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePmsSpu(Long id,PmsSpuForm formData) {
        PmsSpu entity = pmsSpuConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除商品
     *
     * @param ids 商品ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePmsSpus(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的商品数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
