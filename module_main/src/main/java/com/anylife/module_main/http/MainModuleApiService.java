package com.anylife.module_main.http;

import com.anylife.module_main.business.login.LoginResult;
import com.anylife.module_main.business.navigation.fragment.MeProfileResult;
import com.zlb.http.param.LoginParams;
import com.zlb.httplib.HttpResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * API 列表，https://cloud.youku.com/docs?id=43
 *
 *
 * ApiService 对象最终会被在retrofit.create(ApiService.class)后被动态代理
 * 比如 HttpCall.getApiService().getWeather(url,"深圳")
 *
 *
 * Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getWeather方法的注解信息，
 * 配合args参数，创建一个ServiceMethod对象，ServiceMethod就像是一个中央处理器，传入Retrofit对象和
 * Method对象，调用各个接口和解析器，最终生成一个Request，包含api 的域名、path、http请求方法、请求头、
 * 是有body、是否是multipart等等。最后返回一个Call对象，Retrofit2中Call接口的默认实现是OkHttpCall，
 * 它默认使用OkHttp3作为底层http请求client。
 *
 *
 * Created by zenglb on 2017/3/17.
 */
public interface MainModuleApiService {

    /**
     * Oauth,外网暂不支持访问
     */
    @Headers("NeedOauthFlag: NeedOauthFlag")
    @POST("{OauthPath}")
    Observable<HttpResponse<LoginResult>> goLogin(@Path ("OauthPath") String url,@Body LoginParams loginParams);



    /**
     * MeProfile, 外网暂不支持访问
     *
     */
    @GET("{MeProfilePath}")
    Observable<HttpResponse<MeProfileResult>> getMeProfile(@Path ("MeProfilePath") String url);


}

