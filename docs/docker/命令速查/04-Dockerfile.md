# Dockerfile

### Dockerfile简介
Dockerfile为用于构建镜像的文本文件。通过在Dockerfile中定义一系列指令，可以自动化地创建镜像。

### 使用Dockerfile构建镜像
```sh
$ docker build -t [镜像名]:[标签] [dockerfile路径]
```
- `-t`：指定镜像名和标签。
- 如果文件名是dockerfile则只需指定路径 否则需要指定文件名

| 关键字       | 语法                                 | 说明                                                         |
|--------------|--------------------------------------|--------------------------------------------------------------|
| FROM         | FROM [镜像名]:[标签]                 | 指定基础镜像                                                 |
| RUN          | RUN [命令]                           | 在镜像中执行命令                                             |
| COPY         | COPY [源路径] [目标路径]             | 将文件从主机复制到镜像中                                     |
| ADD          | ADD [源路径] [目标路径]             | 与COPY类似，但支持自动解压tar文件                           |
| CMD          | CMD ["命令", "参数1", "参数2"]       | 指定容器启动时默认执行的命令                                 |
| ENTRYPOINT   | ENTRYPOINT ["命令", "参数1", "参数2"] | 与CMD类似，但不会被`docker run`命令行参数覆盖               |
| ENV          | ENV [变量名] [值]                   | 设置环境变量                                                 |
| EXPOSE       | EXPOSE [端口]                       | 声明容器监听的端口                                           |
| VOLUME       | VOLUME [路径]                       | 创建数据卷                                                   |
| WORKDIR      | WORKDIR [路径]                       | 设置工作目录                                                 |

### 注意事项
··· txt
From是必须的 其余都不是  
可以from scratch 表示从空镜像开始
但一般只需要from alpine即可
越小的镜像启动速度越快

CMD定义启动命令和启动参数 docker run / docker exec会覆盖这个启动参数  
ENTRYPOINT 定义的命令不会被覆盖 而是将CMD或来自命令的启动命令 追加到自己的参数来运行 可以用于定义启动初始化脚本  

EXPOSE只是声明
VOLUME也只是声明
···
# 新增内容
SHELL可以设置默认shell 用于RUN CMD ENTRYPOINT  
RUN命令应该尽量合并以减少镜像层数

EXPOSE指令可以声明端口映射，但实际映射是随机的

LABEL可以添加元数据，如作者联系方式等

VOLUME指令可以声明挂载数据卷，但需要指定具体路径

USER可以指定运行命令的用户和用户组

ARG可以在构建时设置变量，仅对Dockerfile有效