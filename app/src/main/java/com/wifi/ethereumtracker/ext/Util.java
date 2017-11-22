package com.wifi.ethereumtracker.ext;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.wifi.ethereumtracker.services.jobService.BackgroundCheckService;

import timber.log.Timber;

public class Util {

    private static JobScheduler jobScheduler;

    /**
     * Creating scheduled job
     * @param context Context used to instantiate jobScheduler
     */
    public static void schedule(Context context, int interval){
        jobScheduler = (JobScheduler) context
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);

        ComponentName service =
                new ComponentName(context.getPackageName(),
                        BackgroundCheckService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(1, service)
                .setPeriodic(interval) // change this
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();


        if (jobScheduler != null && jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_FAILURE){
            Timber.d("Something fucked up!");
        }
    }

    /**
     * Stopping scheduled job
     * @param jobId JobId we want to stop
     */
    public static void stop(int jobId){
        if (jobScheduler != null)
            jobScheduler.cancel(jobId);
    }

    public static int calculate10Percent(int value) {
        return value * 10 / 100;
    }
}
