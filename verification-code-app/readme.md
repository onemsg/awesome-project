# 验证码应用 Example

👉📰 [我用响应式编程实现了一个验证码验证服务 （By Vert.X） - 掘金](https://juejin.cn/post/7018956329628729381/)

## Run

1. `Git clone` 本项目到本地
2. IDE 直接启动 `com.onemsg.message.Main`
3. IDE 直接启动 `com.onemsg.sunday.Main`


## EndPoints

- POST `/api/sendMsg`

- POST `/api/verification-code/ask-for`

- POST `/api/verification-code/validate`


VSCode REST Client `test.rest` file:

```json
POST http://localhost:3001/api/sendMsg
Authorization: Basic faeFaeGEerfteEFmO458
Content-Type: application/json

{
    "Message": "【宜家俱乐部】焕新家装季，收纳有妙招！至10/25来四元桥宜家\n⭐帕克思衣柜每满4000送300元电子券\n⭐斯玛斯塔儿童衣柜每满1500送100元电子券\n 线上线下同享>  c8b.co/Vpz8xpA 回0退订",
    "PhoneNumber": "18620210001"
}

###
POST http://localhost:3000/api/verification-code/ask-for
Content-Type: application/json

{
    "user": "18620210001",
    "service": "SIGIN_IN"
}

### 
POST http://localhost:3000/api/verification-code/validate
Content-Type: application/json

{
    "user": "18620210001",
    "service": "SIGIN_IN",
    "code": 367203
}
```
