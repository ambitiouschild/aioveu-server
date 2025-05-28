# aioveu-server
aioveu-server

#aioveu_MyBatis-Plus_Generator

---------------------------------------------
Seata
这里我们创建三个服务，一个订单服务，一个库存服务，一个账户服务。-------最下面还有笔记

当用户下单时，会在订单服务中创建一个订单，然后通过远程调用库存服务来扣减下单商品的库存，

再通过远程调用账户服务来扣减用户账户里面的余额，

最后在订单服务中修改订单状态为已完成。该操作跨越三个数据库，有两次远程调用，很明显会有分布式事务问题。

---------------------------------------------
Sentinel
D:\SoftWares\sentinel-dashboard-1.8.6
java -jar sentinel-dashboard-1.8.6.jar

localhost:8080

---------------------------------------------

Nacos


D:\SoftWares\nacos-server2.2.3\bin
startup.cmd -m standalone

localhost:8848/nacos

---------------------------------------------

【aioveu微服务架构】【0.1】




通用模块
aioveu_commons
时间格式问题
统一返回值
全局异常接入返回的标准格式



---------------------------------------------

mybatis-generator
https://mybatis.org/generator
mybatis通用Mapper4

src\main\resources
config.properties
generatorConfig.xml

mybatisplus生成实体类
在MyBatis-Plus中，生成实体类（Entity）通常是通过逆向工程（Reverse Engineering）来实现的。MyBatis-Plus 提供了一种便捷的方式来根据数据库表自动生成实体类，这样可以大大提高开发效率，避免手动编写大量的实体类代码。

使用 MyBatis-Plus Generator 生成实体类
MyBatis-Plus Generator 是一个强大的代码生成器，可以基于数据库表结构自动生成实体类、Mapper 接口、XML 文件等。以下是如何使用 MyBatis-Plus Generator 来生成实体类的步骤：


Baomidou是中国团队开发的MyBatis增强工具包（MyBatis-Plus）的开发者团队‌，其官网（baomidou.com）明确标注了国内代码托管平台Gitee的链接（https://gitee.com/baomidou/mybatis-plus），且官方文档发布地址为中文域名（http://mp.baomidou.com/）。

关键证据：
‌团队归属与开源地址‌：

MyBatis-Plus的代码托管在国内外平台（GitHub和Gitee），但Gitee作为国内平台是主要发布渠道之一。
官方文档域名直接使用中文拼音"baomidou"，符合国内开发者的命名习惯。
‌相关项目生态‌：

Baomidou团队的其他开源产品（如MybatisX、Mybatis-Mate等）均面向国内开发者，部分工具（如FlowLong）专门针对“中国特色审批流”设计。
‌时效性与权威性‌：

尽管搜索结果中的时效性一般（2023年），但的时效性非常高（2025年），且内容直接来源于团队官网，权威性更高。
综上，‌Baomidou是中国本土团队‌，其核心产品MyBatis-Plus广泛应用于国内Java开发领域。

1. 添加依赖
   首先，确保你的项目中已经添加了 MyBatis-Plus 和 MyBatis-Plus Generator 的依赖。如果你使用的是 Maven，可以在 pom.xml 文件中添加如下依赖：
<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>你的MyBatis-Plus版本</version>
</dependency>
<!-- MyBatis-Plus Generator -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>你的MyBatis-Plus Generator版本</version>
</dependency>
<!-- 数据库驱动，根据实际使用的数据库选择 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>你的数据库驱动版本</version>
</dependency>

2. 配置代码生成器
   创建一个配置类来配置代码生成器，例如 CodeGenerator.java：
   import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
   import com.baomidou.mybatisplus.core.toolkit.StringPool;
   import com.baomidou.mybatisplus.core.toolkit.StringUtils;
   import com.baomidou.mybatisplus.generator.AutoGenerator;
   import com.baomidou.mybatisplus.generator.config.*;
   import com.baomidou.mybatisplus.generator.config.rules.DateType;
   import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
   import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Scanner;

