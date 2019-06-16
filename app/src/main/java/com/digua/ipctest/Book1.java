package com.digua.ipctest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by longlong on 2019/6/14.
 *
 * @ClassName: Book1
 * @Description:
 * @Date 2019/6/14
 */

public class Book1 implements Parcelable {

    private String name;
    private float price;

    public Book1(String name, float price) {
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

    protected Book1(Parcel in) {
        this.name = in.readString();
        this.price = in.readFloat();
    }

    public static final Parcelable.Creator<Book1> CREATOR = new Parcelable.Creator<Book1>() {
        @Override
        public Book1 createFromParcel(Parcel source) {
            return new Book1(source);
        }

        @Override
        public Book1[] newArray(int size) {
            return new Book1[size];
        }
    };
}
