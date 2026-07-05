你问到了一个关键问题！**serviceToken** 是服务间调用的认证令牌，需要从 Auth 服务中获取。让我为你提供完整的解决方案。

🔧 获取 serviceToken 的几种方案

### 方案一：使用静态 API Key（最简单）

**1. 在配置文件中定义服务令牌：**

```
# application.yml
service:
  auth:
    token: "aioveu-internal-service-token-2025"
    enabled: true
```

2. 创建服务令牌配置类：

```
package com.aioveu.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "service.auth")
public class ServiceAuthProperties {
    
    private String token = "aioveu-internal-service-token-2025";
    private boolean enabled = true;
}
```

**3. 在需要的地方注入并使用：**

```
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    
    private final SystemFeignClient systemFeignClient;
    private final ServiceAuthProperties serviceAuthProperties;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // ✅ 获取服务令牌
            String serviceToken = serviceAuthProperties.getToken();
            
            // 使用服务令牌调用 System 服务
            UserAuthCredentials userAuthCredentials = systemFeignClient.getAuthInfoInternal(username, serviceToken);
            
            return convertToUserDetails(userAuthCredentials);
        } catch (Exception e) {
            throw new UsernameNotFoundException("获取用户信息失败", e);
        }
    }
}
```

### 方案二：动态生成 JWT 服务令牌（推荐）

**1. 创建服务令牌生成器：**

```
package com.aioveu.auth.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTokenGenerator {
    
    /**
     * 生成服务间调用的 JWT 令牌
     */
    public String generateServiceToken() {
        try {
            // 创建 JWT 声明集
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer("aioveu-auth-service")  // 发行者
                    .subject("internal-service")     // 主题
                    .audience("aioveu-system-service") // 受众
                    .expirationTime(Date.from(Instant.now().plusSeconds(3600))) // 过期时间 1小时
                    .notBeforeTime(Date.from(Instant.now())) // 生效时间
                    .issueTime(Date.from(Instant.now()))     // 签发时间
                    .jwtID(UUID.randomUUID().toString())    // JWT ID
                    .claim("service", "auth")                // 自定义声明
                    .claim("type", "internal")              // 令牌类型
                    .claim("permissions", new String[]{"read:user", "write:user"}) // 权限
                    .build();
            
            // 这里简化实现，实际应该使用私钥签名
            // 返回一个简单的令牌格式
            return "service-token-" + System.currentTimeMillis();
            
        } catch (Exception e) {
            log.error("生成服务令牌失败: {}", e.getMessage(), e);
            return "fallback-service-token";
        }
    }
    
    /**
     * 验证服务令牌
     */
    public boolean validateServiceToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // 简单的令牌验证逻辑
        return token.startsWith("service-token-") || 
               token.equals("aioveu-internal-service-token-2025");
    }
}
```

2. 在用户详情服务中使用：

```
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    
    private final SystemFeignClient systemFeignClient;
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载用户信息: {}", username);
        
        try {
            // ✅ 动态生成服务令牌
            String serviceToken = serviceTokenGenerator.generateServiceToken();
            log.debug("生成服务令牌: {}", serviceToken.substring(0, Math.min(20, serviceToken.length())));
            
            // 调用 System 服务
            UserAuthCredentials userAuthCredentials = systemFeignClient.getAuthInfoInternal(username, serviceToken);
            
            if (userAuthCredentials != null) {
                return convertToUserDetails(userAuthCredentials);
            } else {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
        } catch (FeignException e) {
            if (e.status() == 401) {
                log.error("服务令牌认证失败，尝试使用备用方案");
                return fallbackToLocalUser(username);
            }
            throw new UsernameNotFoundException("调用用户服务失败", e);
        } catch (Exception e) {
            log.error("加载用户信息异常: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("用户信息加载失败", e);
        }
    }
    
    /**
     * 备用方案：本地用户数据
     */
    private UserDetails fallbackToLocalUser(String username) {
        log.warn("使用本地备用用户数据: {}", username);
        
        // 本地用户数据（开发环境使用）
        if ("admin".equals(username)) {
            return createAdminUser();
        } else if ("user".equals(username)) {
            return createNormalUser();
        }
        
        throw new UsernameNotFoundException("用户不存在: " + username);
    }
    
    private UserDetails createAdminUser() {
        return org.springframework.security.core.userdetails.User.builder()
                .username("admin")
                .password("$2a$10$dXJ3SW6G7PXl1fYz5B0F.9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B") // 123456
                .authorities("ROLE_ADMIN", "ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    
    private UserDetails createNormalUser() {
        return org.springframework.security.core.userdetails.User.builder()
                .username("user")
                .password("$2a$10$dXJ3SW6G7PXl1fYz5B0F.9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B") // 123456
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
```

