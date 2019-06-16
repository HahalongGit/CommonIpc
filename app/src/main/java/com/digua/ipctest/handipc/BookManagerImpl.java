package com.digua.ipctest.handipc;

import android.os.Binder;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: BookManagerImpl
 * @Description: BookManagerImpl 就是 aidl 文件生成的java 类的内部类Stub
 * 对于进程间通信来说，
 * @Date 2019/6/14
 */

public abstract class BookManagerImpl extends Binder implements IBookManager {


    private static final java.lang.String DESCRIPTOR = "com.digua.ipctest.IBookManager";

    /**
     * Construct the stub at attach it to the interface.
     */
    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * Cast an IBinder object into an com.digua.ipctest.IBookManager interface,
     * generating a proxy if needed.
     */
    public static com.digua.ipctest.handipc.IBookManager asInterface(android.os.IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof com.digua.ipctest.handipc.IBookManager))) {
            return ((com.digua.ipctest.handipc.IBookManager) iin);
        }
        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public android.os.IBinder asBinder() {
        return this;
    }

    @Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                java.util.List<com.digua.ipctest.Book> _result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(_result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                com.digua.ipctest.Book _arg0;
                if ((0 != data.readInt())) {
                    _arg0 = com.digua.ipctest.Book.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                this.addBook(_arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

//    @Override
//    public void addBook(Book book) throws RemoteException {
//        //todo 待实现 // 服务端实现的抽象方法，在Service中添加数据
//    }
//
//    @Override
//    public List<Book> getBookList() throws RemoteException {
//        //todo 待实现 // 服务端实现的抽象方法 返回数据
//        return null;
//    }

    private static class Proxy implements com.digua.ipctest.handipc.IBookManager {
        private android.os.IBinder mRemote;

        Proxy(android.os.IBinder remote) {
            mRemote = remote;
        }

        @Override
        public android.os.IBinder asBinder() {
            return mRemote;
        }

        public java.lang.String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public java.util.List<com.digua.ipctest.Book> getBookList() throws android.os.RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            java.util.List<com.digua.ipctest.Book> _result;
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(BookManagerImpl.TRANSACTION_getBookList, _data, _reply, 0);
                _reply.readException();
                _result = _reply.createTypedArrayList(com.digua.ipctest.Book.CREATOR);
            } finally {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }

        @Override
        public void addBook(com.digua.ipctest.Book book) throws android.os.RemoteException {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    _data.writeInt(1);
                    book.writeToParcel(_data, 0);
                } else {
                    _data.writeInt(0);
                }
                mRemote.transact(BookManagerImpl.TRANSACTION_addBook, _data, _reply, 0);
                _reply.readException();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }
    }

    static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);

}
