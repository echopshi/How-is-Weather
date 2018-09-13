package com.echopshi.weather.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echopshi.weather.GetWeather.Current;
import com.echopshi.weather.GetWeather.Day;
import com.echopshi.weather.GetWeather.Forecast;
import com.echopshi.weather.GetWeather.Hour;
import com.echopshi.weather.LocationFinder;
import com.echopshi.weather.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String Daily_Forecast = "DAILY_FORECAST";
    public static final String Hourly_Forecast = "HOURLY_FORECAST";
    private Forecast mForecast;
    public static String cityName;

    // use the older way to set the textView, not the butterKnife
    // private TextView mTempLabel;
    // the way butterKnife works
    @Bind(R.id.Time_Lable) TextView mTimeLabel;
    @Bind(R.id.tempLabel) TextView mTempLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summary) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.location_Label) TextView mLocationLabel;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.WindValue) TextView mWindValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        // using the location finder to search for user location and update their weather on forecast.
        LocationFinder locationFinder = new LocationFinder(this);
        final double latitude = locationFinder.getLatitude();
        final double longitude = locationFinder.getLongitude();
        cityName = locationFinder.getCityName();
        //Log.v(TAG, "main:lat" + latitude + "long" + longitude + "city "+ cityName);


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        //mTempLabel = (TextView) findViewById(R.id.tempLabel);

        getForecast(latitude, longitude);
        //Log.d(TAG, "Main UI code is running");
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "0c16c259e3e96f30cde83419d319ca91";
        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            taggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taggleRefresh();
                        }
                    });
                    try {
                        String JsonData = response.body().string();
                        //Log.v(TAG, JsonData);
                        if (response.isSuccessful()) {
                            // we will get the current weather date from this method,the data will be the JSON format.
                            mForecast = getForecastDetails(JsonData);
                            // let user can update the wether whenever they want.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            // when we not get the response form web, this may be caused by not network connect or connect error.
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private void taggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mRefreshImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTempLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be ");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        mLocationLabel.setText(cityName);
        mWindValue.setText(current.getWindSpeed() + "");

        Drawable drawable = ContextCompat.getDrawable(this, current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast getForecastDetails(String jsonDate) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetail(jsonDate));
        forecast.setDailyForecast(getDailyDetail(jsonDate));
        forecast.setHourlyForecast(getHourlyDetail(jsonDate));
        return forecast;
    }

    private Hour[] getHourlyDetail(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String TimeZone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for (int i = 0; i < data.length(); i++){
            JSONObject JsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setTemperature(JsonHour.getDouble("temperature"));
            hour.setTime(JsonHour.getLong("time"));
            hour.setIcon(JsonHour.getString("icon"));
            hour.setTimeZone(TimeZone);
            hour.setSummary(JsonHour.getString("summary"));

            hours[i] = hour;
        }
        return hours;
    }

    private Day[] getDailyDetail(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String TimeZone = forecast.getString("timezone");
        JSONObject Daily = forecast.getJSONObject("daily");
        JSONArray data = Daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for (int i = 0; i < data.length(); i++){
            JSONObject JsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setTemperatureMax(JsonDay.getDouble("temperatureMax"));
            day.setTime(JsonDay.getLong("time"));
            day.setIcon(JsonDay.getString("icon"));
            day.setTimeZone(TimeZone);
            day.setSummary(JsonDay.getString("summary"));

            days[i] = day;
        }
        return days;
    }

    // why we do throws instead of try catch block is that we can now put the responsibility of handle the exceptions to the whoever called this method.
    private Current getCurrentDetail(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String TimeZone = forecast.getString("timezone");
        //Log.i(TAG, "From JSON:  "+TimeZone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(TimeZone);
        current.setWindSpeed(currently.getDouble("windSpeed"));
        //to see wheather the time is displayed in the format or not.
       // Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    // butter knife gives a simple way to make a on click listener method.
    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(Daily_Forecast, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, hourlyForecastActivity.class);
        intent.putExtra(Hourly_Forecast, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
