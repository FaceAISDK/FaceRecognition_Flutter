## 基于可能出现的安全,licence和不好的影响，删除原来的Repo。
[新Repo代码GitHub链接点这里](https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2)

![请2年前clone过老项目的同学也删除](https://upload-images.jianshu.io/upload_images/2376786-f20e3d508f535fde.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1024)

## 使用Dagger ,Rxjava ,Retrofit ,LiveData,ViewModel等搭建App框架

根据项目中出现的工程问题以及平衡业务需求，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性以及快速迭代这四个方面。本次迭代除了使用Dagger,Rxjava,Retrofit,Greendao 等外，新引入了Google官方JetPack组件中的LiveData,ViewModel，LifeCycle，WorkManger等。并把工程框架变为模块化，独立的业务模块可以单独的进行调试，管理。

 ### 项目的框架图如下（结束组件化，模块化结构）
  
![项目的框架图](https://upload-images.jianshu.io/upload_images/2376786-012ea22c70e01dc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/555)

项目框架图如上图所示，这里约定一个称谓：Main,News可以单独依赖Base调试，App壳工程也可以把他们打包集合到一起调试，我们称为App模块化架构。如下图切换后Sync一下工程
![切换模块化开发模式.png](https://upload-images.jianshu.io/upload_images/2376786-9545b108bb8ada6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/567)

这样的好处是1.Base可以在同一公司的多个项目共用 2.负责Main，New业务的开发可以只关注自身的git 权限管理，编译调试（大大减少带薪Build 时间）3.工程框架清晰，更容易维护

### ViewModel+LiveData 的封装处理
可以很好的处理生命周期的问题和config change,异步请求数据回调时ui组件生命周期处于不活跃状态也不会出现空指针的问题,该组件可以及时的收到数据变化的回调通知（不用CallBack）。但是onChange 官方只是处理成功数据的回调，我们需要封装[StateLiveData](https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2)处理网络错误,服务器自定义的一些返回码等情况

### Dagger 和ViewModel 结合遇到的问题

项目使用的是Dagger2和Dagger.android，在Demo工程中看起来很繁琐，代码量也很多,但是配置好了一劳永逸) 。随着项目业务变多的时候dagger相关的代码不会变多，但是能让业务代码化繁为简并解耦。

但是Dagger 和ViewModel结合的时候会有点麻烦结合官方和StackOverFlow 的处理后结果如Demo 

### 关于多个系统提供数据（多BaseUrl）,返回的json 数据格式还不一样的情况处理
很多App都是多个BaseUrl,数据由不同的系统提示，甚至内部Python和Java 后台返回的数据json 格式还不一样，那就要抽象Base出来了，demo 的Blog和News 演示了这一情况

### Retrofit 全局屏蔽重复请求

应该没有比Retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
整个app 的结构会简化很多，结合Rxjava2更能简化很多。假如点击一个按钮请求数据除了从源头防止还能不能从发起请求的时候检查。

### 数据的持久化
   SP 
   ORMDB （Room,GreenDao,xx）


## 项目中包含的基本的通用模块
- Dagger.android 大大的优化Dagger 在android 中的使用，
- BaseActivity 中Toolbar 的处理
- 进行网络请求时候的Error，empty,Loading,timeout等通用场景处理，Demo中一处Root注入，处处可用
- Http (Rxjava2+Retrofit2)的闭环处理
- AndroidX 和 Kotlin 支持
- 聚合型API处理（从不同的系统获取数据，返回的API 结构不同，详细见thirdParty 包下的处理）
- 封装StateLiveData 以便处理失败和业务异常
- dagger 和ViewModel 的结合处理

https://developer.android.google.cn/topic/libraries/architecture/images/final-architecture.png

## 参考资料：

[1]. Google 应用架构指南
[2]. LiveData ViewModel Overview
[3]. Dagger - A fast dependency injector for Android and Java
[4]. 






