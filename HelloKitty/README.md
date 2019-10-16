1
Java 基础部分

基本这些公司都会问到相关基础，所以，一般来说基础的准备是必要并且通用的，所以这部分真的很重要。

1. HashMap 和 HashTable 以及 CurrentHashMap 的区别。

一般来说，这三个东西基本在面试中 70% 会被问到，而问的方向也不太一样。比如初级的问法是讲讲它们之前的区别，这个我想没什么难度，大多数人还是知道主要核心区别是并发上的处理。此外，内部数据结构的实现、扩容、存取操作这些问题应该是很老生常谈了，这并没有什么好说的，大多数人也都知道。

 稍微问的深一点的可能会在下面这些点上出问题。哈希碰撞，哈希计算，哈希映射，为什么是头插法，扩容为什么是 2 的幂次等这样的问题。
 
 

2. synchronized 和 volatile 、ReentrantLock 、CAS 的区别。

这个问题被问频率不在 HashMap 之下，因为并发编程，真的很重要。能问到这几个点的方式真的是太多了，我们能发挥的空间也同样很大。CAS 的 ABA 问题？上面几个东西的特性？使用场景？大概我不用再例举了吧？对了，我多次被问到的一个问题是：synchronized 修饰实例方法和修饰静态方法有啥不一样。

3. JVM 类加载机制、垃圾回收算法对比、Java 虚拟机结构等。

这三个问题大概出现概率 40%，基本只需要看我每日一问系列的推文就差不多了吧，希望更清楚明白的可以直接看《深入理解 Java 虚拟机》。当你讲到分代回收算法的时候，不免会被追问到新生对象是怎么从年轻代到老年代的，以及可以作为 root 结点的对象有哪些两个问题。

4. Java 的四大引用

四大引用面试出现概率比我想象中要高，我原本以为就强引用、软引用、弱引用、虚引用这四个玩意儿没啥可讲的。实际上也确实没啥好讲的，稍微问的深一些的面试官会和内存泄漏检测原理以及垃圾回收糅杂在一起。

5. Java 的泛型，<? super T> 和 <? extends T> 的区别。

Java 泛型还是会在面试中出现的，不过几率不是很高，大概是因为我简历中有提到泛型擦除相关的东西。所以会被问到泛型、泛型擦除、通配符相关的东西。不过这个东西，不应该是为了应付面试，实际开发中真的很重要。

6. Java 线程有哪些状态，有哪些锁，各种锁的区别。

这个问题讲真，我也只懂一点皮毛，并且当时回答不是很全面，出现概率的话，不是很高吧。

7. final 、finally、finalize 区别。

老生常谈的问题，没啥好说的，实际上这次社招面试也只遇到了两次。比较喜欢追根溯源的面试官可能会对这个 finalize 有点执念，一定希望搞清楚，这玩意儿我们是不是可以真的搞点黑科技骚操作。

8. 接口和抽象类的区别。
没想到还被问了一次这个，这玩意儿给我的感觉就是随时都在用，但真要较真，还真不能一口气把所有区别都信手拈来。

9. sleep 、wait、yield 的区别，wait 的线程如何唤醒它？

大多数 Android 应用开发并接触不到很多并发相关的东西，不过这玩意儿还是在面试中挺容易出现的。

2
计算机网络部分

计算机网络部分还是挺容易考察的，不过考察的点不会那么深入。通常来说也就是这些问题：

1. TCP 有哪些状态。

2. 三次握手、四次挥手。为啥是三次不是两次？

3. HTTPS 和 HTTP 的区别。HTTP 2.0，3.0？

4. 浏览器输入一个 URL，按下回车网络传输的流程？

5. 喜欢深问一点的还会问到网络架构，每层有些什么协议，FTP 这些相关原理，印象比较深刻的还有一个问题是：TCP 建立连接后，发包频率是怎样的？


3
Android 部分

Android 很广，所以这里只是简单说下有些什么问题。

