package com.albb.testservice;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {

    private String data = null;
    private Boolean isStart = false;

    public TestService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initThread ();
    }

    /**
     * 开启线程，模拟耗时操作
     */
    void initThread (){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStart){
                    try {

                        //每隔一秒就返回一个不同的数据
                        for (int i = 0;i<1000000000;i++){
                            Thread.sleep(1000);
                            if (callBack!=null){
                                callBack.showData("我是第"+i+"个"+data);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    public IBinder onBind(Intent intent) {

        data = intent.getStringExtra("name");

        return new MyBinder();
    }

    /**
     * service 解除绑定时要把线程关闭
     * @param conn
     */
    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        isStart = true;

    }

    /**
     * 接收多次调用startSrvice时传递过来的参数
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        data = intent.getStringExtra("name");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * custom your Binder
     */
    public class MyBinder extends Binder {

        public TestService getTestService() {
            return TestService.this;
        }

        void setData(String data) {
            TestService.this.data = data;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isStart = true;
        stopSelf();
    }




    CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public interface CallBack {

        void showData(String s);

        void showName(Bundle bundle);


    }
}
