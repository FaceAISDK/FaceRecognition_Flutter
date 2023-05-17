# Android 网络请求封装总结

Android Http 网络请求封装，使用 Kotlin+Moshi+Coroutines+retrofit等封装处理Http请求，支持多个域名多态数据返回
封装要达到的目的特性就一个：简洁的同步代码风格处理网络异步请求！


为什么要使用以下技术栈

- 协程 Coroutines 而不是rxjava：rxjava 是个好东西但不是官方的，学习成本也高，后期官方推荐kotlin,引入协程简洁优雅处理网络请求
- Moshi 而不是GSON： Moshi 天生支持Kotlin空安全，使用CodeGen生成代码比Gson反射注解效率高
- Retrofit 而不是直接OKHTTP ：因为Retrofit使用注解+动态代理来优雅的封装调度OKHTTP去执行
- 哪怕是使用自家的OkHttp，哪怕底层调用的始终是OkHttpClient，也需要依赖一个抽象的retrofit2.Call接口，依赖于抽象，而不是依赖于具体
- 同步的方式写异步请求。简洁，简洁，目标只有一个：使用快捷方便简洁 ！

## 封装能达到的效果/目标
 - 1. 多域名Host，多个环境，多态返回数据处理      
 - 2. 使用最新稳定的技术方案实现快捷稳定的请求交互
 - 3. 封装各种Http异常处理，服务器返回的各种异常json 数据
 - 4. 


## 1.关于 [Retrofit](https://www.jianshu.com/p/f57b7cdb1c99)

Retrofit的设计非常插件化而且轻量级，接口设计真的是非常高内聚而且低耦合，**精妙的设计是极佳的研究素材**。Retrofit底层请求 默认由OKHTTP执行，
Retrofit主要负责调度；其中 Retrofit中定义了4个接口：

- Callback<T> 接口
 这个接口就是retrofit请求数据返回的接口，只有两个方法
  * void onResponse(Response<T> response); 
  * void onFailure(Throwable t);

 - Converter<F, T>
这个接口主要的作用就是将HTTP返回的数据解析成Java对象，主要有Xml、Gson、protobuf等等，你可以在创建Retrofit对象时添加你需要使用的Converter实现（看上面创建Retrofit对象的代码）

 - Call<T.>
这个接口主要的作用就是发送一个HTTP请求，Retrofit默认的实现是OkHttpCall<T>，你可以根据实际情况实现你自己的Call类，这个设计和Volley的HttpStack接口设计的思想非常相似，子类可以实现基于HttpClient或HttpUrl Connection的HTTP请求工具，这种设计非常的插件化，而且灵活

 - CallAdapter<R,T> 网络请求执行器（Call）的适配器

CallAdapter用于对原始Call进行再次封装，如Call<R>到Observable<R>或者本SDK 中的HttpResult<T>



## 2. [协程Coroutines](https://rengwuxian.com/kotlin-coroutines-1/)
「协程 Coroutines」源自 Simula 和 Modula-2 语言，这个术语早在 1958 年就被 Melvin Edward Conway 发明并用于构建汇编程序，说明协程是一种编程思想，并不局限于特定的语言; 协程设计的初衷是为了解决并发问题，让 「协作式多任务」 实现起来更加方便。

 在Android中使用协程的环境分为下面五种环境：

 *  网络请求
 *  回调处理
 *  数据库操作
 *  文件操作
 *  其他耗时操作


## 3.数据返回封装


  ![img.png](img.png)

  假设公司各个业务服务器返回的Http Json数据格式都差不多三个大字段code[int] + msg[str] +data[T] ,名称允许自定义不同
  这里我们统一约定称含有三个类似字段为包装数据Http wrapper data定义为HttpWrapper.kt,每个Http请求正常都会含有这三个字段，
  data字段很自然的我们会使用范型T来表示，这也是Http 请求回来实际参与业务处理的部分 .
  - [code] 本字段表示业务服务是否获取了期待的数据，一般0或200 表示成功，其他值表示没有获取正常期望的业务数据如权限不足
    另外，这个业务code 要和标准Http 请求响应的编码区分清除，标准Http 2xx 表示成功，4xx表示客户端错误，5xx 是服务器错误
    强烈建议业务code 的返回值不要和标准Http 请求响应的编码有重合避开[2xx,5xx]范围
  - [msg] msg 是对业务code的补充说明，可以简单的是OK 或者对应的异常提示信息
 
  
  如果你的App 请求的业务服务器返回json不包含类似这三字段结构也没关系：那就不是标准的HttpWrapper了,不需要实现HttpWrapper impl
  相应的判断需要使用的时候进行补齐

  在 HttpResponseCall<> 类中我们根据http 请求是否成功，4xx,5xx 分别对应处理返回HttpResult<out T : Any>中的
  - Success<T>   成功获取期待的数据data范型T
  - Failure(msg,code)  包含了业务错误和Http 请求异常（timeout，networkerror）


## 4.Moshi 解析空安全处理，默认值情况
   这个情况非常多，大部分的闪退和异常源于数据的异常，比如某个必有字段没有返回，导致异常
   还有就是有些字段需要设置后端没有返回的时候的默认值

## 5.LiveData 还是Flow ？
 使用方java？ Kotlin ?    
 从 LiveData 迁移到 Kotlin 数据流：https://juejin.cn/post/6979008878029570055

## 6.接入使用





## 调试使用的API

* https://api.europeana.eu/record/v2/search.json?wskey=gogesqui&query=Paris&reusability=open&media=true
* https://fakerapi.it/en  : https://fakerapi.it/api/v1/companies?_quantity=15&_locale=zh_CN
* https://www.wanandroid.com      https://www.wanandroid.com/banner/json


## 参考文档
- Retrofit 2.0源码分析 https://www.jianshu.com/p/0c055ad46b6c
- 从架构角度看Retrofit  https://www.jianshu.com/p/f57b7cdb1c99