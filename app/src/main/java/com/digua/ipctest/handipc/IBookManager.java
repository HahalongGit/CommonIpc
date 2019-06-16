package com.digua.ipctest.handipc;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.digua.ipctest.Book;

import java.util.List;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: IBookManager
 * @Description: 手动定义一个IBookManager
 * @Date 2019/6/14
 */

public interface IBookManager extends IInterface {

//    static final String DESCRIPTOR = "com.digua.ipctest.handipc.IBookManager";

//    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
//
//    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    void addBook(Book book) throws RemoteException;

    List<Book> getBookList() throws RemoteException;


}
