package com.echopshi.weather.UI;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.echopshi.weather.GetWeather.Hour;
import com.echopshi.weather.R;
import com.echopshi.weather.adapters.HourAdapter;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class hourlyForecastActivity extends AppCompatActivity {
    private Hour[] mHours;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        // must add this line after we called the @Bind at beginning.
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.Hourly_Forecast);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // helps to be performs
        mRecyclerView.setHasFixedSize(true);
    }


}
