package com.digua.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.digua.ipctest.Book;
import com.digua.ipctest.Book1;
import com.digua.ipctest.IBookManager;
import com.digua.ipctest.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: BookManagerService
 * @Description: 运行在独立的进程中
 * @Date 2019/6/14
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<>();//支持并发读写
    // CopyOnWriteArrayList 不支持跨进程间的数据对象传递

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();// 专门处理进程间通信的保存的容易

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();//支持并发读写
    private CopyOnWriteArrayList<Book1> mBookList1 = new CopyOnWriteArrayList<>();//支持并发读写

    /**
     * 保存一个boolean 值 是否退出服务
     */
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean();

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("《计算机组成原理》", 56.8f));
        mBookList.add(new Book("《计算机组成原理》", 56.8f));
//        mBookList1.add(new Book1("《计算机组成原理1》", 56.8f));
//        mBookList1.add(new Book1("《Java程序设计1》", 48.5f));
        new Thread(new ServiceWorker()).start();
    }

    class ServiceWorker implements Runnable {

        @Override
        public void run() {
            //添加图书
            while (!mAtomicBoolean.get()) {// Service是否死亡，3秒添加一次图书
                try {
                    Thread.sleep(3000);
                    int size = mBookList.size() + 1;
                    String name = "《计算机网络第" + size + "版》";
                    Log.e(TAG, "ServiceWorker:" + name);
                    Book book = new Book(name, 38.8f);
                    onBookArrived(book);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onBookArrived(Book book) {
        mBookList.add(book);
//        for (IOnNewBookArrivedListener listener : mListenerList) {//CopyOnWriteArrayList 形式
//            if (listener != null) {
//                try {
//                    listener.onNewBookArrived(book);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        // RemoteCallbackList 形式的回调
        int count = mListenerList.beginBroadcast(); // RemoteCallbackList成对使用的
        for (int i = 0; i < count; i++) {
            IOnNewBookArrivedListener broadcastItem = mListenerList.getBroadcastItem(i);
            try {
                broadcastItem.onNewBookArrived(book);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListenerList.finishBroadcast();// RemoteCallbackList成对使用的
    }

    /**
     * aidl接口类，调用Binder端的接口
     */
    private IBinder mBinder = new IBookManager.Stub() {
        // 服务端实现的抽象方法
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }


        @Override
        public void addBook(Book book) throws RemoteException {
            if (book != null) {
                Log.e(TAG, "服务端addBook:" + book.getName() + "-" + book.getPrice());
            }
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);//CopyOnWriteArrayList 保存aidl 的接口实现
//            } else {
//                Log.e(TAG, "already exists!");
//            }
            mListenerList.register(listener);// 注册
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.e(TAG, "unRegister success");
//            } else {
//                Log.e(TAG, "not found ,can not unresigter!");
//                //CopyOnWriteArrayList 集合解绑失败，注册和解除注册使用的是同一个对象，但是经过Binder后就会变成不同的对象。所以找不到，解除失败。
//            }
            boolean unregister = mListenerList.unregister(listener);// RemoteCallbackList 解绑
            Log.e(TAG, "unregister:" + unregister);//unregister:true
        }
    };

//    private IBinder mBinder = new BookManagerImpl1() {
//        @Override
//        public void addBook(Book1 book) throws RemoteException {
//            if (book != null) {
//                Log.e(TAG, "手写服务端addBook:" + book.getName() + "-" + book.getPrice());
//            }
//            mBookList1.add(book);
//        }
//
//        @Override
//        public List<Book1> getBookList() throws RemoteException {
//            return mBookList1;
//        }
//    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mAtomicBoolean.set(true);
        super.onDestroy();

    }
}