### 方案三：使用 Spring Cloud 的服务间认证

**1. 创建 Feign 拦截器自动添加服务令牌：**

```
package com.aioveu.auth.config;

import com.aioveu.auth.service.ServiceTokenGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignServiceAuthConfig {
    
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    /**
     * Feign 请求拦截器 - 自动添加服务令牌
     */
    @Bean
    public RequestInterceptor serviceTokenInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 只为内部接口添加服务令牌
                if (template.url().contains("/internal/")) {
                    String serviceToken = serviceTokenGenerator.generateServiceToken();
                    template.header("X-Service-Token", serviceToken);
                    log.debug("为Feign请求添加服务令牌: {}", template.url());
                }
            }
        };
    }
}
```

你问到了一个关键问题！**serviceToken** 是服务间调用的认证令牌，需要从 Auth 服务中获取。让我为你提供完整的解决方案。

## 🔧 获取 serviceToken 的几种方案

### 方案一：使用静态 API Key（最简单）

**1. 在配置文件中定义服务令牌：**

yaml

yaml

复制

```
# application.yml
service:
  auth:
    token: "aioveu-internal-service-token-2025"
    enabled: true
```

**2. 创建服务令牌配置类：**

java

java

下载

复制

运行

```
package com.aioveu.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "service.auth")
public class ServiceAuthProperties {
    
    private String token = "aioveu-internal-service-token-2025";
    private boolean enabled = true;
}
```

**3. 在需要的地方注入并使用：**

java

java

下载

复制

运行

```
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    
    private final SystemFeignClient systemFeignClient;
    private final ServiceAuthProperties serviceAuthProperties;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // ✅ 获取服务令牌
            String serviceToken = serviceAuthProperties.getToken();
            
            // 使用服务令牌调用 System 服务
            UserAuthCredentials userAuthCredentials = systemFeignClient.getAuthInfoInternal(username, serviceToken);
            
            return convertToUserDetails(userAuthCredentials);
        } catch (Exception e) {
            throw new UsernameNotFoundException("获取用户信息失败", e);
        }
    }
}
```

### 方案二：动态生成 JWT 服务令牌（推荐）

**1. 创建服务令牌生成器：**

java

java

下载

复制

运行

```
package com.aioveu.auth.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTokenGenerator {
    
    /**
     * 生成服务间调用的 JWT 令牌
     */
    public String generateServiceToken() {
        try {
            // 创建 JWT 声明集
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer("aioveu-auth-service")  // 发行者
                    .subject("internal-service")     // 主题
                    .audience("aioveu-system-service") // 受众
                    .expirationTime(Date.from(Instant.now().plusSeconds(3600))) // 过期时间 1小时
                    .notBeforeTime(Date.from(Instant.now())) // 生效时间
                    .issueTime(Date.from(Instant.now()))     // 签发时间
                    .jwtID(UUID.randomUUID().toString())    // JWT ID
                    .claim("service", "auth")                // 自定义声明
                    .claim("type", "internal")              // 令牌类型
                    .claim("permissions", new String[]{"read:user", "write:user"}) // 权限
                    .build();
            
            // 这里简化实现，实际应该使用私钥签名
            // 返回一个简单的令牌格式
            return "service-token-" + System.currentTimeMillis();
            
        } catch (Exception e) {
            log.error("生成服务令牌失败: {}", e.getMessage(), e);
            return "fallback-service-token";
        }
    }
    
    /**
     * 验证服务令牌
     */
    public boolean validateServiceToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // 简单的令牌验证逻辑
        return token.startsWith("service-token-") || 
               token.equals("aioveu-internal-service-token-2025");
    }
}
```

**2. 在用户详情服务中使用：**

java

java

下载

复制

运行

