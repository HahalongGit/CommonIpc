// IOnNewBookArrivedListener.aidl
package com.digua.ipctest;
import com.digua.ipctest.Book;
// Declare any non-default types here with import statements
// 新书到达时通知
interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book book);
}
