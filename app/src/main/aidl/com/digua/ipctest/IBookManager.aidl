// IBookManager.aidl
package com.digua.ipctest;
import com.digua.ipctest.Book;
import com.digua.ipctest.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {

  List<Book> getBookList();

  void addBook(in Book book);

  void registerListener(IOnNewBookArrivedListener listener);

  void unRegisterListener(IOnNewBookArrivedListener listener);
}
