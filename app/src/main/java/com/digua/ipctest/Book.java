package com.digua.ipctest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: Book
 * @Description:
 * @Date 2019/6/14
 */

public class Book implements Parcelable {
    private String name;
    private float price;

    public Book(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeFloat(this.price);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.price = in.readFloat();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
