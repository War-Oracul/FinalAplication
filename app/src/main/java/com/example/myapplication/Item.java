package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String Name;
    private String Tips;
    private String Editions;
    private int id;

    public Item(String Name, String Tips, String Editions, int id){
        this.Name = Name;
        this.Tips = Tips;
        this.Editions = Editions;
        this.id = id;
    }

    protected Item(Parcel in) {
        Name = in.readString();
        Tips = in.readString();
        Editions = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName(){
        return Name;
    }
    public void setName(String Name){
        this.Name = Name;
    }

    public String getTips(){
        return Tips;
    }
    public void setTips(String Tips){
        this.Tips = Tips;
    }

    public String getEditions(){
        return Editions;
    }
    public void setEditions(String Editions){
        this.Editions = Editions;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Tips);
        parcel.writeString(Editions);
    }
}
