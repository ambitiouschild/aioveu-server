FROM openjdk:17-jdk-alpine
MAINTAINER aioveu aioveutech@163.com

# 设置国内镜像源(中国科技大学镜像源)，修改容器时区(alpine镜像需安装tzdata来设置时区)，安装字体库(验证码)
RUN echo "https://mirrors.ustc.edu.cn/alpine/v3.7/main/" > /etc/apk/repositories && \
    apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk --no-cache add ttf-dejavu fontconfig


# /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层，从而保证容器存储层的无状态化
# 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

# 复制jar到镜像
ADD target/aioveu-auth.jar app.jar

# 指定容器启动时执行的命令
CMD java \
    -Xms128m \
    -Xmx128m \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /app.jar

EXPOSE 9000



