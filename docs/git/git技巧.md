删除远程分支
git push [远程仓库] --delete [分支]
git push origin --delete test

### 启用重命名检测 并设置相似度阈值
``` sh
git config --global diff.renames true
git config --global merge.renameLimit 51
```
