package com.aioveu.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aioveu.auth.common.model.SysConstant;
import com.aioveu.auth.model.vo.SysRolePermissionVO;
import com.aioveu.auth.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author 雒世松
 * 在项目启动时从数据库中将url->角色对应关系加载到Redis中
 */
@Service
public class LoadRolePermissionService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private SysPermissionService sysPermissionService;

    @PostConstruct
    public void init(){
        //获取所有的权限
        List<SysRolePermissionVO> list = sysPermissionService.listRolePermission();
        list.parallelStream().peek(k->{
            //放入Redis中
            List<String> roles = k.getRoles().stream().map(item -> SysConstant.ROLE_PREFIX + item).collect(Collectors.toList());
            redisTemplate.opsForHash().put(SysConstant.OAUTH_URLS, k.getUrl(), roles);
        }).collect(Collectors.toList());

        // 将白名单URL放进缓存中
        redisTemplate.delete(SysConstant.URL_WHITELIST);
        List<String> whiteListUrl = sysPermissionService.getWhiteListUrl();
        if (CollectionUtil.isNotEmpty(whiteListUrl)){
            whiteListUrl.parallelStream().forEach(item -> {
                redisTemplate.opsForList().rightPush(SysConstant.URL_WHITELIST, item);
            });
        }

    }

}
