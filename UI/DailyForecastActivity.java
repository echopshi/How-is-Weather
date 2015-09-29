package com.echopshi.weather.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echopshi.weather.GetWeather.Day;
import com.echopshi.weather.R;
import com.echopshi.weather.adapters.DayAdapter;

import java.util.Arrays;

import butterknife.Bind;

public class DailyForecastActivity extends ListActivity {

    private static final String TAG = DailyForecastActivity.class.getSimpleName();
    private Day[] mDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.v(TAG, "daily "+MainActivity.cityName);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.Daily_Forecast);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dayOfTheWeek = mDays[position].getDayOfWeek();
        String condition = mDays[position].getSummary();
        String highTemp = mDays[position].getTemperatureMax()+"";
        String message = String.format("On %s the high will be %s and the day will be %s", dayOfTheWeek, condition, highTemp);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
