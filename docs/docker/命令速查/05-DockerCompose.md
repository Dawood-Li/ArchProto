# docker-compose

### 安装环境
``` sh
# 以下几个命令就可以把整套项目跑起来
$ sudo dnf install docker docker-compose -y # 下载
$ sudo systemctl enable docker          # 启用docker服务
$ sudo systemctl start docker           # 启动docker服务
$ sudo ./image_pull.sh                  # 提前拉取基础镜像
$ sudo docker-compose build             # 构建镜像
$ sudo docker-compose up                # 启动镜像
# ok 全套启动完成
```

### 查看定义的服务
``` sh
$ sudo docker-compose config --services
```

### 构建镜像
``` sh
$ sudo docker-compose build
```

### 创建实例 (不启动)
``` sh
$ sudo docker-compose create
```

### 启动实例 前台
``` sh
$ sudo docker-compose up
```

### 启动实例 后台
``` sh
$ sudo docker-compose up -d
```

### 查看日志
``` sh
$ sudo docker-compose logs
```

### 停止实例
``` sh
$ sudo docker-compose stop
```

### 重启实例
``` sh
$ sudo docker-compose restart
```

### 销毁实例
``` sh
$ sudo docker-compose down
```

### 销毁实例和数据卷（不包括挂载的目录）
``` sh
$ sudo docker-compose down -v
```

### 构建设置, 数据卷
``` yml
version: '3.8'

volumes:
  # 定义数据卷
  # 数据卷不会在清理镜像时一并被清理
  # 数据卷存在时 不会再次执行初始化脚本 哪怕初始化失败
  # 所以初始化失败时 应当使用docker-compose down -v命令 一并移除数据卷
  # 除非down命令包含-v参数 将会移除数据卷 丢失所有数据
  example_data: {}

services:
  example:
    # 直接使用现有镜像 与build二选一
    # image: ubuntu:latest

    # 需要使用dockerfile构建镜像
    build:
      # 执行目录
      context: myapp
      # 使用的dockerfile名称
      dockerfile: dockerfile

    volumes:
      # 绑定数据卷
      # 好像可以将一个数据卷绑定给多个实例
      - example_data:/data
      # 通过数据卷绑定本机目录到镜像的/docker-entrypoint-initdb.d目录
      # /docker-entrypoint-initdb.d内的脚本将在数据库初始化阶段按序号执行
      # 如01-xxx.sh 将会优先于02-xxx.sh执行 除此之外不保证执行顺序
      - ./initscript:/docker-entrypoint-initdb.d  
      # 可以绑定目录 也可以绑定文件
      # 如./nginx/nginx.conf:/etc/nginx/nginx.conf
      # 若外部文件/目录不存在 则会以root身份创建
      # 若存在 则内部文件/目录将会被创建或删除后覆盖
```

### 端口映射, 虚拟子网，环境变量
``` yml
version: '3.8'

networks:
  # 子网中的实例将允许通过host名与其他实例通信
  # 不定义具名虚拟子网 将会自动创建与配置文件所在目录+后缀_net的子网
  # 子网内的镜像并不认为处在同一台机器上 而需要借助虚拟子网+host名访问互联 或通过映射在主机上的端口互联
  # 同名的具名docker子网将允许互联
  dl_net:
    driver: bridge

services:

  postgres:
    image: m.daocloud.io/docker.io/postgres:15-alpine
    # 暴露端口且映射 但如在同一个网络中 则不需要 
    ports:
      - "10000:5432"
    # 环境变量 此镜像必须定义如下参数才可启动
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=1234
      - POSTGRES_DB=postgres
      - LANG=C.UTF-8
    # 定义虚拟子网
    networks:
      - dl_net
    # 用于直接使用主机网络
    # network_mode: "host"
```

### 性能限制和保留
``` yml
version: '3.8'

services:
  example:
    image: ubuntu:latest
    deploy:
      resources:
        # 限制 即实例最高可占用多少资源
        limits:
          # 核数非整数 单位是核心数而不是总体利用率
          cpus: '4.0'
          memory: 4096M
        # 保留 即实例启动的最低所需
        reservations:
          cpus: '0.1'
          memory: 256M
    ulimits:
      memlock: -1
      # 内存使用限制 -1无限
      # 最大文件打开数 soft是警告值 hard是关闭值 默认1024
      nofile:
        soft: 65535
        hard: 65535
```
