package com.digua.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.digua.ipctest.service.BookManagerService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private IBookManager mRemoteBookManager;

    private final int HANDLER_MESSAGE_CODE = 10001;

    /**
     * 执行在服务端的Binder线程池，hanler切换到主线程
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MESSAGE_CODE: {
                    Book book = (Book) msg.obj;
                    if (book != null) {
                        Log.e(TAG, "handleMessage:" + book.getName());
                    }
                    break;
                }
            }
        }
    };

    /**
     * 链接服务
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            com.digua.ipctest.handipc.IBookManager1 iBookManager = BookManagerImpl1.asInterface(service);// 测试手写aidl实现java文件
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = iBookManager;
            try {
                List<Book> bookList = iBookManager.getBookList();
                for (Book book : bookList) {
                    Log.e(TAG, "客户端获取的book:" + book.getName() + "-" + book.getPrice());
                }
                iBookManager.addBook(new Book("摄影用光1", 110.5f));
                iBookManager.registerListener(iOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 监听回调接口
     */
    IOnNewBookArrivedListener iOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            //IOnNewBookArrivedListener 运行在Binder线程池中，需要切换到主线程
            Log.e(TAG, "onNewBookArrived:" + book.getName());
            Message message = handler.obtainMessage();
            message.obj = book;
            message.what = HANDLER_MESSAGE_CODE;
            handler.sendMessage(message);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn_bindService);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 直接绑定服务
                Intent intent = new Intent(MainActivity.this, BookManagerService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.e(TAG, "setOnClickListener-onCreate");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (mRemoteBookManager != null) {
            try {
                mRemoteBookManager.unRegisterListener(iOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
