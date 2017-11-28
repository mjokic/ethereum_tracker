package com.wifi.ethereumtracker.ext;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ext.services.backgroundCheckJobService.BackgroundCheckService;

import timber.log.Timber;

public class Util {

    private static JobScheduler jobScheduler;

    /**
     * Creating scheduled job
     *
     * @param context Context used to instantiate jobScheduler
     */
    public static void schedule(Context context, int interval) {
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


        if (jobScheduler != null && jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_FAILURE) {
            Timber.d("Something fucked up!");
        }
    }

    /**
     * Stopping scheduled job
     *
     * @param jobId JobId we want to stop
     */
    public static void stop(int jobId) {
        if (jobScheduler != null)
            jobScheduler.cancel(jobId);
    }

    public static double calculate10Percent(double value) {
        return value * 10 / 100;
    }


    public static void displayErrorDialog(Context context) {
        new AlertDialog.Builder(context, R.style.ErrorAlertDialogStyle)
                .setCancelable(false)
                .setTitle(R.string.error_dialog_title)
                .setMessage(R.string.error_dialog_msg)
                .create()
                .show();
    }

}
