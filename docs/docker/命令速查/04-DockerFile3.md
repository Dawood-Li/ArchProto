FROM [父镜像]

WORKDIR [指定容器中的目录]

COPY [src] [dst]
本地目录拷贝到镜像目录
可以是目录 文件 通配符

ADD
和COPY类似 但如果src为tar.gz tar.bz tar.xz 会自动解压

RUN [命令]
每个RUN都会创建一层镜像所以命令尽量用&&连接
在docker build时运行
构建镜像时运行

CMD
CMD在docker run时运行
镜像构建成容器时运行

容器启动时运行？




ENTRYPOINT



### 使用dockerfile构建
docker build -t nginx:v3 .

