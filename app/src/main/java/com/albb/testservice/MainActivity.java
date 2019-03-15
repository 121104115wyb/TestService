package com.albb.testservice;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    TestService.MyBinder mybinder;
    boolean bindBoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定服务
        initBinderService();

    }

    void initBinderService() {
        Intent intent = new Intent(MainActivity.this, TestService.class);
        intent.putExtra("name", "我是谁");
        bindBoo = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    //开启绑定服务通道
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mybinder == null) {
                mybinder = (TestService.MyBinder) iBinder;
                //实现service的接口，进而取到返回值
                mybinder.getTestService().setCallBack(new TestService.CallBack() {
                    @Override
                    public void showData(String s) {
                        //接收返回值
                        if (!TextUtils.isEmpty(s)) {
                            Log.d("MainActivity", "showData: " + s);
                        }else {
                            Log.d("MainActivity", "showData: 返回值为空");
                        }
                    }
                    @Override
                    public void showName(Bundle bundle) {

                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mybinder = null;
        }
    };

   //通过startService方式，会重新调用onstartConmmand方法进行传入数据的更新与改变
    public void btnOclick(View view) {
        if (!bindBoo) {
            initBinderService();
        }
        Intent intent = new Intent();
        intent.setClass(this,TestService.class);
        intent.putExtra("name","ddddd");
        startService(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mybinder = null;

        //停止service
        stopService(new Intent(this,TestService.class));

    }


}
