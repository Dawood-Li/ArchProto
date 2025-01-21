# SpringBoot docker 多阶段构建

Date: 2024/7/12  
Author: Dawood Li  

## 说明
``` txt
用docker隔离构建环境和运行环境
不是微服务 只是环境管理

避免污染生产环境
避免多个服务之间版本冲突

数据库端口之类还需要手写配置文件
后续提供docker-compose
以及CI/CD
```

### 准备maven源的配置文件
``` xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>/usr/share/maven/ref/repository</localRepository>
  
  <!-- 只有这里是在原有基础上新增的 -->
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>central</mirrorOf>
      <name>阿里云公共仓库</name>
      <url>https://maven.aliyun.com/repository/central</url>
    </mirror>
    
    <!-- 此处可以添加更多源 -->
    <!-- ... -->
  </mirrors>
</settings>
```

### 准备dockerfile
``` dockerfile
# 构建环境
FROM m.daocloud.io/docker.io/maven:3.9.6-eclipse-temurin-21 AS build-env

# 设置工作目录
WORKDIR /app

# 自定义配置文件 设置数据源为阿里云
COPY settings-docker.xml /usr/share/maven/ref/

# maven 构建依赖 使用指定的配置文件
COPY pom.xml /app/
# RUN mvn dependency:go-offline
RUN mvn -s /usr/share/maven/ref/settings-docker.xml dependency:go-offline

# maven 生成jar包  使用指定的配置文件
COPY src /app/src/
# RUN mvn package -DskipTests
RUN mvn -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

# 运行环境
FROM m.daocloud.io/docker.io/library/alpine:latest

# 更换apk源为阿里云的Alpine镜像源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 下载OpenJDK 21 
RUN apk add --no-cache openjdk21-jre

# 复制jar包
COPY --from=build-env /app/target/*.jar /app.jar

# 启动指令
CMD ["java", "-jar", "/app.jar"]
```

### 提前拉取镜像 防止卡构建
``` sh
$ sudo docker pull m.daocloud.io/docker.io/maven:3.9.6-eclipse-temurin-21
$ sudo docker pull m.daocloud.io/docker.io/library/alpine:latest
```

### 构建
``` sh
# 进入项目目录进行构建 和pom.xml一个层级
$ sudo docker build -t springboot-app -v ~/.m2:/root/.m2 .

# 如果想 不使用缓存重新构建 执行这条
$ sudo docker build --no-cache -t springboot-app -v ~/.m2:/root/.m2 .
```

### 运行
``` sh
# 其余参数先自己填 本方案仅构建思路 后续方案提供完整参数
$ sudo docker run -itd --name springboot-app \
    -p 8081:8080 \
    -restart always \
    app
```


指定环境变量 以切换配置文件
environment:
  - SPRING_PROFILES_ACTIVE=prod

application.properties：所有环境的通用配置。
application-dev.properties：开发环境的特定配置。
application-test.properties：测试环境的特定配置。
application-prod.properties：生产环境的特定配置。
