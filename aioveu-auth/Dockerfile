FROM openjdk:17-jdk-alpine
MAINTAINER aioveu ambitiouschild@qq.com

# 设置国内镜像源(中国科技大学镜像源)，修改容器时区(alpine镜像需安装tzdata来设置时区)，安装字体库(验证码)
RUN echo "https://mirrors.ustc.edu.cn/alpine/v3.7/main/" > /etc/apk/repositories && \
    apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk --no-cache add ttf-dejavu fontconfig

# 安装字体库 (必须)
RUN apk add --update ttf-dejavu fontconfig
#由于验证码是画出来的，openjdk里没有相关类 ，可以用 oracle jdk 镜像
#OpenJDK基础代码都是来自Oracle的JDK，但是为了开原协议的要求，把Oracle JDK中用到的一些非开源的组件、
#代码去除了，替换成了开源的组件，主要的是在加密和图形的部分。可能会有一些不兼容
#Docker镜像运行Spring Boot项目一般采用的是openjdk，这个jdk环境缺省是没有字体的，在运行很多项目时，会因缺省字体报错



# /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层，从而保证容器存储层的无状态化
# 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

# 复制jar到镜像
ADD target/aioveu-auth.jar app.jar

# 指定容器启动时执行的命令
CMD java \
    --add-opens java.base/java.io=ALL-UNNAMED \
    --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    --add-opens java.base/java.util=ALL-UNNAMED \
    -Xms128m \
    -Xmx128m \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /app.jar

EXPOSE 9000