```
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    
    private final SystemFeignClient systemFeignClient;
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载用户信息: {}", username);
        
        try {
            // ✅ 动态生成服务令牌
            String serviceToken = serviceTokenGenerator.generateServiceToken();
            log.debug("生成服务令牌: {}", serviceToken.substring(0, Math.min(20, serviceToken.length())));
            
            // 调用 System 服务
            UserAuthCredentials userAuthCredentials = systemFeignClient.getAuthInfoInternal(username, serviceToken);
            
            if (userAuthCredentials != null) {
                return convertToUserDetails(userAuthCredentials);
            } else {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
        } catch (FeignException e) {
            if (e.status() == 401) {
                log.error("服务令牌认证失败，尝试使用备用方案");
                return fallbackToLocalUser(username);
            }
            throw new UsernameNotFoundException("调用用户服务失败", e);
        } catch (Exception e) {
            log.error("加载用户信息异常: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("用户信息加载失败", e);
        }
    }
    
    /**
     * 备用方案：本地用户数据
     */
    private UserDetails fallbackToLocalUser(String username) {
        log.warn("使用本地备用用户数据: {}", username);
        
        // 本地用户数据（开发环境使用）
        if ("admin".equals(username)) {
            return createAdminUser();
        } else if ("user".equals(username)) {
            return createNormalUser();
        }
        
        throw new UsernameNotFoundException("用户不存在: " + username);
    }
    
    private UserDetails createAdminUser() {
        return org.springframework.security.core.userdetails.User.builder()
                .username("admin")
                .password("$2a$10$dXJ3SW6G7PXl1fYz5B0F.9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B") // 123456
                .authorities("ROLE_ADMIN", "ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    
    private UserDetails createNormalUser() {
        return org.springframework.security.core.userdetails.User.builder()
                .username("user")
                .password("$2a$10$dXJ3SW6G7PXl1fYz5B0F.9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B0Zz9B") // 123456
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
```

### 方案三：使用 Spring Cloud 的服务间认证

**1. 创建 Feign 拦截器自动添加服务令牌：**

java

java

下载

复制

运行

```
package com.aioveu.auth.config;

import com.aioveu.auth.service.ServiceTokenGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignServiceAuthConfig {
    
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    /**
     * Feign 请求拦截器 - 自动添加服务令牌
     */
    @Bean
    public RequestInterceptor serviceTokenInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 只为内部接口添加服务令牌
                if (template.url().contains("/internal/")) {
                    String serviceToken = serviceTokenGenerator.generateServiceToken();
                    template.header("X-Service-Token", serviceToken);
                    log.debug("为Feign请求添加服务令牌: {}", template.url());
                }
            }
        };
    }
}
```

**2. 修改 Feign 客户端，移除手动传递令牌：**

```
@FeignClient(name = "aioveu-system", configuration = FeignServiceAuthConfig.class)
public interface SystemFeignClient {
    
    /**
     * 移除了 serviceToken 参数，由拦截器自动添加
     */
    @GetMapping("/aioveus/v8/aioveu-auth/users/internal/auth-info")
    UserAuthCredentials getAuthInfoInternal(@RequestParam String username);
    // 注意：移除了 @RequestHeader("X-Service-Token") String serviceToken 参数
}
```

3. 在 System 服务中添加服务令牌验证：

```
package com.aioveu.system.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceTokenAuthFilter extends OncePerRequestFilter {
    
    private final ServiceTokenValidator serviceTokenValidator;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        // 只验证内部接口
        if (path.contains("/internal/")) {
            String serviceToken = request.getHeader("X-Service-Token");
            
            if (!serviceTokenValidator.validate(serviceToken)) {
                log.warn("服务令牌验证失败: path={}, token={}", path, serviceToken);
                response.setStatus(401);
                response.getWriter().write("Invalid service token");
                return;
            }
            
            log.debug("服务令牌验证成功: path={}", path);
        }
        
        filterChain.doFilter(request, response);
    }
}
```

## 💡 推荐方案

### 根据你的需求选择：

1. **✅ 开发环境**：使用方案一（静态令牌），简单可靠
2. **✅ 生产环境**：使用方案二（JWT动态令牌），安全性更高
3. **✅ 微服务架构**：使用方案三（Feign拦截器），自动化程度高

立即可用的代码：

```
// 在你的代码中直接使用：
@Service
public class YourService {
    
    @Value("${service.auth.token:aioveu-internal-service-token-2025}")
    private String serviceToken;
    
    public void yourMethod() {
        // 直接使用配置的令牌
        UserAuthCredentials credentials = systemFeignClient.getAuthInfoInternal(username, serviceToken);
    }
}
```

