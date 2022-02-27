# amazing-e-commerce

这是一个基于 Spring MVC + Vue 的simple前后端分离 web 项目，电子商务，做到实现商品的展示、购买等功能。
后期会继续更新，使得项目功能更丰富。

> 此项目是本人学习 Spring + Vue 开发的练习项目，基本照搬了 [liuzhengwei127/Ebook](https://github.com/liuzhengwei127/Ebook)，这里感谢
> ***@liuzhengwei127***。

## 用到的技术栈

### 前台

- axios: Axios 是一个基于 promise 的 HTTP 库，方便的用来实现ajax.
- Bootstrap: 简洁、直观、强悍的前端开发框架，让web开发更迅速、简单
- VUE: 近几年最受人瞩目的前端框架，渐进式的Java Script开发框架

### 后台

- Spring：Java世界流行的框架，基于Ioc、AOP思想，使得开发快速、高效。
- Spring MVC：基于Spring 的 MVC框架
- Hibernate：全球流行的Java ORM框架，国外使用率第一。
- Jackson: 流行且久经考验的Java JSON数据处理框架
- Lombok: Java包，通过注解的方式，使开发者高效的开发Java对象

### 如何运行

clone源码到本地

1. 使用maven打包成war文件，放入tomcat的webapps目录运行；

2. 或者使用Jetty的maven插件，运行命令mvn jetty:run来运行。

注意 *请更改初始化你的MySQL数据库*
