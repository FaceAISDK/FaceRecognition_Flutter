package com.zenglb.framework.mvp.handylife;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zlb.http.result.AnyLifeResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AnyLifePresenter}
 * HandyLifePresenter 的单元测试
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class AnyPresenterTest {
    private static List<AnyLifeResult> mHandyLifeResultList;  // mHandyLifeResultList for static json String

    @Mock
    private AnyLifeRepository mHandyLifeRepository;

    @Mock
    private AnyLifeContract.HandyLifeView mHandyLifeView; //The V of the MVP

    private AnyLifePresenter mHandyLifePresenter;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<IAnyLifeDataSource.LoadHandyLifeDataCallback> mLoadHandyLifeDataCallbackCaptor;

    /**
     * Call before every test function
     *
     */
    @Before
    public void setupPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mHandyLifePresenter = new AnyLifePresenter(mHandyLifeRepository);
        mHandyLifePresenter.takeView(mHandyLifeView);

        // The presenter won't update the view unless it's active.
        when(mHandyLifeView.isActive()).thenReturn(true);

        // mHandyLifeResultList for static json String
        try {
            mHandyLifeResultList = new Gson().fromJson(StaticJSON.jsonStr,
                    new TypeToken<List<AnyLifeResult>>() {
                    }.getType());
        } catch (Exception e) {
            Log.e("JSON Exception", e.toString());
        }
    }


    @Test
    public void getHandyLifeData() throws Exception {
        // Given an initialized HandyLifePresenter with initialized data
        mHandyLifePresenter.getHandyLifeData("city", 1);  //type page
        verify(mHandyLifeRepository, times(1)).getHandyLifeData(anyString(), anyInt(),mLoadHandyLifeDataCallbackCaptor.capture());

        mLoadHandyLifeDataCallbackCaptor.getValue().onHandyLifeDataSuccess(mHandyLifeResultList);

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mHandyLifeView).showHandyLifeData(listArgumentCaptor.capture());

        assertTrue(listArgumentCaptor.getValue().size() == 20);
    }




}