package com.albb.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


/**
 * @author lishuwen
 * @
 */
public class MyService extends Service {


    private static String TAG = "MyService";

    private String getDataStr = "";

    private Boolean isStart = false;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

        if (intent != null) {

            getDataStr = intent.getStringExtra("name");
        }

        Log.d(TAG, "showToast: this is onBind");
        return new Mybinder(intent.getExtras());
    }

    @Override
    public void onCreate() {
        super.onCreate();






        Log.d(TAG, "showToast: this is onCreate");
    }


    void initThread(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStart){

                    for (int i= 0;i<100000000;i++){

                        try {
                            if ("20".equalsIgnoreCase(String.valueOf(i))){
                                sendBroadcast(new Intent("stop"));
                            }
                            Thread.sleep(200);


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();

    }

    public class Mybinder extends Binder {

        private Bundle bundle;

        MyService getService() {
            return MyService.this;
        }

        public Mybinder(Bundle bundle) {
            this.bundle = bundle;
        }


        public void showToast() {
            String data = null;
            if (bundle != null) {
                if (bundle.containsKey("name")) {
                    data = bundle.getString("name");
                }
            }
            Log.d(TAG, "showToast: this is showToast" + data);
        }

        public void showLog() {

            Log.d(TAG, "showToast: this is showLog");
        }

    }


    @Override
    public boolean onUnbind(Intent intent) {

        Log.d(TAG, "This is onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "This is onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "This is onDestroy");
    }
}
