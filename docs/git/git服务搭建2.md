# git服务搭建

### 1. 安装 Git
```sh
sudo apt-get update
sudo apt-get install git
```

### 2. 创建 Git 用户并指定默认 shell 为 git-shell
```sh
sudo adduser --system --shell /usr/bin/git-shell --gecos 'Git Version Control' --group --disabled-password --home /srv/git git
sudo adduser --system --shell /usr/bin/git-shell --group --home-dir /srv/git git
```

### 3. 创建 /srv/git 目录
```sh
sudo mkdir -p /srv/git
sudo chown git:git /srv/git
```

### 4. 配置 SSH 访问
#### 创建 .ssh 目录并设置权限
```sh
sudo mkdir -p /srv/git/.ssh
sudo touch /srv/git/.ssh/authorized_keys
sudo chown -R git:git /srv/git/.ssh
sudo chmod 700 /srv/git/.ssh
sudo chmod 600 /srv/git/.ssh/authorized_keys
```

#### 将开发者的公钥添加到 authorized_keys
将开发者的公钥（public key）复制到 `/srv/git/.ssh/authorized_keys` 文件中。

### 5. 封锁其他 SSH 权限
编辑 `/etc/ssh/sshd_config` 文件：
```sh
sudo nano /etc/ssh/sshd_config
```
在文件中添加以下内容：
```sh
Match User git
    AllowTcpForwarding no
    X11Forwarding no
    PermitTunnel no
    AllowAgentForwarding no
```
保存并退出后，重启 SSH 服务：
```sh
sudo systemctl restart ssh
```

### 6. 使用 SSH 协议远程创建一个空的 Git 仓库
```sh
ssh git@your_server 'git init --bare /srv/git/your_project.git'
```

### 7. 创建新分支
#### 克隆远程仓库到本地
```sh
git clone git@your_server:/srv/git/your_project.git
```

#### 创建新分支
```sh
cd your_project
git checkout -b new_branch
```

#### 推送新分支到远程仓库
```sh
git push origin new_branch
```

### 总结
以上步骤完成后，你将能够通过 SSH 访问 Git 仓库，执行创建分支等操作。所有开发者的公钥应添加到 `/srv/git/.ssh/authorized_keys` 中，确保他们有访问权限。