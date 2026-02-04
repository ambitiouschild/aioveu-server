# 基础镜像 指定具体 Alpine 版本
#由于验证码是画出来的，openjdk里没有相关类 ，可以用 oracle jdk 镜像
#OpenJDK基础代码都是来自Oracle的JDK，但是为了开原协议的要求，把Oracle JDK中用到的一些非开源的组件、
#代码去除了，替换成了开源的组件，主要的是在加密和图形的部分。可能会有一些不兼容
#Docker镜像运行Spring Boot项目一般采用的是openjdk，这个jdk环境缺省是没有字体的，在运行很多项目时，会因缺省字体报错


#---------------------------------------------------------
#确定基础镜像类型：
#查看 Dockerfile 开头的 FROM 指令





# 这是 Debian/Ubuntu 基础   使用方案一
FROM aioveu-17-jdk-slim


# 或者使用 Distroless 镜像
#FROM gcr.io/distroless/java17-debian11


# 这是 Alpine 基础   使用方案三
#FROM openjdk:17-jdk-alpine


# 维护者信息 替换弃用的 MAINTAINER
#MAINTAINER aioveu <ambitiouschild@qq.com>
LABEL maintainer="ambitiouschild@qq.com"


#Dockerfile 中 RUN 指令使用 \ 续行符时，后续行不能为空行**（或仅含空格）。您的命令中可能在 && 和下一行之间存在多余空行。
# 合并连续的 RUN 指令（减少镜像层 + 避免续行错误）
#v3.7 是较旧的 Alpine 版本，建议更新到稳定版（如 v3.18），新源地址
#如果中科大源不稳定，可替换为
#中科大镜像源问题：USTC 源可能未同步 v3.18 版本，或者该版本已不维护
#仓库配置不全：很多包（特别是字体包）需要在 community 仓库中查找
#版本兼容性：使用太新或太旧的 Alpine 版本可能导致包不可用

# 阿里云源
#RUN echo "https://mirrors.aliyun.com/alpine/v3.18/main/" > /etc/apk/repositories
# 清华源
#RUN echo "https://mirrors.tuna.tsinghua.edu.cn/alpine/v3.18/main/" > /etc/apk/repositories

# 设置镜像源（包含main和community仓库）  # 使用稳定版本
#建议使用阿里云源 + Alpine 3.16 的组合，这个版本有长期支持且包可用性高。修改后重新构建应该能解决问题。
#RUN echo "https://mirrors.aliyun.com/alpine/v3.18/main/" > /etc/apk/repositories \
# && echo "https://mirrors.aliyun.com/alpine/v3.18/community/" >> /etc/apk/repositories
#----------------------------------------------------------
# 不修改源直接运行（官方源通常最可靠）
#方案三：如果您确实需要使用 Alpine 镜像
# 安装依赖（合并到一个RUN指令）
#RUN apk update \
# && apk --no-cache add tzdata ttf-dejavu fontconfig \
# && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
# && echo "Asia/Shanghai" > /etc/timezone \
# && rm -rf /var/cache/apk/*  # 清理缓存
#----------------------------------------------------------
#关键是根据您 Dockerfile 中 FROM 指令指定的基础镜像类型，选择合适的包管理器命令
#----------------------------------------------------------
#方案一：如果您使用的是 Debian/Ubuntu 基础镜像
# 替换为 apt-get 命令
RUN apt-get update \
    && apt-get install -y --no-install-recommends tzdata fonts-dejavu fontconfig \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

#----------------------------------------------------------
#方案二：如果您使用的是 CentOS/RHEL 基础镜像

#FROM centos:7
# 替换为 yum 命令
#RUN yum install -y tzdata dejavu-sans-fonts fontconfig \
#    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone \
#    && yum clean all

#----------------------------------------------------------
#方案三：如果您确实需要使用 Alpine 镜像  使用的基础镜像可能是基于Alpine Linux（使用apk）或Debian（使用apt），而不是基于RHEL/CentOS（使用yum）
# 使用 Alpine 基础镜像
#FROM openjdk:17-alpine

# 然后使用原来的 apk 命令
#RUN apk update \
#    && apk --no-cache add tzdata ttf-dejavu fontconfig \
#    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone \
#    && rm -rf /var/cache/apk/*



#----------------------------------------------------------
#1.字体包名称差异
#发行版	字体包名称
#Debian/Ubuntu	         fonts-dejavu
#Alpine	                 ttf-dejavu
#CentOS/RHEL	         dejavu-sans-fonts
#2.时区设置差异：
#Alpine：使用 cp 复制时区文件
#Debian/Ubuntu：使用 ln -sf 创建符号链接
#CentOS：两种方式都支持
#3.缓存清理位置
#行版	缓存位置
#Alpine	             /var/cache/apk/*
#Debian/Ubuntu	     /var/lib/apt/lists/*
#CentOS/RHEL	         /var/cache/yum/*
#----------------------------------------------------------

# 注意：
# 1. 行尾用 `\` 续行，下一行开头用 `&&` 连接（无空行！）
# 2. 合并了 ttf-dejavu 和 fontconfig 安装（原第二条 RUN 冗余）

#RUN apk add --update ttf-dejavu fontconfig
# 安装字体库 (必须)
#RUN apk add --no-cache fontconfig ttf-dejavu


#这个错误发生在生成验证码时，由于缺少字体管理类 sun.font.SunFontManager。 \
#这通常是因为在容器环境中缺少必要的字体库。以下是如何彻底解决这个问题的完整方案：
#关键在于确保容器内安装了必要的字体包，并在代码中正确引用它们。

# /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层
# 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

# 复制主机文件至镜像内，复制的目录需放置在 Dockerfile 文件同级目录下
ADD target/*.jar app.jar

# 容器启动执行命令
#增大 Docker 内存分配
#这是一个 JDK 在容器环境中的监控问题，不影响业务逻辑
CMD java \
    -Xms128m \
    -Xmx1024m \
    -Djava.security.egd=file:/dev/./urandom \
    -Djdk.internal.platform.disable=true  \
    -jar /app.jar




# 声明容器提供服务端口
EXPOSE 9000



