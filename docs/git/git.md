# Git分支管理与代码提交指南

Date: 2024/6/25  
Author: Dawood Li  
Email: dawood_li@outlook.com  

### 分支介绍

- **master**：这是生产环境分支，由管理员控制。只包含稳定和经过测试的代码。将会清理分支提交记录，仅保留简洁的版本更新记录。
- **release**：这是测试环境分支，由管理员控制。用于上线前的集成测试。
- **dev**：这是开发分支，由管理员控制。用于合并分支，将保留所有开发者的提交记录。
- **个人分支**：这是你的个人分支，由你创建和修改，由管理员负责合并和删除。每个人每个任务都有自己的分支。

### 如何工作

1. **设置Git**：首先，设置你的Git用户名和邮箱。
``` sh
$ git config --global user.name "Dawood Li"
$ git config --global user.email "dawood_li@outlook.com"
```

2. **克隆仓库**：从远程仓库克隆代码到本地。
``` sh
$ git clone https://example.com/repository.git
```

3. **创建个人分支**：从master分支创建一个新的分支，分支名称应该反映你的任务目标，比如`feature_login_history`。
``` sh
$ git checkout master
$ git pull
$ git checkout -b feature_login_history
```

4. **编写代码**：在你的个人分支上编写代码。记得在代码中添加注释，包括你的名字、联系方式和修改日期。
``` java
package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
/**
 * Date: 2024-06-25
 * Author: Dawood Li
 * Email: dawood_li@outlook.com
 * 
 * 此类由我管理 用于接口格式测试 将在后续版本中移除
 * 测试了纯文本返回 和json返回
 * 后续将测试二进制返回 以及定义response模型 以实现静态检查
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    
    @GetMapping("/hello2")
    public Map<String, Object> hello() {
        return new HashMap<String, Object>() {{
            put("code", 0);
            put("msg", "Hello, World!");
            put("data", Arrays.asList());
        }};
    }
}
```

5. **提交代码**：完成一部分工作后，提交你的代码到个人分支。提交信息要详细，说明你对哪些文件做了什么修改，为什么这么修改，是否实现了哪些功能或修改了什么bug。
```bash
$ git add .
$ git commit -m "追加了一个新的接口返回格式 用于测试不同的接口格式在前端的表现"
```
6. **推送到远程**：将你的代码推送到远程仓库的个人分支。
```bash
$ git push origin feature_login_history
```
7. **等待审核**：提交后，等待管理员审核和拉取你的代码。

### 注意事项
- 只需关注个人分支和提交代码，其余交给管理员。
- 保持代码的注释清晰，以帮助其他成员快速熟悉项目和排查问题。
- 测试不通过或不符合规范的代码可能会被驳回，请注意邮件。


