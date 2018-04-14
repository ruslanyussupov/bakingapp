package com.ruslaniusupov.android.bakingapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private int mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(int quantity, String measure, String name) {
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mName = name;
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mName = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mName);
    }
}
