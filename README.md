# fapi

快接 API 开放平台

### 项目介绍

基于 Spring Boot + Dubbo + Gateway + React 的 API 接口开放调用平台。管理员可以接入并发布接口，可视化各接口调用情况；用户可以开通接口调用权限、浏览接口及在线调试，并通过客户端 SDK 轻松调用接口。

### 项目架构

<img width="1479" height="846" alt="image" src="https://github.com/user-attachments/assets/46aea0ec-298b-4d11-98d9-99639ce951e5" />

### 技术选型

#### 前端

- React 18.2.0
- Ant Design Pro 5.13.2 脚手架
- UmiJs 4.3.34 框架
- Ant Design 和 procomponents 组件库
- OpenAPI
- ECharts 图表库

#### 后端

- Spring Boot 框架
- MySQL 数据库
- MyBatis-Plus
- API 签名认证
- Spring Boot Start（SDK开发）
- Dubbo 分布式框架（RPC, Nacos）
- Spring Cloud GateWay 微服务网关
- swagger + knife4j 接口文档生成
- Hutool、Apache Common Utils、Gson 等工具库

### 项目亮点

- 根据业务流程，将整个项目后端划分为 web 系统、模拟接口、公共模块、客户端 SDK、API 网关这 5 个子项目，并使用 Maven 进行多模块依赖管理和打包；
- 使用 Ant Design Pro 脚手架 + 自建 Spring Boot 项目模板快速构建初始 web 项目，并实现了前后端统一权限管理、多环境切换等基础能力；
- 基于 MyBatis Plus 框架的 QueryWrapper 实现对 MySQL 数据库的灵活查询，并配合 MyBatis X 插件自动生成后端 CRUD 基础代码，减少重复工作；
- 为防止接口被恶意调用，设计 **API 签名认证**算法，为用户分配唯一 ak / sk 以鉴权，保障调用的安全性、可溯源性；
- 为解决开发者调用成本过高的问题（须自己使用 HTTP + 封装签名去调用接口，平均 20 行左右代码），基于 Spring Boot Starter 开发了客户端 SDK，**一行代码**即可调用接口，提高开发体验；
- 为解决多个子系统内代码大量重复的问题，抽象模型层和业务层代码为公共模块，并使用 Dubbo RPC 框架实现子系统间的高性能接口调用，大幅减少重复代码。
