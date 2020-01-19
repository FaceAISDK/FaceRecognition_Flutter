## 由于licence，可能出现的安全问题和不好的影响等，删除原来的Repo。

![请clone过老项目的同学也删除](https://upload-images.jianshu.io/upload_images/2376786-f20e3d508f535fde.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1024)

[新Repo代码GitHub链接点这里](https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2)

## 使用Dagger ,Rxjava ,LiveData,ViewModel等搭建App框架

根据项目中出现的工程问题以及平衡业务需求，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性以及快速迭代这四个方面。本次迭代除了使用Dagger,Rxjava,Retrofit,Greendao 等外，新引入了Google官方JetPack组件中的LiveData,ViewModel，LifeCycle，WorkManger等。并把工程框架变为模块化，独立的业务模块可以单独的进行调试，管理。

### 项目的框架图如下
  
![项目的框架图](https://upload-images.jianshu.io/upload_images/2376786-012ea22c70e01dc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/555)

项目框架图如上图所示，这里约定一个称谓：Main,News可以单独依赖Base调试，App壳工程也可以把他们打包集合到一起调试，我们称为App模块化架构。如下图切换后Sync一下工程
![切换模块化开发模式.png](https://upload-images.jianshu.io/upload_images/2376786-9545b108bb8ada6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/567)

这样的好处是
1.Base可以在同一公司的多个项目共用 
2.负责Main，New业务的开发可以只关注自身的git 权限管理，编译调试（大大减少带薪Build 时间）
3.工程框架清晰，更容易维护

### ViewModel+LiveData 的封装处理

ViewModel和LiveData 官方JetPack组件重要组成部分。
- ViewModel 当因系统配置发生改变导致 Activity 重建的时候（比如旋转屏幕，更改系统语言），能对 LiveData 进行正确的保存和恢复（当然这要配合 LiveData）。当然如果是系统资源紧张被清除是不能恢复的。
- LiveData 的作用是在使得数据能具有生命周期感知能力，在 Activity 等变为活跃状态的时候，自动回调观察者中的回调方法，也就是说对数据的变化进行实时监听。这样就不会出现异步请求数据成功后UI不活跃的时候出现空指针异常，也可以很好的避免内存泄露

但是onChange 观察回调官方只是处理成功数据的回调，我们需要封装[StateLiveData](https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2/blob/master/baselib/src/main/java/com/zlb/statelivedata/StateLiveData.java)处理网络错误,服务器自定义的一些返回码等情况
```
        //LiveData 具有生命周期感知能力，它会自动对这些进行管理
        //UI 组件只需要对相关的数据进行监听，不需要关心是否应该暂停或者恢复监听。 
        blogViewModel.getAllBlog().observe(this, stateData -> {
            switch (stateData.getStatus()) {
                case SUCCESS:
                    disposeSuccessData(stateData.getData());
                    break;
                case ERROR:
                    //全局统一的错误处理，http 提示和UI 空页面，错误页面提示等
                    BaseDispose.errorDispose(mBaseLoadService, getActivity(), stateData.getMsg(), stateData.getCode());
                    break;
            }
        });
```
![权侵删](https://upload-images.jianshu.io/upload_images/2376786-e0ad051dac85fa05.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

而使用Retrofit请求网络业务层的写法也是非常的精简

```
    public StateLiveData<HotNewsResult> getStateLiveData() {
        //1.异步加载网络请求数据
        newsApiService.getNews()
                .compose(SwitchSchedulers.applyScheduler2())//rxjava 切换线程
                .subscribe(new HttpObserver<NewsResult>() {
                    @Override
                    public void onSuccess(NewsResult newsResult) {
                        stateLiveData.postSuccess(newsResult);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        //把错误信息告知给UI操作层面
                        stateLiveData.postFailure(message + code);
                    }
                });
        return stateLiveData;
    }
```

### Dagger 和ViewModel 结合遇到的问题

项目使用的是Dagger2和Dagger.android，在Demo工程中看起来很繁琐，代码量也很多,但是配置好了一劳永逸) 。随着项目业务变多的时候dagger相关的代码不会变多，但是能让业务代码化繁为简并解耦。

但是Dagger 和ViewModel结合的时候会有点麻烦。定义好ViewModelKey 和 class MyViewModelFactory implements ViewModelProvider.Factory后，其实 和在一个Activity/Fragment中使用Dagger 差不多。

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
- 进行网络请求时候的Error,empty,Loading,timeout等通用场景处理，Demo中一处Root注入，处处可用
- Http (Rxjava2+Retrofit2)的闭环处理
- 聚合型API处理（从不同的系统获取数据，返回的API 结构不同，详细见thirdParty 包下的处理）
- 封装StateLiveData 以便处理失败和业务异常
- dagger 和ViewModel 的结合处理

希望大家会喜欢，并在[GitHub](https://github.com/AnyLifeZLB/MVP-Dagger2-Rxjava2)提出宝贵意见

## 参考资料：

- [1]. Google 应用架构指南
- [2]. LiveData ViewModel Overview
- [3]. Dagger - A fast dependency injector for Android and Java
- [4]. Android JetPack 使用入门
... ...






