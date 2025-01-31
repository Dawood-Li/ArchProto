rsync -avz --progress (--delete) (--exclude-from=[exclude.txt]) (-e "ssh -p [port]") [src] [dst]
rsync 远程差分同步
-a --archive 归档模式，表示以递归方式传输文件并保持所有属性，等同于 -rlptgoD
-v --verbose 详细模式，显示传输过程中的详细信息
-z --compress 压缩模式，在传输过程中压缩数据
-e "ssh -p [port]" 指定使用 ssh 协议进行远程传输，并指定端口号
--progress 显示传输进度
--delete 删除目标目录中源目录不存在的文件
--exclude-from=[exclude.txt] 指定排除文件列表，文件中每行一个排除项
[src] 源目录
[dst] 目标目录

rsync -avn --delete --exclude-from=[exclude.txt] [src] [dst]
-n --dry-run 模拟运行，不实际进行文件传输，仅显示传输过程






rsync -avz --exclude-from-rsync-execlude.txt -e "ssh -p 10022" dawoodli@dawoodli.top:~/note.dawoo
dli.top note2

rsync -avn --exclude-from="rsync-exclude.txt" -e "ssh -p 10022" note.dawoodli.top dawood@dawoodli.top:~

rsync -avz --exclude-from="rsync-exclude.txt" -e "ssh -p 10022" dawood@dawoodli.top:~/note.dawoodli.top note2