这个的话其实真的 70% 问题出自你的简历。

关于简历可以参考：
给大家 3 个走心的面试建议

1. Activity 的生命周期；

2. Android 的 4 大启动模式，注意 onNewIntent() 的调用；

3. 组件化架构思路，如何从一个老项目一步一步实现组件化，主要问实现思路，考察应试者的架构能力和思考能力。

这一块内容真的很多，你需要考虑的问题很多，哪一步做什么，顺序很重要。

4. MVC、MCP、MVVP 的区别和各种使用场景，如何选择适合自己的开发架构？

5. Router 原理，如何实现组件间通信，组件化平级调用数据方式。

6. 系统打包流程；

7. APP 启动流程；

8. 如何做启动优化？
冷启动什么的肯定是基础，后续应该还有的是懒加载，丢线程池同步处理，需要注意这里可能会有的坑是，丢线程池如何知道全部完成。

9. 事件分发机制。

事件分发已经不是直接让你讲了，会给你具体的场景，比如 A 嵌套 B ，B 嵌套 C，从 C 中心按下，一下滑出到 A，事件分发的过程，这里面肯定会有 ACTION_CANCEL 的相关调用时机。

10. 如何检测卡顿，卡顿原理是什么，怎么判断是页面响应卡顿还是逻辑处理造成的卡顿？

11. 生产者模式和消费者模式的区别？

12. 单例模式双重加锁，为什么要这样做。

13. Handler 机制原理，IdleHandler 什么时候调用。

14. LeakCanary 原理，为什么检测内存泄漏需要两次？

15. BlockCanary 原理。

16. ViewGroup 绘制顺序；

17. Android 有哪些存储数据的方式。

18. SharedPrefrence 源码和问题点；

19. 讲讲 Android 的四大组件；

20. 属性动画、补间动画、帧动画的区别和使用场景；

21. 自定义 ViewGroup 如何实现 FlowLayout？如何实现 FlowLayout 调换顺序？

22. 自定义 View 如何实现打桌球效果；

23. 自定义 View 如何实现拉弓效果，贝瑟尔曲线原理实现？

24. APK 瘦身是怎么做的，只用 armabi-v7a 没有什么问题么？

APK 瘦身这个基本是 100% 被面试问到，可能是我简历上提到的原因。

25. ListView 和 RecyclerView 区别？RecyclerView 有几层缓存，如何让两个 RecyclerView 共用一个缓存？

26. 如何判断一个 APP 在前台还是后台？

27. 如何做应用保活？全家桶原理？

28. 讲讲你所做过的性能优化。

29. Retrofit 在 OkHttp 上做了哪些封装？动态代理和静态代理的区别，是怎么实现的。

30. 讲讲轨迹视频的音视频合成原理；

31. AIDL 相关；

32. Binder 机制，讲讲 Linux 上的 IPC 通信，Binder 有什么优势，Android 上有哪些多进程通信机制?

33. RxJava 的线程切换原理。

34. OkHttp 和 Volloy 区别；

35. Glide 缓存原理，如何设计一个大图加载框架。

36. LRUCache 原理；

37. 讲讲咕咚项目开发中遇到的最大的一个难题和挑战；

这个问题基本是 95% 必问的一个问题；

38. 说说你开发最大的优势点。

出现率同上。

4
算法

1. String 转 int。

核心算法就三行代码，不过临界条件很多，除了判空，还需要注意负数、Integer 的最大最小值边界等；

2. 如何判断一个单链表有环？

3. 链表翻转；

4. 快排；

5. 100 亿个单词，找出出现频率最高的单词。要求几种方案；

6. 链表每 k 位逆序；

7. 镜像二叉树；

8. 找出一个无序数组中出现超过一半次数的数字；

9. 计算二叉树的最大深度，要求非递归算法。

10. String 方式计算加法。

5
HR 面

