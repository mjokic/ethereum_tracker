package com.wifi.ethereumtracker.services.asyncTasks;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.wifi.ethereumtracker.activities.MainActivity;
import com.wifi.ethereumtracker.model.RetrofitTask;

public class SplashAsyncTask extends AsyncTask<Context, Void, Boolean>{

    private Context context;

    @Override
    protected Boolean doInBackground(Context... contexts) {
        // Get sources from info endpoint
        this.context = contexts[0];
        boolean stat = new RetrofitTask("doesn't", "matter")
                .getSources(context);
        System.out.println(stat + "<-- status");

        return stat;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if(result) {
            Intent intent = new Intent(context, MainActivity.class);
            this.context.startActivity(intent);
        }else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Error!");
            alertDialog.setMessage("There's an error while loading sources!\n" +
                    "Please check your internet connection or try again later!");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
            alertDialog.show();
        }
    }

}
