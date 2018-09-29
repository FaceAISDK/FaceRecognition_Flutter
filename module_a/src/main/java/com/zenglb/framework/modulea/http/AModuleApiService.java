package com.zenglb.framework.modulea.http;

import com.zenglb.framework.modulea.http.result.AnyLifeResult;
import com.zenglb.framework.modulea.http.result.CustomWeatherResult;
import com.zenglb.framework.modulea.http.result.JokesResult;
import com.zenglb.framework.modulea.http.result.LoginResult;
import com.zlb.http.param.LoginParams;
import com.zlb.http.result.StaffMsg;
import com.zlb.httplib.HttpResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * ApiService 对象最终会被在retrofit.create(ApiService.class)后被动态代理
 * 比如 HttpCall.getApiService().getWeather(url,"深圳")
 * <p>
 * Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getWeather方法的注解信息，
 * 配合args参数，创建一个ServiceMethod对象，ServiceMethod就像是一个中央处理器，传入Retrofit对象和
 * Method对象，调用各个接口和解析器，最终生成一个Request，包含api 的域名、path、http请求方法、请求头、
 * 是有body、是否是multipart等等。最后返回一个Call对象，Retrofit2中Call接口的默认实现是OkHttpCall，
 * 它默认使用OkHttp3作为底层http请求client。
 * <p>
 * <p>
 * Created by zenglb on 2017/3/17.
 */
public interface AModuleApiService {


    /**
     * 第三方动态 url 访问
     * 测试在同一个系统下访问外部URL
     */
    @GET
    Call<CustomWeatherResult> getWeather(@Url String url, @Query("city") String city);


    @GET("http://zhihu.0x01.site/articles/test1")
    Observable<HttpResponse<List<AnyLifeResult>>> getHandyLifeData(@Query("type") String type, @Query("page") int page);


    /**
     * 这里只是举例子说明，不要在意
     */
    @Headers("NeedOauthFlag: NeedOauthFlag")
    @POST("api/XXXXXXXXX/oauth/access_token/XXXXXXXX")
    Observable<HttpResponse<LoginResult>> goLoginByRxjavaObserver(@Body LoginParams loginRequest);



    @GET()
    Call<String> getUserProfile(@Url String url);


}

