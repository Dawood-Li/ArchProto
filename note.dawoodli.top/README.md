# DL Mardown Docs

* 初版

    app.py单文件即是初版项目，开发于24年初。 
    使用flask + sqlite编写。  
    目标是实现一个极速部署极速迁移的轻量级笔记管理系统。  
    实现了基本的增删改查功能，使用软删除和元数据分离的设计思路，保留了创建和修改的日期和版本记录。  
    前端实现了一个基础的编辑页面，并对移动端做了简单的布局适配。  
    整个项目简洁清爽，并经过了两个月的轻度使用，解决了大部分bug，管理了个人的数百条笔记数据。  

* 重写

    使用docker + nginx + mysql + springboot + mybatis plus  
    于2025年1月20日重写  

* 计划

    bugfix：搜索使用了rest api 在keywords中包含#的情况下会搜索失败    
    基于邮箱的用户注册登录  
    markdown渲染
    用Vue进行重构

* 更新日志

    2025年1月26日
    初步实现了对prod test dev环境切换不同配置的支持
    如可以根据ENV选择不同的nginx.conf，dockerfile, application.properties
    优化了springboot app的构建流程。
    dev.dockerfile: 复用maven缓存，映射代码启动，修改更快。
    prod.dockerfile: 复用maven缓存，多阶段构建jar包，占用更低。
    用.env.prod文件存储生产环境敏感信息
    仅为初步支持，后续继续完善细化。
    计划dev.nginx.conf使用自签名 prod.nginx.conf使用letsencrypt证书
    devtools失效目前原因不明，推测和docker文件映射机制有关。
