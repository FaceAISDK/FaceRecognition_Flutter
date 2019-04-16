package com.zenglb.framework.module_note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zenglb.framework.module_note.test.DynamicProxy;
import com.zenglb.framework.module_note.test.Sell;
import com.zenglb.framework.module_note.test.Vendor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 感觉做一些不想分享的笔记也会很有意思的啊
 * <p>
 * 假如没能盈利可以最后做成离线版本吧
 * <p>
 * 数据库要加密，内容也要加密，换手机支持离线道出数据 ！
 * <p>
 * Material Design 吧
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


//        //创建中介类实例
//        DynamicProxy inter = new DynamicProxy(new Vendor());
//
//        //加上这句将会产生一个$Proxy0.class文件，这个文件即为动态生成的代理类文件
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//
//        //获取代理类实例sell
//        Sell sell = (Sell) (Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class[]{Sell.class}, inter));
//
//        //通过代理类对象调用代理类方法，实际上会转到invoke方法调用
//        sell.sell();
//        sell.ad();


        /**
         * 动态代理，拦截处理器
         * 所有的请求都可以添加自定义的行为
         *
         *
         *
         */
        // 1. 首先实现一个InvocationHandler，方法调用会被转发到该类的invoke()方法。
        Sell sell = (Sell) Proxy.newProxyInstance(
                Sell.class.getClassLoader(),  //2.类加载器
                new Class[]{Sell.class},      //3.代理需要实现的类，要有多个
                new InvocationHandler() {     //4.方法调用的实际处理者
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        Log.e("HHHH", "Before");

                        return method.invoke(new Vendor(), args);
                    }
                });


        //得到了
        sell.sell();
        sell.ad();


//        上述代码的关键是Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler handler)方法，该方法会根据指定的参数动态创建代理对象。三个参数的意义如下：
//
//        loader，指定代理对象的类加载器；
//        interfaces，代理对象需要实现的接口，可以同时指定多个接口；
//        handler，方法调用的实际处理者，代理对象的方法调用都会转发到这里（*注意1）。
//        newProxyInstance()会返回一个实现了指定接口的代理对象，对该对象的所有方法调用都会转发给InvocationHandler.invoke()方法。理解上述代码需要对Java反射机制有一定了解。动态代理神奇的地方就是：
//
//        代理对象是在程序运行时产生的，而不是编译期；
//        对代理对象的所有接口方法调用都会转发到InvocationHandler.invoke()方法，在invoke()方法里我们可以加入任何逻辑，比如修改方法参数，加入日志功能、安全检查功能等；之后我们通过某种方式执行真正的方法体，示例中通过反        射调用了Hello对象的相应方法，还可以通过RPC调用远程方法。



//        //太慢了。跑了一晚上都没有跑出结果来
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                hahah();
//            }
//        }).start();


    }


    /**
     * 两个质数相乘=707829217。求之 ？
     */
    private void hahah() {
        for (int x = 3; x < 707829217; x++) {
            int flag1 = 0;
            for (int i = 2; i < x / 2; i++) {
                if (x % i == 0) {
                    flag1 = 1;
                    break;
                }
            }
            if (flag1 == 0) {
                for (int y = 3; y < 707829217; y++) {
                    int flag2 = 0;
                    for (int j = 2; j < y / 2; j++) {
                        if (y % j == 0) {
                            flag2 = 1;
                            break;
                        }
                    }
                    if (flag2 == 0) {
                        Log.d("AAAAA", "X=" + x + "    Y=" + y);

                        if (x * y == 707829217) {
                            Log.e("AAAAARFRT", "X=" + x + "    Y=" + y);
                            return;
                        }
                    }
                }
            }
        }
    }


}



