package com.example.savagavran.sunshine.model;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.R;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.data.WeatherContract;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenterImpl;
import com.example.savagavran.sunshine.sync.SunshineSyncAdapter;

import java.lang.ref.WeakReference;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.savagavran.sunshine.ForecastFragment.LOG_TAG;

public class MainModel
        implements Model.ModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private WeakReference<Context> mContext;
    private ForecastFragmentPresenterImpl mForecastFragmentPresenter;
    private WeakReference<LoaderManager> mLoader;
    private boolean mLocationChanged;

    @Override
    public ForecastAdapter setUpForecastAdapter(RequiredView.ForecastViewOps context, boolean useTodayLayout) {
        mForecastAdapter = new ForecastAdapter(context.returnParentActivity(), null, 0);
        mForecastAdapter.setUseTodayLayout(useTodayLayout);
        return mForecastAdapter;
    }

    @Override
    public void setPresenter(ForecastFragmentPresenterImpl forecastFragmentPresenter) {
        mForecastFragmentPresenter = forecastFragmentPresenter;
    }

    private ForecastAdapter mForecastAdapter;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    private static final int FORECAST_LOADER = 0;

    public void updateWeather(Context context) {
        SunshineSyncAdapter.syncImmediately(context);
    }

    @Override
    public void setUseTodayLayout(boolean useTodayLayout) {
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(useTodayLayout);
        }
    }

    @Override
    public void openPreferredLocationInMap(RequiredView.ForecastViewOps temp) {
        if (null != mForecastAdapter) {
            Cursor c = mForecastAdapter.getCursor();
            if (null != c) {
                c.moveToPosition(0);
                String posLat = c.getString(COL_COORD_LAT);
                String posLong = c.getString(COL_COORD_LONG);
                Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

                if (intent.resolveActivity(temp.returnParentActivity().getPackageManager()) != null) {
                    temp.returnParentActivity().startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
                }
            }
        }
    }

    @Override
    public void onLocationOrUnitChanged(Context context, LoaderManager locationManager) {
        updateWeather(context);
        if(locationManager != null) {
            locationManager.restartLoader(FORECAST_LOADER, null, this);
        } else {
            LoaderManager temp = mLoader.get();
            if(temp != null){
                temp.restartLoader(FORECAST_LOADER, null, this);
            }
        }
    }

    private String mLocation;
    private boolean mUnitChanged;
    private boolean mNotificationValue;
    private int mUnitValue;

    @Override
    public boolean hasUnitChanged(Context context) {
        mUnitChanged = Utility.isMetric(context);

        return false;
    }

    @Override
    public String getLocationValue() {
        return mLocation;
    }

    @Override
    public int getUnitValue() {
        return mUnitValue;
    }

    @Override
    public boolean getNotificationsValue() {
        return mNotificationValue;
    }

    @Override
    public void onNotificationChanged(Context context, boolean notificationEnabled) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(context.getString(R.string.pref_enable_notifications_key), notificationEnabled);
        mNotificationValue = notificationEnabled;
        editor.apply();
    }

    @Override
    public void onUnitChanged(Context context, int position) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putInt(context.getString(R.string.pref_units_key), position);
        mUnitValue = position;
        editor.apply();
    }

    @Override
    public void onLocationChanged(Context context, String location) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        if(mLocation != location){
            mLocationChanged = true;
            mLocation = location;
        }
        else {
            mLocationChanged = false;
        }
        editor.putString(context.getString(R.string.pref_location_key), location);

        editor.apply();
    }

    @Override
    public boolean hasLocationChanged(Context context) {
        if(mLocationChanged){
            mLocationChanged = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void synchronise(Context context) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Context temp;

        if (mContext.get() != null) {
            temp = mContext.get();

            // Sort order:  Ascending, by date.
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

            String locationSetting = Utility.getPreferredLocation(temp);
            mLocation = locationSetting;
            Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                    locationSetting, System.currentTimeMillis());

            return new CursorLoader(mContext.get(),
                    weatherForLocationUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
        return null;
    }

    @Override
    public String getPreferredLocation(FragmentActivity activity) {
        return Utility.getPreferredLocation(activity);
    }

    @Override
    public void initLoader(LoaderManager loaderManager, Context context) {
        mContext = new WeakReference<>(context);
        loaderManager.initLoader(FORECAST_LOADER, null, this);
        mLoader = new WeakReference<>(loaderManager);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if(data.getCount() != 0) {
            if (mContext.get() != null) {
                mForecastFragmentPresenter.setRetryLayoutVisibility(View.GONE);
                mForecastFragmentPresenter.scrollToPosition();
            }
        } else {
            mForecastFragmentPresenter.setRetryLayoutVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    public MainModel() {

    }
}