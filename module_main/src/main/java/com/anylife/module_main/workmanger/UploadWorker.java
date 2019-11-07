package com.anylife.module_main.workmanger;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * WorkManger&LiveData
 *
 */
public class UploadWorker extends Worker {
    int a=0;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        a++;

        Thread.currentThread().getName();
//        uploadImages()

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
}