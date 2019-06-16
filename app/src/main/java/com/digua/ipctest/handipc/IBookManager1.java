package com.digua.ipctest.handipc;

import android.os.IInterface;
import android.os.RemoteException;

import com.digua.ipctest.Book;
import com.digua.ipctest.Book1;

import java.util.List;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: IBookManager
 * @Description: 手动定义一个IBookManager
 * @Date 2019/6/14
 */

public interface IBookManager1 extends IInterface {

//    static final String DESCRIPTOR = "com.digua.ipctest.handipc.IBookManager";

//    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
//
//    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    void addBook(Book1 book) throws RemoteException;

    List<Book1> getBookList() throws RemoteException;


}
