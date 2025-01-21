# git服务搭建

### 准备工作
``` sh
# 安装git
sudo dnf update -y
sudo dnf install -y git-all

# 以下操作建议使用root用户执行

# 建立git用户
$ adduser git

# 设置git用户的默认shell为git-shell以封锁权限
$ chsh git -s $(which git-shell)
# 若添加失败 检查/etc/shells中是否存在$(which git-shell) 若不存在 手动添加

# 创建目录.ssh 并设置权限为700
$ mkdir /home/git/.ssh
$ chown git:git /home/git/.ssh
$ chmod 700 /home/git/.ssh
$ touch /home/git/.ssh/authorized_keys
$ chown git:git /home/git/.ssh/authorized_keys
$ chmod 600 /home/git/.ssh/authorized_keys

# 复制已知的公钥到git用户
$ cat /tmp/id_rsa.dawood.pub >> /home/git/.ssh/authorized_keys

```

在authorized_keys中添加仓库管理员的密钥 并在密钥前追加前缀
no-port-forwarding,no-X11-forwarding,no-agent-forwarding,no-pty
以避免被利用ssh打洞 x11远程桌面 上传下载文件等

用git身份建立目录/srv/git
git init --bare --shared
--shared意为将此目录权限设置为可写
