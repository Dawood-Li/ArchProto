### 使用dockerfile构建
docker build -t [镜像命名] [dockerfile路径]

### [必填] 父镜像
> FROM [镜像]

### 指定容器的工作目录
> WORKDIR [目录]

### 指定容器的环境变量
> ENV [key1]=[value1] [key2]=[value2]

### 拷贝文件到容器
> COPY [本机路径] [容器路径]  
> 可以是目录 文件 通配符

### 拷贝文件到容器 但自动解压tar.gz tar.bz tar.xz
> ADD [本机路径] [容器路径]

### 设置shell 用于RUN CMD ENTRYPOINT
> SHELL [shell路径]

### 构建镜像时执行命令
> RUN [命令]
> 每个RUN都会创建一层镜像所以命令尽量用&&连接

### 实例化时执行命令 会被create/run指令指定的命令覆盖  
> CMD [命令]  
### 实例化时执行命令 不会被create/run指令指定的命令覆盖  
> ENTRYPOINT [命令]  

### 暴露端口 随机映射
> ESPOSE [端口1] [端口2]

### 元数据
> LABEL [key1]=[value1] [key2]=[value2]
> 用于描述容器 作者名字 联系方式等信息

### 挂载数据卷 ? 
> VOLUME ["<路径1>", "<路径2>"...]

### 指定用户和组
> USER [用户名]:[用户组]

### 指定构建时变量
> ARG [key]=[value]
> 仅对dockerfile有效

