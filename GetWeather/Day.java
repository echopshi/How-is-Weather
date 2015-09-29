package com.echopshi.weather.GetWeather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ann on 2015-09-24.
 */
public class Day implements Parcelable{
    private String mIcon;
    private long mTime;
    private double mTemperatureMax;
    private String mSummary;
    private String mTimeZone;

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

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {

        mTemperatureMax = (temperatureMax - 32) * 5 / 9;
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

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date date = new Date(mTime *1000);
        return formatter.format(date);
    }

    public Day(){ }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeString(mTimeZone);
        dest.writeDouble(mTemperatureMax);
    }

    //  this method is used to unwrite to Parcel ,and the order must be same as the write to Parcel.
    private Day(Parcel in){
        mTime = in.readLong();
        mSummary = in.readString();
        mIcon = in.readString();
        mTimeZone = in.readString();
        mTemperatureMax = in.readDouble();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
