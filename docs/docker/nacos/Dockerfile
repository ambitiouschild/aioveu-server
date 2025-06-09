# 使用带有 JDK 11 的基础镜像
FROM openjdk:11-jre-slim

# 设置环境变量
ENV MODE=standalone \
    PREFER_HOST_MODE=hostname \
    NACOS_VERSION=2.2.3 \
    NACOS_SERVER=~ \
    NACOS_LOG_PATH=/home/nacos/logs

# 下载并安装 Nacos
RUN apt-get update -y \
    && apt-get install -y wget \
    && wget https://github.com/alibaba/nacos/releases/download/${NACOS_VERSION}/nacos-server-${NACOS_VERSION}.tar.gz \
    && tar -xzvf nacos-server-${NACOS_VERSION}.tar.gz \
    && mv nacos /home/nacos \
    && rm -rf nacos-server-${NACOS_VERSION}.tar.gz \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 修改时区（可选）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置工作目录
WORKDIR /home/nacos

# 暴露端口
EXPOSE 8848 9848 9555

# 启动命令
ENTRYPOINT ["bash", "bin/startup.sh", "-m", "standalone"]