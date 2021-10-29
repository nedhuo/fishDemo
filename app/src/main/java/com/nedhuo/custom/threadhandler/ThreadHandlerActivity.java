package com.nedhuo.custom.threadhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.nedhuo.custom.R;

public class ThreadHandlerActivity extends AppCompatActivity {
    //创建子线程
    class MyThread extends Thread{
        private Looper looper;//取出该子线程的Looper
        public void run() {
            Looper.prepare();//创建该子线程的Looper
            looper = Looper.myLooper();//取出该子线程的Looper
            Looper.loop();//只要调用了该方法才能不断循环取出消息
        }
    }

    private Handler mHandler;//将mHandler指定轮询的Looper

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       setContentView(R.layout.main);

//        thread = new MyThread();
//        thread.start();//千万别忘记开启这个线程
//        //下面是主线程发送消息
//        mHandler = new Handler(thread.looper){
//            public void handleMessage(android.os.Message msg) {
//                Log.d("当前子线程是----->",Thread.currentThread()+"");
//            };
//        };
//        mHandler.sendEmptyMessage(1);
    }
}