1. 你为什么离开咕咚？
2. 你的缺点是什么？
3. 你能给公司带来什么效益？
4. 你对未来的职业规划？


6
最后

此次面试刷了一遍剑指 Offer，个人感觉平时写博客真的很重要，简历上的东西，自己写的所以自己都知道，而其他的基础和细节，自己的博客就已经覆盖。总的来说，不是打广告，每日一问和之前的面试系列真的命中面试题概率很大。

如果你觉得还算有用的话，不妨把它们推荐给你的朋友。



# 2018 年9月25 ，基于某些安全,licence和可能带来的不好影响，删除了原来的Repo,这是修改后的

---
![image.png](https://upload-images.jianshu.io/upload_images/2376786-f20e3d508f535fde.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
---
但还是熟悉配方和味道，欢迎交流 👏

# 教训：自由的世界也是有规则的

# 关于本Demo-组件化的工程架构With MVP，Dagger2.android,RXjava2

  一直在纠结在何种开发模式之中，重构希望能把关注点集中到代码结构、整体架构、可测试性、可维护性这四个方面
  Rxjava2 + retrofit2 + MVP + Drager2 +,应该是当前Android开发主流的框架 ，我们都会参考Google的官方框架https://github.com/googlesamples/android-architecture 或者 Google 的最新的项目架构组件https://github.com/googlesamples/android-architecture-components

本 Demo目前已经完善了组件化架构，解决大型项目需要按需编译，模块化，代码隔离的问题
  
 # Android 组件化架构
   简单的分为BaseLib(抽象出来的其他Module共用)，module_main,module_news;其中module_main 中的News_fragment组件化到了module_news。 Android 组件化架构带来的好处和副作用网络上相关文章很多，在实际的项目中实施过程如果有使用Dagger&Dager.android ,可能会遇到一些麻烦，本Demo演示了如何处理。集成模式还是组建模式可以clone完代码后自己手动通过添加修改gradle.properties 文件中 的isModule 值（这个文件不会上传git版本管理），然后Sync Project 生效 （gradle.properties 文件用来配置Gradle settings的，例如JVM参数等，我们在gradle.properties 中配置的字段都可以在build.gradle文件中直接读取出来，不用任何多余的代码）
   
   加上Dagger2.android 真的炒鸡解耦，某个模块外包出去也可以，还大大减少大项目的编译调试时间，加快效率
   
   更多介绍：https://www.jianshu.com/p/5028eff76c30
   

# Error,empty,Loading,timeout等通用的场景处理，一处Root注入，处处可用
   项目中的toolbar几乎每个页面都要使用，每个Layout 都写？
   进行网络请求时候的Error，empty,Loading,timeout等通用场景也是必须要处理的，Demo中一处Root注入，处处可用(Power by Loadsir）

# 关于Http网络请求 (Rxjava2+Retrofit2)
  [New]使用Retrofit 全局屏蔽重复请求

  应该没有比Retrofit2 更好的了吧？不过api 不是restful 就需要再封装一下了，网路模块就是数据命脉，做好了
  整个app 的结构会简化很多，结合Rxjava2不是更快哉;配合RxLifeCycle 控制生命周期;
  BaseObserver 中getErrorMsg(HttpException httpException) 方法中的处理和我们的Api  结构有关，请知悉。可以在Activity，fragment，service，broadcast 等发起http请求。


# UI架构模型-MVP (结合rxjava&dagger2)
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
- 通用的BaseActivity 和BaseFragment的封装（跳转PV打点，事件打点，不放和base 无关的东西）
- Http (Rxjava2+Retrofit2)的闭环处理
- AndroidX 和 Kotlin 支持
- [混淆压缩打包优化 Proguard　proguard-android-optimize　和 proguard-android 区别 ？](https://github.com/D-clock/Doc/blob/master/Android/Gradle/4_AndroidStudio%E4%B8%8BProGuard%E6%B7%B7%E6%B7%86%E6%89%93%E5%8C%85.md)




















