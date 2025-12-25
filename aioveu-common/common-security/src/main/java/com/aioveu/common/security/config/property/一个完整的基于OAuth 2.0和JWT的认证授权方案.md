基于你的微服务架构，我来帮你设计一个完整的基于OAuth 2.0和JWT的认证授权方案。

## 1. 架构概览

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端/客户端     │────│   API网关        │────│   微服务集群      │
│                 │    │                 │    │                 │
│                 │    │  - 路由转发     │    │  auth-service   │
│                 │    │  - JWT验证     │    │  system-service │
└─────────────────┘    │  - 限流熔断     │    │  user-service   │
                       └─────────────────┘    └─────────────────┘
```

## 2. 密钥生成和管理

首先生成RSA密钥对：

```
# 生成私钥
openssl genrsa -out private.key 2048

# 生成公钥
openssl rsa -in private.key -pubout -out public.key
```

## 3. 项目结构和依赖

### 公共模块（common-security）

```
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>
</dependency>
```

### 公共安全配置类

```
// CommonSecurityConfig.java
@Component
public class JwtConfig {
    
    @Value("${jwt.private-key}")
    private String privateKey;
    
    @Value("${jwt.public-key}")
    private String publicKey;
    
    @Value("${jwt.expiration:3600}")
    private Long expiration;
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public Long getExpiration() {
        return expiration;
    }
}
```

## 4. 认证服务 (auth-service)

### 认证服务配置

```
# application.yml
server:
  port: 8081

spring:
  application:
    name: auth-service
  datasource:
    url: jdbc:mysql://localhost:3306/auth_db
    username: root
    password: password

jwt:
  private-key: classpath:keys/private.key
  public-key: classpath:keys/public.key
  expiration: 3600
```

### 认证控制器

```
// AuthController.java
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    
    @PostMapping("/token/verify")
    public ResponseEntity<JwtClaims> verifyToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyToken(token));
    }
    
    // 客户端凭证模式 - 服务间认证
    @PostMapping("/oauth/token")
    public ResponseEntity<AuthResponse> getClientToken(
            @RequestParam String client_id,
            @RequestParam String client_secret,
            @RequestParam String grant_type) {
        return ResponseEntity.ok(authService.getClientToken(client_id, client_secret));
    }
}

// DTO类
@Data
public class AuthResponse {
    private String access_token;
    private String token_type = "Bearer";
    private Long expires_in;
    private String scope;
    private UserInfo user;
}

@Data
public class LoginRequest {
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
}
```

### 认证服务实现

```
// AuthService.java
@Service
public class AuthService {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    @Autowired
    private UserService userService;
    
    public AuthResponse authenticate(LoginRequest request) {
        // 1. 验证用户凭证
        User user = userService.validateCredentials(request.getUsername(), request.getPassword());
        
        // 2. 生成JWT
        String token = generateToken(user);
        
        // 3. 返回响应
        AuthResponse response = new AuthResponse();
        response.setAccess_token(token);
        response.setExpires_in(jwtConfig.getExpiration());
        response.setUser(UserInfo.from(user));
        
        return response;
    }
    
    public AuthResponse getClientToken(String clientId, String clientSecret) {
        // 验证客户端凭证（数据库或配置中）
        Client client = validateClientCredentials(clientId, clientSecret);
        
        String token = Jwts.builder()
                .setSubject(clientId)
                .claim("type", "client")
                .claim("scope", client.getScopes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
                
        AuthResponse response = new AuthResponse();
        response.setAccess_token(token);
        response.setExpires_in(jwtConfig.getExpiration());
        response.setScope(client.getScopes());
        
        return response;
    }
    
    public JwtClaims verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .parseClaimsJws(token)
                    .getBody();
                    
            return JwtClaims.builder()
                    .subject(claims.getSubject())
                    .issuedAt(claims.getIssuedAt())
                    .expiration(claims.getExpiration())
                    .claims(claims)
                    .valid(true)
                    .build();
        } catch (Exception e) {
            return JwtClaims.builder().valid(false).build();
        }
    }
    
    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .claim("type", "user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }
    
    private PrivateKey getPrivateKey() {
        // 从字符串加载私钥
        // 实现略...
    }
    
    private PublicKey getPublicKey() {
        // 从字符串加载公钥
        // 实现略...
    }
}
```

## 5. 系统服务 (system-service)

### 安全配置

```
// SecurityConfig.java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

// JWT认证过滤器
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String token = resolveToken(request);
        
        if (token != null && validateToken(token)) {
            Authentication auth = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
                
        String username = claims.getSubject();
        List<GrantedAuthority> authorities = extractAuthorities(claims);
        
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
    
    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        // 从claims中提取权限
        // 实现略...
    }
}
```

## 6. API网关配置

### Spring Cloud Gateway 配置

```
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/auth/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
        
        - id: system-service
          uri: lb://system-service
          predicates:
            - Path=/api/**
          filters:
            - name: JwtAuthenticationFilter
            - StripPrefix=1
```

### 网关JWT过滤器

```
// Gateway JWT Filter
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private AuthServiceClient authServiceClient;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 放行认证端点
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            return chain.filter(exchange);
        }
        
        // 验证JWT
        String token = extractToken(exchange.getRequest());
        if (token == null) {
            return unauthorizedResponse(exchange, "Missing token");
        }
        
        try {
            JwtClaims claims = authServiceClient.verifyToken(token);
            if (!claims.isValid()) {
                return unauthorizedResponse(exchange, "Invalid token");
            }
            
            // 将用户信息添加到请求头
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Roles", String.join(",", claims.getRoles()))
                    .build();
                    
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return unauthorizedResponse(exchange, "Token verification failed");
        }
    }
}
```

## 7. 服务间调用认证

### Feign客户端配置

```
// Feign配置类
@Configuration
public class FeignConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 从安全上下文中获取当前服务的token
            String serviceToken = getServiceToken();
            template.header("Authorization", "Bearer " + serviceToken);
        };
    }
}

// 服务间调用的Feign客户端
@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserServiceClient {
    
    @GetMapping("/users/{userId}")
    ResponseEntity<User> getUserById(@PathVariable("userId") Long userId);
}
```

## 8. 客户端配置

创建客户端凭证配置文件：

```
# clients.yml
clients:
  web-frontend:
    clientId: web-app
    clientSecret: web-secret-2024
    scopes: read,write
    grantTypes: password,refresh_token
    
  system-service:
    clientId: system-service
    clientSecret: system-secret-2024
    scopes: service_internal
    grantTypes: client_credentials
```

## 9. 部署配置

### Docker Compose 示例

```
version: '3.8'
services:
  auth-service:
    build: ./auth-service
    environment:
      - JWT_PRIVATE_KEY=file:/app/keys/private.key
      - JWT_PUBLIC_KEY=file:/app/keys/public.key
    volumes:
      - ./keys:/app/keys:ro

  system-service:
    build: ./system-service
    environment:
      - JWT_PUBLIC_KEY=file:/app/keys/public.key
    volumes:
      - ./keys:/app/keys:ro
```

这个设计提供了：

- **安全的密钥管理**：使用RSA非对称加密
- **灵活的认证方式**：支持用户密码登录和客户端凭证
- **微服务友好的架构**：服务间认证和用户认证分离
- **集中式的权限控制**：通过网关统一认证
- **可扩展性**：易于添加新的微服务和客户端类型

你需要根据具体的业务需求调整角色权限、token有效期、刷新令牌等细节。