package com.zenglb.framework.mvp.handylife;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zenglb.framework.RxJavaTestSchedulerRule;
import com.zenglb.framework.Rxjava2UnitTestUtils;
import com.zenglb.framework.http.ApiService;
import com.zlb.http.result.AnyLifeResult;
import com.zlb.httplib.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import io.reactivex.Observable;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * Created by zlb on 2018/3/24.
 */
public class AnyLifeRepositoryTest {
    private static List<AnyLifeResult> mHandyLifeResultList;  // mHandyLifeResultList for static json String

    private AnyLifeRepository mHandyLifeRepository;

    @Rule
    public RxJavaTestSchedulerRule rule = new RxJavaTestSchedulerRule();

    @Mock
    private ApiService mApiService;  //........

    @Mock
    private IAnyLifeDataSource.LoadHandyLifeDataCallback loadHandyLifeDataCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<IAnyLifeDataSource.LoadHandyLifeDataCallback> loadHandyLifeDataCallbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        //yes you know
        Rxjava2UnitTestUtils.asyncToSync();

        // Get a reference to the class under test
        mHandyLifeRepository = new AnyLifeRepository(mApiService);


        // mHandyLifeResultList for static json String
        try {
            mHandyLifeResultList = new Gson().fromJson(StaticJSON.jsonStr,
                    new TypeToken<List<AnyLifeResult>>() {
                    }.getType());
        } catch (Exception e) {
            Log.e("JSON Exception", e.toString());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHandyLifeData() throws Exception {
        HttpResponse<List<AnyLifeResult>> httpResponse=new HttpResponse<>();
        httpResponse.setCode(0);
        httpResponse.setResult(mHandyLifeResultList);

        //Observable<HttpResponse<List<HandyLifeResultBean>>>
        when(mApiService.getHandyLifeData(anyString(),anyInt())).thenReturn(Observable.just(httpResponse));

        //invoke getHandyLifeData
        mHandyLifeRepository.getHandyLifeData("city", 1,loadHandyLifeDataCallback);  //type page

        verify(mApiService).getHandyLifeData(anyString(),anyInt());

//        //是否被调用
//        verify(mHandyLifeRepository, times(1)).getHandyLifeData(anyString(), anyInt()
//                ,loadHandyLifeDataCallbackArgumentCaptor.capture());


//        loadHandyLifeDataCallbackArgumentCaptor.getValue().onHandyLifeDataSuccess();
//
//        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
//        verify(mHandyLifeView).showHandyLifeData(listArgumentCaptor.capture());
//
//        assertTrue(listArgumentCaptor.getValue().size() == 20);
    }

}