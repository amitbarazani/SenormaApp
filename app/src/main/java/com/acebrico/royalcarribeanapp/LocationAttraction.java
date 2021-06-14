package com.acebrico.royalcarribeanapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;


public class LocationAttraction
        //implements Parcelable
    {

    public String name;
    public Double lng;
    public Double lat;
    public Double distanceFromCurrentPlace;
    public Double rating = 0.0;
    public String description = "";
    public Boolean isOpen = false;
    public Boolean isChosen = false;
    public Bitmap pictureBitmap = null;
   // public byte[] pictureBytes;
    public String type;

    public LocationAttraction(String name,Double lat,Double lng, Double rating, String description, Boolean isOpen, Boolean isChosen) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.isOpen = isOpen;
        this.isChosen = isChosen;
        this.lng = lng;
        this.lat = lat;
    }

    public LocationAttraction() {

    }
    /*
    public LocationAttraction(Parcel p) {
        this.description = p.readString();
        this.name = p.readString();
        this.lat = p.readDouble();
        this.lng = p.readDouble();
        this.distanceFromCurrentPlace = p.readDouble();
        //this.pictureBitmap  = p.readParcelable(Bitmap.class.getClassLoader());
        p.readByteArray(this.pictureBytes);
         this.pictureBitmap = BitmapFactory.decodeByteArray(this.pictureBytes, 0, this.pictureBytes.length);
        this.type = p.readString();
    }

     */

    @Override
    public String toString() {
        return "LocationAttraction{" +
                "name='" + name + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", isOpen=" + isOpen +
                ", isChosen=" + isChosen
                + '}';
    }

    /*
    @Override
    public int describeContents() {
        return 0;
    }

     */





    /*
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeDouble(this.distanceFromCurrentPlace);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lng);
        parcel.writeString(this.type);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.pictureBytes = stream.toByteArray();
        parcel.writeByteArray(this.pictureBytes);
    }

     */

    /*
    public static final Parcelable.Creator<LocationAttraction> CREATOR = new Parcelable.Creator<LocationAttraction>()
    {
        public LocationAttraction createFromParcel(Parcel in)
        {
            return new LocationAttraction(in);
        }
        public LocationAttraction[] newArray(int size)
        {
            return new LocationAttraction[size];
        }
    };

     */

}

