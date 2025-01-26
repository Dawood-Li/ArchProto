# 构建环境
FROM m.daocloud.io/docker.io/library/alpine:latest AS build-env

# 更换apk源为阿里云的Alpine镜像源并安装依赖
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache openjdk21-jdk maven

# 设置工作目录
WORKDIR /app

# 自定义配置文件 设置数据源为阿里云
COPY settings-docker.xml /usr/share/maven/ref/

# maven 构建依赖 使用指定的配置文件
COPY pom.xml /app/
RUN mvn -s /usr/share/maven/ref/settings-docker.xml dependency:go-offline

# maven 生成jar包 使用指定的配置文件
COPY src /app/src/
RUN mvn -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

# 运行环境
FROM m.daocloud.io/docker.io/library/alpine:latest

# 更换apk源为阿里云的Alpine镜像源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 下载OpenJDK JRE 21 
RUN apk add --no-cache openjdk21-jre

# 复制jar包
COPY --from=build-env /app/target/*.jar /app.jar

# 启动指令
CMD ["java", "-jar", "/app.jar"]
