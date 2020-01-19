package com.anylife.module_main.workmanger;


import android.content.Context;
import android.util.TimeUtils;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zlb.Sp.SPDao;

import javax.inject.Inject;

/**
 * WorkManger&LiveData
 *
 */
public class UploadWorker extends Worker {
    int a=0;

    @Inject
    SPDao spDao;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        a++;


//        spDao.saveData("time,","a="+a);

        spDao.get("time,",String.class);

        //pool-1-thread-1
        Thread.currentThread().getName();

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
}