public class CodeGenerator {

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
 
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
 
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("作者名");
        gc.setOpen(false); // 是否打开资源管理器
        gc.setFileOverride(true); // 是否覆盖文件
        gc.setServiceName("%sService"); // 默认Service名称，%s会自动填充表名，默认ServiceImpl会加Impl后缀，如果想自定义可以不加%s, 如UserServiceImpl, UserMapper等。
        gc.setIdType(IdType.AUTO); // 主键策略，默认自增，可选的有NONE, AUTO, INPUT, ASSIGN_ID, ASSIGN_UUID, ...等。
        gc.setDateType(DateType.ONLY_DATE); // 定义生成的实体类中日期类型，默认是LocalDateTime。可选的有ONLY_DATE, TIME_PACK, ONLY_TIME等。
        mpg.setGlobalConfig(gc);
 
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/数据库名?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername

---------------------------------------------




---------------------------------------------

mybatis-generator
https://mybatis.org/generator
mybatis通用Mapper4

src\main\resources
config.properties
generatorConfig.xml

mybatisplus生成实体类
在MyBatis-Plus中，生成实体类（Entity）通常是通过逆向工程（Reverse Engineering）来实现的。MyBatis-Plus 提供了一种便捷的方式来根据数据库表自动生成实体类，这样可以大大提高开发效率，避免手动编写大量的实体类代码。

使用 MyBatis-Plus Generator 生成实体类
MyBatis-Plus Generator 是一个强大的代码生成器，可以基于数据库表结构自动生成实体类、Mapper 接口、XML 文件等。以下是如何使用 MyBatis-Plus Generator 来生成实体类的步骤：


Baomidou是中国团队开发的MyBatis增强工具包（MyBatis-Plus）的开发者团队‌，其官网（baomidou.com）明确标注了国内代码托管平台Gitee的链接（https://gitee.com/baomidou/mybatis-plus），且官方文档发布地址为中文域名（http://mp.baomidou.com/）。

关键证据：
‌团队归属与开源地址‌：

MyBatis-Plus的代码托管在国内外平台（GitHub和Gitee），但Gitee作为国内平台是主要发布渠道之一。
官方文档域名直接使用中文拼音"baomidou"，符合国内开发者的命名习惯。
‌相关项目生态‌：

Baomidou团队的其他开源产品（如MybatisX、Mybatis-Mate等）均面向国内开发者，部分工具（如FlowLong）专门针对“中国特色审批流”设计。
‌时效性与权威性‌：

尽管搜索结果中的时效性一般（2023年），但的时效性非常高（2025年），且内容直接来源于团队官网，权威性更高。
综上，‌Baomidou是中国本土团队‌，其核心产品MyBatis-Plus广泛应用于国内Java开发领域。

1. 添加依赖
   首先，确保你的项目中已经添加了 MyBatis-Plus 和 MyBatis-Plus Generator 的依赖。如果你使用的是 Maven，可以在 pom.xml 文件中添加如下依赖：
<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>你的MyBatis-Plus版本</version>
</dependency>
<!-- MyBatis-Plus Generator -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>你的MyBatis-Plus Generator版本</version>
</dependency>
<!-- 数据库驱动，根据实际使用的数据库选择 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>你的数据库驱动版本</version>
</dependency>

2. 配置代码生成器
   创建一个配置类来配置代码生成器，例如 CodeGenerator.java：
   import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
   import com.baomidou.mybatisplus.core.toolkit.StringPool;
   import com.baomidou.mybatisplus.core.toolkit.StringUtils;
   import com.baomidou.mybatisplus.generator.AutoGenerator;
   import com.baomidou.mybatisplus.generator.config.*;
   import com.baomidou.mybatisplus.generator.config.rules.DateType;
   import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
   import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Scanner;

public class CodeGenerator {

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
 
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
 
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("作者名");
        gc.setOpen(false); // 是否打开资源管理器
        gc.setFileOverride(true); // 是否覆盖文件
        gc.setServiceName("%sService"); // 默认Service名称，%s会自动填充表名，默认ServiceImpl会加Impl后缀，如果想自定义可以不加%s, 如UserServiceImpl, UserMapper等。
        gc.setIdType(IdType.AUTO); // 主键策略，默认自增，可选的有NONE, AUTO, INPUT, ASSIGN_ID, ASSIGN_UUID, ...等。
        gc.setDateType(DateType.ONLY_DATE); // 定义生成的实体类中日期类型，默认是LocalDateTime。可选的有ONLY_DATE, TIME_PACK, ONLY_TIME等。
        mpg.setGlobalConfig(gc);
 
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/数据库名?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername

---------------------------------------------


微服务架构编码Base工程模块构建
订单-支付，业务需求说明

约定>配置>编码
Only Do It

微服务cloud整体聚合Maven父工程Project
Maven父工程步骤
1.New Project：只需要一个pom文件
2.聚合总父工程名字:
3.字符编码：
4.注解生效激活
5.java编译版本选17
6.File Type过滤


dependencyManagement
Maven 使用dependencyManagement 元素来提供了一种管理依赖版本号的方式。
通常会在一个组织或者项目的最顶层的父POM 中看到dependencyManagement 元素。
使用pom.xml 中的dependencyManagement 元素能让所有在子项目中引用一个依赖而不用显式的列出版本号。
Maven会沿着父子层次向上走，直到找到一个拥有dependencyManagement 元素的项目，然后它就会使用这个
dependencyManagement 元素中指定的版本号。

这样做的好处就是：如果有多个子项目都引用同一样依赖，则可以避免在每个使用的子项目里都声明一个版本号，优势：
1 这样当想升级或切换到另一个版本时，只需要在顶层父容器里更新，而不需要一个一个子项目的修改 ；
2 另外如果某个子项目需要另外的一个版本，只需要声明version就可。
*     dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。
*   如果不在子项目中声明依赖，是不会从父项目中继承下来的，只有在子项目中写了该依赖项并且没有指定具体版本，才会从父项目中继承该项        且version和scope都读取自父pom;
*     如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。


---------------------------------------------



【aioveu版本选择】boot, cloud, cloud alibaba【0.1】

若同时使用boot和cloud,由话事人cloud决定boot版本
Java: Java17+

cloud: 2024.0.1
Spring-Cloud版本选择
https://github.com/spring-cloud
https://spring.io/projects/spring-cloud#overview

<properties>
    <spring-cloud.version>2024.0.0</spring-cloud.version>
</properties>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

让程序员专注于业务逻辑，有第三方支撑
Spring Cloud 2025微服务
Spring Cloud 微服务对应的组件支撑
Spring Cloud 2025升级

boot: 3.5.0
Spring-Boot版本选择
https://github.com/spring-projects/spring-boot/releases/
Spring-Boot版本说明
https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes

Spring Boot 3.0 requires Java 17 as a minimum version. If you are currently using Java 8 or Java 11,
you’ll need to upgrade your JDK before you can develop Spring Boot 3.0 applications.
Spring Boot 3.0 also works well, and has been tested with JDK 19.


cloud alibaba : 2023.0.1.0
Spring-Cloud-Alibaba版本选择
https://github.com/alibaba/spring-cloud-alibaba/wiki/
https://github.com/alibaba/spring-cloud-alibaba?spm=5176.29160081.0.0.74805c72d83SmI
2023.x branch: Corresponds to Spring Cloud 2023 & Spring Boot 3.2.x, JDK 17 or later versions are supported.
https://sca.aliyun.com/docs/2023/overview/version-explain/?spm=5176.29160081.0.0.74805c7286RlIq

Spring Cloud Alibaba Version	Spring Cloud Version	Spring Boot Version
2023.0.1.0*	Spring Cloud 2023.0.1	3.2.4
2023.0.0.0-RC1	Spring Cloud 2023.0.0	3.2.0

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2023.0.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

Maven: 3.9+
MySQL: 8.0+

【aioveu微服务架构】【0.1】

---------------------------------------------
【服务注册与发现】 Consul  Alibaba Nacos
【服务调用和负载均衡】 LoadBalancer OpenFeign
【分布式事务】Alibaba Seata
【服务熔断和降级】Circuit Breaker Alibaba Sentinel
【服务链路追踪】Micrometer Tracing
【服务网关】Gateway
【分布式配置管理】Consul Alibaba Nacos


---------------------------------------------
GroupId 的命名规则
反向域名格式：推荐使用反向域名命名，如 com.example、org.apache，这样可以避免命名冲突。
唯一性：每个 GroupId 应该唯一地标识一个组织、公司或开源项目。
与 Java 包一致：通常，GroupId 会与项目的 Java 包名一致，这样可以保证它的唯一性。
————————————————
<groupId>com.aioveu</groupId>

小程序，这20个坑一定不踩

1.客户在我们小程序付的钱，是要被扣手续费的
默认是千分之6
如果你找的小程序是支付接口服务商，最低可以申请到千分之2
注意：不是所有的小程序公司都可以申请到千2，只有接口服务商才行

2.有的小程序公司帮你从千6调整成千2
是需要找你收一笔调整的服务费用的
你可以多找一找，有的是免费就可以帮你调的

3.做小程序是需要购买域名的，com,cn,都可以用，没什么区别，那种便宜就买那种，目前cn最便宜，34元/年

4.市面上所有的小程序都可以分成2种类型：
有独立源小程序和没有独立源码小程序，
没有独立源码的也叫SAAS模板，别弄混淆了！！

5.有独立源码的小程序，不用担心软件公司突然倒闭，你小程序就没有了，并且后期小程序的功能可以修改和增加，
没有独立源码的小程序(SAAS)一旦软件公司倒闭，你小程序就没有了，并且小程序是不能修改和增加功能的，这个一定要注意下区别

6.做小程序是需要有营业执照的，个人目前做不了小程序。有的个人做的小程序，是入驻开店，大家留意一下手续费问题

7.没有独立源码的小程序，也就是SAAS模板。是不需要自己去购买域名和服务器的，因为你只是入驻在里面，不需要购买，一定要留意下

8.做小程序需要用到SSL域名证书，这个可以找软件公司申请一个免费使用，不要花钱购买

9.小程序上线之前，是需要进行域名ICP备案和小程序备案的，这两项都是免费可以办理的，不用花钱
注意，域名ICP备案和ICP经营许可证是2个东西，注意区分

10.小程序可以绑定在公众号里面，这样用户关注你公众号了，可以通过公众号打开你的小程序，公众号分：订阅号和服务号，订阅号可以每天发一次推文
服务号一个月可以发推文4次，千万不要申请错了

11.小程序认证默认是300元/年，但如果你是个体工商户营业执照，只需要30元/年
注意，公司执照，客人付钱进的是你的对公账户，个体工商户进的是法人的银行卡账户

12.认证小程序之前，可以先花300认证一个公众号，
这样在公众号里面免费开通小程序认证，这样就可以300得到一个小程序认证和公众号认证

13.不是所有的小程序，都需要定制开发，一些常见类型的比如商场，人才招聘，交友等，很多软件公司也有一些开发好的成品源代码，价格比
定制开发要便宜很多，而且也有独立源代码.

14.小程序打开速度快慢，跟你的服务器配置有很大关系，一般2-3000的可以,人多了，可以随时升级配置，很方便

15.定制开发小程序，一定要找懂技术的人员沟通，
防止后面开发出来的不是自己想要的。后面软件公司跟你扯皮，让你加钱。

16.做小程序之前，一定要问小程序开发公司源代码可是免费给你提供的，防止后面你要的话，他们会找你加钱

17.源代码有盗版的，破解的，还有网上免费下载的，一定要问是不是他们原创开发的，要不然后期啊你被起诉版权侵权，一啊不能都是10w以上，这个一定要在合同里面备注清楚。

18.跟小程序开发公司签合同的时候，一定要把小程序里面的功能细节全部加到合同里面去，包括软件公司口头答应给你做的功能全部列进去，然后你按照这个验收，防止他们扯皮

19.找小程序开发公司，最好找干时间长的公司合作，比如5年,10年以上，防止他们突然倒闭了，不干了，你小程序没人接手维护。

20.一些特殊类型的小程序，比如交友，多商家入驻等，是需要办ICP经营许可证的次啊能上线的，开发前，一定要问清楚，防止你程序开发好了，上不了线，那你小程序的开发费用就白花了
---------------------------------------------

aioveu我爱你-小程序，助力商家实现销量大爆发
无需开发
3分钟一键开店
线上线下一体化爆单(需有货源，有营业执照)

aioveu我爱你-小程序电商解决方案
为管理提效+业绩增长赋能
10年+系统研发经验
支持按需定制开发
满足企业电商转型需求

搭建品牌流量池
精益运营驱动ROI高增长
全链路营销SOP
丰富运营题材库
打造品牌自有用户池

小程序海量营销功能
满足企业全场景营销诉求
拼团秒杀增购
直播带货变现
会员储值长效留存

全行业10000 + 模板一键装修，用户在哪，店就开在哪
20000 + 行业类目
精美模板一键套用
定制化装修服务

获客拉新到转化成交
客户秒变忠诚粉丝
全域获客转化
精准会员管理复购提频
社交分享+直播随时卖货成交


全行业软件定制开发
物流仓储
供应链管理
生产制造
医疗健康

APP定制开发
商场购物
教育培训
信息发布
生活服务

物联网应对定制开发
智能家电
智能玩具
工业互联
智能健康

Ai大模型定制开发
医疗大模型应用
政务大模型应用
教育大模型应用
企业管理大模型应用

服务优势
实力保驾护航
技术能力强悍
周期时短质优
价格透明，性价比高
案例多而全面
服务周到贴心