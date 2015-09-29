package com.echopshi.weather;

/**
 * Created by ann on 2015-09-29.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by ann on 2015-09-29.
 */
public class LocationFinder {

    private static final String TAG = LocationFinder.class.getSimpleName();
    private final Context mContext;
    boolean isGPSwork = false;
    boolean isNETwork = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    public LocationFinder(Context context){
        this.mContext = context;
        GetLocation();
    }
    public Location GetLocation() {

        location = new Location("");

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location l) {
                location = l;
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                //Log.v(TAG, "listener:lat" + latitude + "long" + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSwork = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNETwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

       // Log.v(TAG, "checkGPS" + isGPSwork +isNETwork);
        //Log.v(TAG, "first:lat" + latitude + "long" + longitude);

        if (!isGPSwork && !isNETwork) {
            // no Network and GPS, should return the default location which is Toronto, CA
            buildAlertMessageNoGps();
            //Log.v(TAG, "nothing:lat" + latitude + "long" + longitude);
        } else {
            this.canGetLocation = true;
            try {
                if (isNETwork) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 10, listener);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        //Log.v(TAG, "NET:lat" + latitude + "long" + longitude);
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSwork) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 10, listener);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                       // Log.v(TAG, "GPS:lat" + latitude + "long" + longitude);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
       // Log.v(TAG, "last:lat" + latitude + "long" + longitude);
        return location;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public double getLatitude(){
        return latitude;
}

    public double getLongitude(){
        return longitude;
    }

    // do not know where get wrong
    public String getCityName() {
        String cityName = "Markham, ON";
        Geocoder geo = new Geocoder(mContext.getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            // this line is never make change, and the size is always and forever be 0 no matter what i do.
            addresses = geo.getFromLocation(latitude, longitude, 3);
            //Log.v(TAG, "size: "+addresses.size());

            if (addresses.size() != 0){
                cityName = addresses.get(0).getLocality();
                //Log.v(TAG, "location if stat:"+ cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.v(TAG, "location:"+ cityName);
        return cityName;
    }
}

