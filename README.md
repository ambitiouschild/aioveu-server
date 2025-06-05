# aioveu-server



##  🌱分支说明

| 说明    | 适配管理前端分支                                      | 适配移动端分支                                               |                                                              |
| ------- | ----------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ✅master | Java 17 + Spring Boot 3 + Spring Authorization Server | [aioveu-admin:main](https://github.com/ambitiouschild/aioveu-hair-admin) | [aioveu-app:main](https://github.com/ambitiouschild/aioveu-hair-app) |
| java8   | Java 8 + Spring Boot 2 + Spring Security OAuth2       |                                                              |                                                              |



##  🚀项目简介



[aioveu-server](https://github.com/ambitiouschild/aioveu-hair-server) 是基于Spring Boot 3 、Spring Cloud & Alibaba 2022、Vue3、Element-Plus、uni-app等全栈主流技术栈构建的开源商城项目，涉及 [微服务接口](https://github.com/ambitiouschild/aioveu-hair-server)、 [管理前端](https://github.com/ambitiouschild/aioveu-hair-admin)、 [微信小程序](https://github.com/ambitiouschild/aioveu-hair-app) 和 [APP应用](https://github.com/ambitiouschild/aioveu-hair-app)等多端的开发。

- 项目使用皆是当前主流前后端技术栈(持续更新...)，无过度自定义封装，易理解学习和二次扩展；
- Spring Boot 3 、SpringCloud & Alibaba 2022 一站式微服务开箱即用的解决方案；
- Spring Authorization Server 、 JWT 常用 OAuth2 授权模式扩展；
- 移动端采用终极跨平台解决方案 uni-app， 一套代码编译iOS、Android、H5和小程序等多个平台；
- Jenkins、K8s、Docker实现微服务持续集成与交付(CI/CD)。



##  🌈在线预览



| 项目       | 地址                                                         | 用户名/密码        |
| ---------- | ------------------------------------------------------------ | ------------------ |
| 管理端     | [aioveu-admin](https://github.com/ambitiouschild/aioveu-hair-admin) | admin/123456       |
| 移动端(H5) | [http://aioveu.com](http://aioveu.com)                       | 18866668888/666666 |
| 微信小程序 | 关注【可我不敌可爱】公众号                                   | 获取体验码申请体验 |



##  🍀源码地址



| Gitee    | Github                                                       | GitCode | Gitee |
| -------- | ------------------------------------------------------------ | ------- | ----- |
| 后端接口 | [aioveu-server](https://github.com/ambitiouschild/aioveu-hair-server) | -       | -     |
| 管理前端 | [aioveu-admin](https://github.com/ambitiouschild/aioveu-hair-admin) | -       | -     |
| 移动端   | [aioveu-app](https://github.com/ambitiouschild/aioveu-hair-app) | -       | -     |





##  📁目录结构



```
aioveu-server
├── docs  
    ├── nacos                       # Nacos配置
        ├── nacos_config.zip        # Nacos脚本   
    ├── sql                         # SQL脚本
        ├── mysql5                  # MySQL5脚本
        ├── mysql8                  # MySQL8脚本
├── aioveu-oms                      # 订单服务
    ├── oms-api                     # oms Feign接口
    ├── oms-boot                    # oms 管理接口
├── aioveu-pms                      # 商品服务
    ├── pms-api                     # pms Feign接口
├── aioveu-sms                      # 营销服务
    ├── sms-api                     # sms Feign接口
├── aioveu-ums                      # 会员服务
    ├── ums-api                     # ums Feign接口
├── aioveu-auth                     # 认证授权中心
├── aioveu-common                   # 公共模块
    ├── common-core                 # 基础依赖
    ├── common-apidoc               # 日志公共模块
    ├── common-mybatis              # Mybatis 公共模块
    ├── common-rabbitmq             # RabbitMQ 公共模块
    ├── common-redis                # Redis 公共模块
    ├── common-seata                # Seata 公共模块
    ├── common-security             # 资源服务器安全公共模块
    ├── common-web                  # Web 公共模块
    ├── common-sms                  # sms 公共模块
├── aioveu-gateway                  # 网关
├── aioveu-system                   # 系统服务
    ├── system-api                  # 系统Feign接口
    ├── system-boot                 # 系统管理接口
└── end       
```



##  🌌启动项目

环境要求



- JDK 17
- MySQL 8 或 MySQL 5.7
- Nacos 2.2+
- 

安装中间件



| Windows  | Linux                                                        | 是否必装                                                     |              |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------ |
| Nacos    | [Windows 安装 Nacos 2.2](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F130864925) | [Linux 安装 Nacos 2.3](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F132592040) | 是           |
| MySQL    | [Windows 安装 MySQL 8](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F133272887) | [Linux 安装 MySQL8](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F130398179) | 否(建议安装) |
| Redis    | [Windows 安装 Redis](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F133410293) | [Linux 安装 Redis](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F130439335) | 否(建议安装) |
| Seata    | [Windows 安装 Seata 1.6](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F133295970) | [Linux 安装 Seata 1.7](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F133376131) | 否           |
| RabbitMQ | /                                                            | [Linux 安装 RabbitMQ](https://gitee.com/link?target=https%3A%2F%2Fblog.csdn.net%2Fu013737132%2Farticle%2Fdetails%2F130439122) | 否           |

💡默认中间件使用aioveu线上的环境，其中线上 MySQL 数据是只读的，如果需要进行修改或删除操作，建议自己安装 MySQL。

初始化数据库



进入 `docs/sql` 目录 ， 根据 MySQL 版本选择对应的脚本；

先执行 [aioveu_database.sql]() 完成数据库的创建；

再执行 [aioveu_system.sql](https://gitee.com/youlaitech/youlai-mall/blob/master/docs%2Fsql%2Fmysql8%2Fyoulai_system.sql) 、[aioveu_oauth2_server.sql](https://gitee.com/youlaitech/youlai-mall/blob/master/docs%2Fsql%2Fmysql8%2Foauth2_server.sql)、aioveu_*.sql 完成数据表的创建和数据初始化。



导入Nacos配置

打开浏览器，地址栏输入 Nacos 控制台的地址 [http://localhost:8848/nacos](https://gitee.com/link?target=http%3A%2F%2Flocalhost%3A8848%2Fnacos) ；

输入用户名/密码：nacos/nacos ；

进入控制台，点击左侧菜单 `配置管理` → `配置列表` 进入列表页面，点击 `导入配置` 选择项目中的 `docs/nacos/nacos_config.zip` 文件。



修改Nacos配置



在共享配置文件 aioveu-common.yaml 中，包括 MySQL、Redis、RabbitMQ 和 Seata 的连接信息，默认是有aioveu线上的环境。

如果您有自己的环境，可以按需修改相应的配置信息。

如果没有自己的 MySQL、Redis、RabbitMQ 和 Seata 环境，可以直接使用默认的配置。



启动服务

- 进入 `aioveu-gateway` 模块的启动类 GatewayApplication 启动网关；
- 进入 `aioveu-auth` 模块的启动类 AuthApplication 启动认证授权中心；
- 进入 `aioveu-system` → `system-boot` 模块的启动类 SystemApplication 启动系统服务；
- 至此完成基础服务的启动，商城服务按需启动，启动方式和 `aioveu-system` 一致;
- 访问接口文档地址测试: [http://localhost:9999/doc.html](https://gitee.com/link?target=http%3A%2F%2Flocalhost%3A9999%2Fdoc.html)



##  📝开发文档



- [Spring Authorization Server 扩展 OAuth2 密码模式](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F134024381)
- [Spring Cloud Gateway + Knife4j 网关聚合和 OAuth2 密码模式测试](https://gitee.com/link?target=https%3A%2F%2Fyoulai.blog.csdn.net%2Farticle%2Fdetails%2F134081509)

##  💖加交流群

![可我不敌可爱公众号二维码1](F:\Coding\aioveu-hair\aioveu-hair-server\可我不敌可爱公众号二维码1.jpg)
