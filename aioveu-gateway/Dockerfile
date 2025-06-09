# 使用官方的Java运行时环境作为基础镜像
FROM openjdk:17-jdk-alpine
# 维护者信息
MAINTAINER aioveu ambitiouschild@qq.com

## 安装curl
#RUN apt-get update && apt-get install -y curl

# 设置国内镜像源(中国科技大学镜像源)，修改容器时区(alpine镜像需安装tzdata来设置时区)
RUN echo "https://mirrors.ustc.edu.cn/alpine/v3.7/main/" > /etc/apk/repositories && \
    apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层，从而保证容器存储层的无状态化
# 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

# 将jar文件添加到容器中并放到合适的位置
# 复制jar至镜像
ADD target/aioveu-gateway.jar app.jar


# 暴露端口
EXPOSE 9999

## 健康检查
#HEALTHCHECK --interval=30s --timeout=10s \
#  CMD curl -f http://localhost:9999/actuator/health || exit 1

# 指定容器启动时执行的命令
CMD java \
    -Xms128m \
    -Xmx128m \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /app.jar

# 启动命令（添加JVM安全和日志优化）
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]



#docker run -d --name aioveu-gateway -p 9999:9999 --network aioveu-server aioveu-gateway:latest

#我们的Java项目需要部署到Docker容器中，以便于更好地管理和部署应用。在没有Docker的情况下，我们每次部署都需要手动配置服务器环境，这样既繁琐又容易出错。
#而使用Docker，我们可以将应用和其所需的依赖项打包成一个镜像，然后在任何支持Docker的环境中运行。

#1. 安装Docker
 #首先，我们需要在我们的开发环境中安装Docker。
 #Docker支持多个操作系统，包括Windows、Mac和Linux。你可以根据你的操作系统从Docker官方网站上下载并安装Docker。
#2. 编写Dockerfile
#Dockerfile是一个文本文件，其中包含了一系列用于构建Docker镜像的指令。
#我们可以通过编写Dockerfile来定义我们的Java项目所需的环境和依赖项。