package com.aioveu.system.aioveu04Menu.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.constant.SystemConstants;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.common.model.KeyValue;
import com.aioveu.common.model.Option;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.system.aioveu03Role.service.RoleMenuService;
import com.aioveu.system.aioveu04Menu.converter.MenuConverter;
import com.aioveu.system.aioveu04Menu.mapper.MenuMapper;
import com.aioveu.system.aioveu04Menu.model.entity.Menu;
import com.aioveu.system.aioveu04Menu.model.form.MenuForm;
import com.aioveu.system.aioveu04Menu.model.query.MenuQuery;
import com.aioveu.system.aioveu04Menu.model.vo.MenuVO;
import com.aioveu.system.aioveu04Menu.model.vo.RouteVO;
import com.aioveu.system.aioveu04Menu.service.MenuService;
import com.aioveu.system.aioveu11Codegen.model.entity.GenConfig;
import com.aioveu.system.aioveu04Menu.enums.MenuTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: MenuServiceImpl
 * @Description //TODO  菜单服务实现类   负责菜单数据的增删改查、树形结构构建、路由生成等核心业务逻辑
 *                       @核心功能:
 *                       - 菜单的CRUD操作
 *                       - 菜单树形结构构建
 *                       - 前端路由生成
 *                       - 权限控制集成
 *                       - 代码生成集成
 *                      技术特性:
 *                       - 递归算法构建树形结构
 *                       - 缓存管理提高性能
 *                       - 类型安全枚举使用
 *                       - 数据转换器模式
 *                       - 权限验证集成
 *                      业务逻辑:
 *                       - 支持目录、菜单、按钮、外链四种菜单类型
 *                       - 自动生成路由路径和组件
 *                       - 树形路径维护父子关系
 *                       - 角色权限关联管理
 *                    这个MenuServiceImpl是一个功能完整、设计良好的菜单服务实现，涵盖了企业级应用中菜单管理的所有核心需求。
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:09
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 菜单数据转换器：负责Menu实体与VO/Form之间的转换
     * 使用Lombok的@RequiredArgsConstructor自动注入
     */
    private final MenuConverter menuConverter;


    /**
     * 角色菜单关联服务：用于权限缓存刷新
     */
    private final RoleMenuService roleMenuService;


    /**
     * TODO  获取菜单列表（树形结构）
     *           根据查询条件返回层次化的菜单数据，用于管理界面展示
     *
     * @param queryParams 菜单查询参数，包含关键词搜索等条件
     * @return List<MenuVO> 树形结构的菜单列表
     *
     *  TODO        @实现逻辑:
     *            1. 查询符合条件的菜单数据（平铺列表）
     *            2. 识别根节点（父节点不在菜单ID列表中的节点）
     *            3. 递归构建树形结构
     *            4. 返回层次化菜单数据
     *            @使用场景: 菜单管理页面的表格展示
     *            @性能考虑: 数据量大会有递归性能开销，可考虑缓存优化
     */
    @Override
    public List<MenuVO> listMenus(MenuQuery queryParams) {

        log.info(" 1. 查询菜单数据（按排序字段升序排列）");
        List<Menu> menus = this.list(new LambdaQueryWrapper<Menu>()
                .like(StrUtil.isNotBlank(queryParams.getKeywords()), Menu::getName, queryParams.getKeywords())
                .orderByAsc(Menu::getSort)
        );
        // 获取所有菜单ID
        log.info(" 2. 获取所有菜单ID用于识别根节点");
        Set<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toSet());

        // 获取所有父级ID
        log.info(" 3. 获取所有父级ID");
        Set<Long> parentIds = menus.stream()
                .map(Menu::getParentId)
                .collect(Collectors.toSet());

        // 获取根节点ID（递归的起点），即父节点ID中不包含在部门ID中的节点，注意这里不能拿顶级菜单 O 作为根节点，因为菜单筛选的时候 O 会被过滤掉
        log.info(" 4. 识别根节点：父节点ID不在菜单ID列表中的即为根节点");
        List<Long> rootIds = parentIds.stream()
                .filter(id -> !menuIds.contains(id))
                .toList();

        // 使用递归函数来构建菜单树
        log.info(" 5. 为每个根节点递归构建菜单树");
        return rootIds.stream()
                .flatMap(rootId -> buildMenuTree(rootId, menus).stream())
                .collect(Collectors.toList());
    }

    /**
     * 递归构建菜单树
     * 内部方法，通过递归将平铺的菜单列表转换为树形结构
     *
     * @param parentId 当前层级的父节点ID
     * @param menuList 完整的菜单数据列表
     * @return List<MenuVO> 当前父节点下的子菜单树
     *
     * @算法原理:
     *   - 基线条件：找到parentId的所有直接子节点
     *   - 递归条件：对每个子节点继续查找其子节点
     *   - 终止条件：某个节点没有子节点时递归结束
     *
     * @时间复杂度: O(n^2) - 可优化为O(n)使用Map缓存
     */
    private List<MenuVO> buildMenuTree(Long parentId, List<Menu> menuList) {
        return CollectionUtil.emptyIfNull(menuList)
                .stream()
                // 过滤出当前父节点的直接子节点
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    // 实体转VO
                    MenuVO menuVO = menuConverter.toVo(entity);
                    // 递归构建子节点
                    List<MenuVO> children = buildMenuTree(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).toList();
    }

    /**
     * 获取菜单下拉选项数据
     * 用于表单中的菜单选择器，支持只查询父级菜单（排除按钮）
     *
     * @param onlyParent 是否只查询父级菜单
     *   - true: 只返回目录和菜单类型（排除按钮）
     *   - false: 返回所有菜单类型
     * @return List<Option<Long>> 层次化的下拉选项数据
     *
     * @使用场景:
     *   - 新增/编辑菜单时的父菜单选择
     *   - 角色权限分配时的菜单选择
     *   - 其他需要菜单选择器的场景
     */
    @Override
    public List<Option<Long>> listMenuOptions(boolean onlyParent) {

        log.info("构建查询条件：根据onlyParent参数过滤菜单类型");
        List<Menu> menuList = this.list(new LambdaQueryWrapper<Menu>()
                // 只查询目录和菜单类型，排除按钮
                .in(onlyParent, Menu::getType, MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue())
                .orderByAsc(Menu::getSort)
        );

        // 从根节点开始递归构建下拉选项树
        return buildMenuOptions(SystemConstants.ROOT_NODE_ID, menuList);
    }

    /**
     * 递归构建菜单下拉选项
     * 将菜单列表转换为前端下拉组件需要的层次结构
     *
     * @param parentId 当前父节点ID
     * @param menuList 菜单数据列表
     * @return List<Option<Long>> 下拉选项树
     *
     * @数据结构:
     *   Option {
     *     value: 菜单ID,
     *     label: 菜单名称,
     *     children: [子选项列表]
     *   }
     */
    private List<Option<Long>> buildMenuOptions(Long parentId, List<Menu> menuList) {
        List<Option<Long>> menuOptions = new ArrayList<>();

        for (Menu menu : menuList) {

            // 找到当前父节点的直接子节点
            if (menu.getParentId().equals(parentId)) {
                // 创建选项对象
                Option<Long> option = new Option<>(menu.getId(), menu.getName());

                // 递归构建子选项
                List<Option<Long>> subMenuOptions = buildMenuOptions(menu.getId(), menuList);
                if (!subMenuOptions.isEmpty()) {
                    option.setChildren(subMenuOptions);
                }
                menuOptions.add(option);
            }
        }

        return menuOptions;
    }

    /**
     * TODO   获取当前用户的可访问路由列表
     *        根据用户角色生成前端路由配置，用于动态菜单和权限控制
     *
     * @return List<RouteVO> 当前用户有权限访问的路由列表
     *
     *   TODO       @权限逻辑:
     *            - 超级管理员：获取所有非按钮菜单
     *            - 普通用户：根据角色权限获取菜单
     *          @缓存策略: 使用@Cacheable缓存路由数据，提高性能
     *          @前端使用: 生成Vue Router配置，实现动态路由
     */
    @Override
    public List<RouteVO> getCurrentUserRoutes() {


        log.info("从安全上下文中获取当前用户角色");
        Set<String> roleCodes = SecurityUtils.getRoles();
        log.info("从安全上下文中获取当前用户角色：{}",roleCodes);

        log.info("无角色权限返回空列表");
        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptyList();
        }

        List<Menu> menuList;
        if (SecurityUtils.isRoot()) {
            // 超级管理员获取所有菜单
            log.info("超级管理员：获取所有非按钮菜单");
            menuList = this.list(new LambdaQueryWrapper<Menu>()
                    .ne(Menu::getType, MenuTypeEnum.BUTTON.getValue())  // 排除按钮类型
                    .orderByAsc(Menu::getSort)
            );
        } else {

            log.info("普通用户：根据角色编码查询有权限的菜单");
            menuList = this.baseMapper.getMenusByRoleCodes(roleCodes);
        }

        log.info("构建路由树形结构");
        return buildRoutes(SystemConstants.ROOT_NODE_ID, menuList);
    }

    /**
     * TODO  递归构建路由层级结构
     *          将菜单数据转换为前端路由配置
     *
     * @param parentId 父节点ID
     * @param menuList 菜单列表
     * @return List<RouteVO> 路由列表
     *
     * TODO @路由属性:
     *   - path: 路由路径
     *   - component: 路由组件
     *   - meta: 元数据（标题、图标等）
     *   - children: 子路由
     */
    private List<RouteVO> buildRoutes(Long parentId, List<Menu> menuList) {
        List<RouteVO> routeList = new ArrayList<>();

        for (Menu menu : menuList) {

            // 找到当前父节点的直接子节点
            if (menu.getParentId().equals(parentId)) {

                // 转换为路由VO
                RouteVO routeVO = toRouteVo(menu);

                // 递归构建子路由
                List<RouteVO> children = buildRoutes(menu.getId(), menuList);
                if (!children.isEmpty()) {
                    routeVO.setChildren(children);
                }
                routeList.add(routeVO);
            }
        }

        return routeList;
    }

    /**
     *    TODO          将Menu实体转换为RouteVO路由对象
     *              负责菜单数据到前端路由配置的映射转换
     *
     * @param menu 菜单实体
     * @return RouteVO 路由值对象
     *
     *   TODO           @转换规则:
     *                - 路由名称: 自动驼峰转换或使用配置值
     *                - 路由路径: 直接使用菜单配置
     *                - 组件路径: 根据菜单类型确定
     *                - 元数据: 标题、图标、缓存配置等
     */
    private RouteVO toRouteVo(Menu menu) {
        RouteVO routeVO = new RouteVO();
        // 获取路由名称

        log.info("1. 路由名称处理：支持自定义或自动生成");
        String routeName = menu.getRouteName();
        if (StrUtil.isBlank(routeName)) {
            // 路由 name 需要驼峰，首字母大写
            log.info("自动生成：路径转驼峰，首字母大写");
            routeName = StringUtils.capitalize(StrUtil.toCamelCase(menu.getRoutePath(), '-'));
        }
        // 根据name路由跳转 this.$router.push({name:xxx})
        routeVO.setName(routeName);

        // 根据path路由跳转 this.$router.push({path:xxx})
        log.info("2. 基础路由属性");
        routeVO.setPath(menu.getRoutePath());   // 路由路径
        routeVO.setRedirect(menu.getRedirect());  // 重定向路径
        routeVO.setComponent(menu.getComponent());  // 组件路径

        log.info("3. 构建路由元数据");
        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle(menu.getName());         // 菜单标题
        meta.setIcon(menu.getIcon());          // 菜单图标
        meta.setHidden(StatusEnum.DISABLE.getValue().equals(menu.getVisible()));  // 是否隐藏


        // 【菜单】是否开启页面缓存
        log.info("4. 页面缓存配置：只有菜单类型且开启缓存时设置");
        if (MenuTypeEnum.MENU.getValue().equals(menu.getType())
                && ObjectUtil.equals(menu.getKeepAlive(), 1)) {
            meta.setKeepAlive(true);
        }

        log.info("5. 始终显示配置：即使只有一个子路由也显示父级");
        meta.setAlwaysShow(ObjectUtil.equals(menu.getAlwaysShow(), 1));

        log.info("6. 路由参数处理：JSON字符串转Map");
        String paramsJson = menu.getParams();
        // 将 JSON 字符串转换为 Map<String, String>
        if (StrUtil.isNotBlank(paramsJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> paramMap = objectMapper.readValue(paramsJson, new TypeReference<>() {
                });
                meta.setParams(paramMap);
            } catch (Exception e) {
                throw new RuntimeException("解析参数失败", e);
            }
        }
        routeVO.setMeta(meta);
        return routeVO;
    }

    /**
     *    TODO      新增/修改菜单
     *          菜单数据的创建和更新，包含业务规则校验和树形路径维护
     *
     *          @param menuForm 菜单表单数据
     *          @return boolean 操作是否成功
     *
     *   TODO      @业务规则:
     *            - 目录类型：路径格式校验，组件设置为Layout
     *            - 外链类型：组件设置为null
     *            - 父菜单不能为自身
     *            - 菜单类型的路由名称必须唯一
     *             @缓存策略: 操作成功后清除路由缓存
     *             @权限刷新: 编辑菜单时刷新角色权限缓存
     */
    @Override
    @CacheEvict(cacheNames = "menu", key = "'routes'")   // 清除路由缓存
    public boolean saveMenu(MenuForm menuForm) {

        Integer menuType = menuForm.getType();

        log.info("1. 根据菜单类型进行特殊处理");
        if (MenuTypeEnum.CATALOG.getValue().equals(menuType)) {  // 如果是目录

            // 目录类型处理
            String path = menuForm.getRoutePath();

            log.info("一级目录路径必须以/开头");
            if (menuForm.getParentId() == 0 && !path.startsWith("/")) {
                menuForm.setRoutePath("/" + path); // 一级目录需以 / 开头
            }

            log.info("目录使用布局组件");
            menuForm.setComponent("Layout");
        } else if (MenuTypeEnum.EXTLINK.getValue().equals(menuType)) {
            // 外链菜单组件设置为 null
            log.info("外链类型：组件设置为null");
            menuForm.setComponent(null);
        }

        log.info("2. 校验父菜单不能为自身");
        if (Objects.equals(menuForm.getParentId(), menuForm.getId())) {
            throw new RuntimeException("父级菜单不能为当前菜单");
        }

        log.info("3. 表单数据转实体");
        Menu entity = menuConverter.toEntity(menuForm);

        log.info("4. 生成树形路径");
        String treePath = generateMenuTreePath(menuForm.getParentId());
        entity.setTreePath(treePath);

        log.info("5. 处理路由参数：List<KeyValue>转JSON字符串");
        List<KeyValue> params = menuForm.getParams();
        // 路由参数 [{key:"id",value:"1"}，{key:"name",value:"张三"}] 转换为 [{"id":"1"},{"name":"张三"}]
        if (CollectionUtil.isNotEmpty(params)) {
            entity.setParams(JSONUtil.toJsonStr(params.stream()
                    .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue))));
        } else {
            entity.setParams(null);
        }
        // 新增类型为菜单时候 路由名称唯一
        log.info("6. 菜单类型路由名称唯一性校验");
        if (MenuTypeEnum.MENU.getValue().equals(menuType)) {
            // 检查路由名称是否已存在（排除自身）
            Assert.isFalse(this.exists(new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getRouteName, entity.getRouteName())
                    .ne(menuForm.getId() != null, Menu::getId, menuForm.getId())
            ), "路由名称已存在");
        } else {
            // 其他类型时 给路由名称赋值为空
            entity.setRouteName(null);
        }


        log.info("7. 保存菜单数据");
        boolean result = this.saveOrUpdate(entity);

        log.info("8. 成功后处理");
        if (result) {
            // 编辑刷新角色权限缓存
            log.info("编辑操作刷新角色权限缓存");
            if (menuForm.getId() != null) {
                roleMenuService.refreshRolePermsCache();
            }
        }
        // 修改菜单如果有子菜单，则更新子菜单的树路径
        log.info("更新子菜单的树路径（如果父菜单路径变化）");
        updateChildrenTreePath(entity.getId(), treePath);
        return result;
    }

    /**
     *    TODO          递归更新子菜单树路径
     *              当父菜单的树路径变化时，同步更新所有子菜单的树路径
     *
     * @param id 当前菜单ID
     * @param treePath 当前菜单的树路径
     *
     *     TODO         @算法说明: 深度优先遍历更新所有子节点
     *              @性能考虑: 子菜单多时批量更新优化
     */
    private void updateChildrenTreePath(Long id, String treePath) {

        log.info("查询直接子菜单");
        List<Menu> children = this.list(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, id));
        if (CollectionUtil.isNotEmpty(children)) {
            // 子菜单的树路径等于父菜单的树路径加上父菜单ID

            log.info("子菜单树路径 = 父菜单树路径 + 父菜单ID");
            String childTreePath = treePath + "," + id;

            log.info("批量更新直接子菜单的树路径");
            this.update(new LambdaUpdateWrapper<Menu>()
                    .eq(Menu::getParentId, id)
                    .set(Menu::getTreePath, childTreePath)
            );

            log.info("递归更新孙子菜单");
            for (Menu child : children) {
                // 递归更新子菜单
                updateChildrenTreePath(child.getId(), childTreePath);
            }
        }
    }

    /**
     *      TODO            生成菜单树路径
     *                  树路径用于快速查询子节点和祖先节点，格式如：0,1,2
     *
     * @param parentId 父菜单ID
     * @return String 树路径字符串
     *
     *       TODO               @路径格式: 祖先ID,父ID,当前ID
     *                      @示例: 顶级菜单：0
     *                             二级菜单：0,1
     *                             三级菜单：0,1,2
     */
    private String generateMenuTreePath(Long parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {

            log.info("根节点的树路径就是自身ID");
            return String.valueOf(parentId);
        } else {

            log.info("非根节点：父路径 + 当前父ID");
            Menu parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }


    /**
     *          TODO            修改菜单显示状态
     *                      控制菜单在前端是否显示（用于菜单的启用/禁用）
     *
     * @param menuId 菜单ID
     * @param visible 显示状态
     *   - 1: 显示
     *   - 2: 隐藏
     * @return boolean 操作是否成功
     *
     *           TODO                @缓存策略: 清除路由缓存，确保前端及时更新
     *                          @前端效果: 隐藏的菜单不会在导航中显示
     */
    @Override
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public boolean updateMenuVisible(Long menuId, Integer visible) {
        return this.update(new LambdaUpdateWrapper<Menu>()
                .eq(Menu::getId, menuId)
                .set(Menu::getVisible, visible)
        );
    }

    /**
     *     TODO         获取菜单表单数据
     *              用于菜单编辑时的数据回显，包含参数格式转换
     *
     * @param id 菜单ID
     * @return MenuForm 菜单表单数据
     *
     *      TODO            @参数转换: 将数据库中的JSON参数转换为前端需要的List<KeyValue>格式
     *                  @格式示例:
     *                    数据库: {"id":"1","name":"张三"}
     *                    前端: [{key:"id",value:"1"},{key:"name",value:"张三"}]
     */
    @Override
    public MenuForm getMenuForm(Long id) {
        Menu entity = this.getById(id);
        Assert.isTrue(entity != null, "菜单不存在");

        log.info("基础数据转换");
        MenuForm formData = menuConverter.toForm(entity);
        // 路由参数字符串 {"id":"1","name":"张三"} 转换为 [{key:"id", value:"1"}, {key:"name", value:"张三"}]

        // 路由参数转换：JSON字符串转List<KeyValue>
        String params = entity.getParams();
        if (StrUtil.isNotBlank(params)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 解析 JSON 字符串为 Map<String, String>
                log.info("解析JSON为Map");
                Map<String, String> paramMap = objectMapper.readValue(params, new TypeReference<>() {
                });

                // 转换为 List<KeyValue> 格式 [{key:"id", value:"1"}, {key:"name", value:"张三"}]

                log.info("Map转List<KeyValue>");
                List<KeyValue> transformedList = paramMap.entrySet().stream()
                        .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                        .toList();

                // 将转换后的列表存入 MenuForm
                log.info("将转换后的列表存入 MenuForm");
                formData.setParams(transformedList);
            } catch (Exception e) {
                throw new RuntimeException("解析参数失败", e);
            }
        }

        return formData;
    }

    /**
     *      TODO        删除菜单
     *              删除菜单及其所有子菜单，并刷新权限缓存
     *
     * @param id 菜单ID
     * @return boolean 删除是否成功
     *
     *    TODO          @删除策略: 使用like查询删除所有子节点
     *              @SQL示例: WHERE id = ? OR tree_path LIKE '%,?,%'
     *              @权限清理: 删除后刷新角色权限缓存
     */
    @Override
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public boolean deleteMenu(Long id) {

        log.info("删除菜单及其所有子菜单（通过tree_path模糊匹配）");
        boolean result = this.remove(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getId, id)
                .or()
                .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", id));


        // 刷新角色权限缓存
        if (result) {
            roleMenuService.refreshRolePermsCache();
        }
        return result;

    }

    /**
     *     TODO             代码生成时自动创建菜单
     *                  代码生成工具调用，为生成的模块自动创建菜单和按钮权限
     *
     * @param parentMenuId 父菜单ID
     * @param genConfig 代码生成配置
     *
     *        Todo              @生成内容:
     *                        - 1个菜单项（对应生成的功能模块）
     *                        - 4个按钮权限（增删改查）
     *                      @使用场景: 代码生成器生成CRUD功能时自动创建对应菜单
     */
    @Override
    public void addMenuForCodegen(Long parentMenuId, GenConfig genConfig) {

        log.info("1. 验证父菜单存在");
        Menu parentMenu = this.getById(parentMenuId);
        Assert.notNull(parentMenu, "上级菜单不存在");

        String entityName = genConfig.getEntityName();

        log.info("2. 检查菜单是否已存在（避免重复创建）");
        long count = this.count(new LambdaQueryWrapper<Menu>().eq(Menu::getRouteName, entityName));
        if (count > 0) {
            return;
        }

        // 获取父级菜单子菜单最带的排序
        log.info(" 3. 计算排序值：取父菜单下最大排序值+1");
        Menu maxSortMenu = this.getOne(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, parentMenuId)
                .orderByDesc(Menu::getSort)
                .last("limit 1")
        );
        int sort = 1;
        if (maxSortMenu != null) {
            sort = maxSortMenu.getSort() + 1;
        }

        log.info(" 4. 创建主菜单");
        Menu menu = new Menu();
        menu.setParentId(parentMenuId);
        menu.setName(genConfig.getBusinessName());  // 业务名称作为菜单名


        log.info(" 路由配置");
        menu.setRouteName(entityName);  // 实体名作为路由名
        menu.setRoutePath(StrUtil.toSymbolCase(entityName, '-'));  // 转kebab-case
        menu.setComponent(genConfig.getModuleName() + "/" + StrUtil.toSymbolCase(entityName, '-') + "/index");
        menu.setType(MenuTypeEnum.MENU.getValue());
        menu.setSort(sort);
        menu.setVisible(1);   // 默认显示
        boolean result = this.save(menu);

        if (result) {
            // 生成treePath
            log.info("5. 生成树路径并更新");
            String treePath = generateMenuTreePath(parentMenuId);
            menu.setTreePath(treePath);
            this.updateById(menu);

            // 生成CURD按钮权限
            log.info("6. 生成CRUD按钮权限");
            String permPrefix = genConfig.getModuleName() + ":" + genConfig.getTableName().replace("_", "-") + ":";
            String[] actions = {"查询", "新增", "编辑", "删除"};
            String[] perms = {"query", "add", "edit", "delete"};

            for (int i = 0; i < actions.length; i++) {
                Menu button = new Menu();
                button.setParentId(menu.getId());
                button.setType(MenuTypeEnum.BUTTON.getValue());
                button.setName(actions[i]);  // 按钮显示名称
                button.setPerm(permPrefix + perms[i]);  // 权限标识
                button.setSort(i + 1);
                this.save(button);

                // 生成treePath
                button.setTreePath(treePath + "," + button.getId());
                this.updateById(button);
            }
        }
    }
}
