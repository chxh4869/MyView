package com.study.myview;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.RequiresApi;

public class MyViewSave extends View.BaseSavedState{

    public int present;

    public MyViewSave(Parcel source) {
        super(source);
        present = source.readInt();
    }


    public MyViewSave(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(present);
    }

    public static final Parcelable.Creator<MyViewSave> CREATOR = new Parcelable.Creator<MyViewSave>(){

        @Override
        public MyViewSave createFromParcel(Parcel source) {
            return new MyViewSave(source);
        }

        @Override
        public MyViewSave[] newArray(int size) {
            return new MyViewSave[size];
        }
    };

}
