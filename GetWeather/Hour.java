package com.echopshi.weather.GetWeather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ann on 2015-09-24.
 */
public class Hour implements Parcelable{
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private String mSummary;
    private String mTimeZone;

    public Hour(){ }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = (temperature - 32) * 5 / 9;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() {
        return 0;//ignore this method
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemperature);
        dest.writeString(mIcon);
        dest.writeString(mSummary);
        dest.writeString(mTimeZone);
        dest.writeLong(mTime);
    }

    private Hour(Parcel in){
        mTemperature = in.readDouble();
        mIcon = in.readString();
        mSummary = in.readString();
        mTimeZone = in.readString();
        mTime = in.readLong();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
