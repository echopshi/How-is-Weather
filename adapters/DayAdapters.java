package com.echopshi.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echopshi.weather.GetWeather.Day;
import com.echopshi.weather.R;

/**
 * Created by ann on 2015-09-25.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }

    // get us the count of numbers in the array that adapter is using
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;// we aren't using this , this is help tag items for easy reference.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            //brand new, need to create everything
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView)convertView.findViewById(R.id.temptureLabel);
            holder.dayLabel = (TextView)convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        Day day = mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax()+"");

        if(position == 0){
            holder.dayLabel.setText("Today");
        }else {
            holder.dayLabel.setText(day.getDayOfWeek());
        }
        return convertView;
    }


    public static class ViewHolder{
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
