# 基于某些安全,licence和可能带来的不好影响，删除了原来的Repo   -- 2018.09.25

![image.png](https://upload-images.jianshu.io/upload_images/2376786-f20e3d508f535fde.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


https://developer.android.google.cn/jetpack/docs/guide#common-principles

# 关于本Demo-组件化的工程架构With MVP，Dagger2.android,RXjava2

  根据实际项目中出现的问题，也一直在思考何种开发模式之中，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性这四个方面。
  不搞插件化之类的黑科技（难度和实际工程效果），首要目的是解决开发效率问题使用成熟而且稳定的组合：
  Rxjava2 + retrofit2 + MVP + Dagger2 也参考Google的官方框架https://github.com/googlesamples/android-architecture 
  本Demo目前已经完善了组件化架构，从Support升级到AndroidX ，也支持Kotlin 进行开发。


# Error,empty,Loading,timeout等通用的场景处理，一处Root注入，处处可用
   项目中的toolbar几乎每个页面都要使用，每个Layout 都写？
   进行网络请求时候的Error，empty,Loading,timeout等通用场景也是必须要处理的，Demo中一处Root注入，处处可用(Power by Loadsir）

# 关于Http网络请求 (Rxjava2+Retrofit2)
  [New]使用Retrofit 全局屏蔽重复请求

  应该没有比Retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多，结合Rxjava2不是更快哉;配合RxLifeCycle 控制生命周期;
  BaseObserver 中getErrorMsg(HttpException httpException) 方法中的处理和我们的Api  结构有关，请知悉。可以在Activity，fragment，service，broadcast 等发起http请求。


# UI架构模型-MVP
  Android应用的UI架构模型经历了MVC,MVP 和 MVVM 的演变过程。MVC中View 层（Activity，Fragment/自定义的View）
  可能代码会随着业务的复杂变得很大，里面不但要处理界面，还要处理很多业务逻辑里面承载了太多的东西，试试MVP吧，
  已经是很流行的UI架构模型了。
  使用MVP多关注代码结构、整体架构、可测试性、可维护性这四个方面

# 关于Dagger
  以前在使用dagger2的时候感觉理解绕，而且也违背依赖注入的核心原则：一个类不应该知道如何实现依赖注入；它要求注射类型知道  
  其注射器; 即使这是通过接口而不是具体类型完成的。dagger.android 出来后还大大的减少了模版代码😄, 不用在需要Inject 的地方写xx.build().inject(this);
  
  如果没有[Dagger.android](https://google.github.io/dagger//android.html) 我是不想使用dagger2的。写下面的类似代码实在太多了
  
  我自己翻译的关于Dagger Android 的文章：https://www.jianshu.com/p/879e0fe4ef36 。刚开始使用会比较难上手
  
  ```
  public class FrombulationActivity extends Activity {
    @Inject Frombulator frombulator;
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // DO THIS FIRST. Otherwise frombulator might be null!
      ((SomeApplicationBaseType) getContext().getApplicationContext())
          .getApplicationComponent()
          .newActivityComponentBuilder()
          .activity(this)
          .build()
          .inject(this);
      // ... now you can write the exciting code
      
    }
  }
 
 ```

# 项目中包含的基本的通用模块
- Dagger.android 大大的优化Dagger 在android 中的使用，
- BaseActivity 中Toolbar 的处理
- 进行网络请求时候的Error，empty,Loading,timeout等通用场景处理，Demo中一处Root注入，处处可用
- Http (Rxjava2+Retrofit2)的闭环处理
- AndroidX 和 Kotlin 支持
- 聚合型API处理（从不同的系统获取数据，返回的API 结构不同，详细见thirdParty 包下的处理）



















