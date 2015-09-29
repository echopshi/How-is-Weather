package com.echopshi.weather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echopshi.weather.GetWeather.Hour;
import com.echopshi.weather.R;

import org.w3c.dom.Text;

/**
 * Created by ann on 2015-09-26.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder>{

    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context context, Hour[] hours){
        mContext = context;
        mHours = hours;
    }


    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(mHours[i]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mSummaryLabel;
        public TextView mTempLabel;
        public ImageView mIconImageLabel;
        public TextView mTimeLabel;

        public HourViewHolder(View itemView) {
            super(itemView);

            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTempLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mIconImageLabel = (ImageView) itemView.findViewById(R.id.iconImageView);

            // must have this line for working with recyclerView
            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour){
            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText(hour.getSummary());
            mIconImageLabel.setImageResource(hour.getIconId());
            mTempLabel.setText(hour.getTemperature()+"");
        }

        @Override
        public void onClick(View v) {
            String summary = mSummaryLabel.getText().toString();
            String temp = mTempLabel.getText().toString();
            String time = mTimeLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s ", time, temp, summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        }
    }
}
