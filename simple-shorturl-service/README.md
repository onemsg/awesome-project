# Simple Shorturl Service | 简单短网址服务

一个简单的短网址服务系统，可通过 RESTful API 来生成新短网址，短网址与原网址的映射存储在 Redis 数据库中，用户请求短网址时会被重定向到原网址。

后台使用 [Vert.X-Web](https://vertx.io/docs/vertx-web/java/) 和异步编程，web服务和redis服务之间通过 EventBus 通信。 短网址生成使用原网址到62进制映射的方案。

短网址服务原理可参考 [短网址(short URL)系统的原理及其实现 | 思否](https://segmentfault.com/a/1190000012088345)

## RESTful API 说明

- **`POST /api/create {'srcUrl': 'source url'}`** | 返回提交原网址的 短网址

```json
{"shortUrl": "xxx.xx/5Fdx6l", "date": "2020-4-28 16:22", "state": "success|fail"}
```

- **`GET /api/list`** | 返回已创建的所有短网址

```json
[ {"shortUrl": "short url", "srcUrl": "source url", "date": "create date" } ]
```

- **`GET /:shortUrlKey`** | 通过短网址，重定向到原网址

## 类/包简要说明

- `shorturl.common.Convertor` 是一个工具类，主要用来把十进制整数转62进制，和把一个字符串转62进制。

- `shorturl.verticle` 包中都是各个 `verticle`。 `RedisVerticle` 用来创建和管理 Redis 数据库，以及通过事件机制（异步）相应 Redis 的读写请求；`RestVerticle` 用来创建 web 服务，提供 RESTful API 管理接口和短域名的路由重定向，它会异步请求读写 Redis。

- `short.Server` 部署和运行 Vert.X 服务。

## 其他说明

- 数据存储为什么不用 ConcurrentHashMap 或 MySQL?

因为我想应用重启后，之前创建的短域名能再次使用，因此必须使用外置的数据库，放弃 ConcurrentHashMap。因为 Web 层已经使用了 Vert.X，一个异步的、高并发性能的框架（工具套件），再使用低性能的关系型数据库肯定不匹配，因此使用了 NOSQL 数据库 Redis, 另外数据存储内容也很简单。

- 有什么需要改进的吗？

当然很多，第一件容易改进的就是让日志系统打印出更详细、具体的内容。另一个，有些挑战性的就是，当短域名创建的多时，如何处理62进制短网址的同名冲突，一个简单的思路是生成短域名后，去检测 Redis 里是否已存在相同键（短域名），如果存在，则把短域名做个小修改（比如+1），然后再去 Redis 里检测是否有相同键，直到不存在相同键为止。*（欢迎评论区大佬给建议）*

## 运行

首先确保 Redis 数据库已启动，JDK version 11或以上

- IDE 直接运行 `short.Server` 的 main 方法；

- 生成 jar 包，运行 `java -jar short-url-**.jar short.Server`。
