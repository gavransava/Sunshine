package com.example.savagavran.sunshine.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.savagavran.sunshine.DetailData;
import com.example.savagavran.sunshine.R;
import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.data.WeatherContract;
import com.example.savagavran.sunshine.presenter.DetailFragmentPresenterImpl;

import java.lang.ref.WeakReference;

public class DetailModel
        implements Model.DetailModelOps, LoaderManager.LoaderCallbacks<Cursor> {

    private WeakReference<Context> mContext;
    private Uri mUri;
    private static final int DETAIL_LOADER = 0;
    private DetailFragmentPresenterImpl mDetailFragmentPresenter;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY = 5;
    public static final int COL_WEATHER_PRESSURE = 6;
    public static final int COL_WEATHER_WIND_SPEED = 7;
    public static final int COL_WEATHER_DEGREES = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;
    private WeakReference<LoaderManager> mLoader;


    @Override
    public void setPresenter(DetailFragmentPresenterImpl detailFragmentPresenter) {
        mDetailFragmentPresenter = detailFragmentPresenter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mUri != null) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    mContext.get(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context temp;
        if (data != null && data.moveToFirst()) {
            if (mContext.get() != null) {
                temp = mContext.get();

                DetailData detailData = new DetailData();

                int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
                long date = data.getLong(COL_WEATHER_DATE);
                double high = data.getDouble(COL_WEATHER_MAX_TEMP);
                double low = data.getDouble(COL_WEATHER_MIN_TEMP);
                float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
                float pressure = data.getFloat(COL_WEATHER_PRESSURE);
                float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
                float windDirStr = data.getFloat(COL_WEATHER_DEGREES);

                String friendlyDateText = Utility.getDayName(temp, date);
                String dateText = Utility.getFormattedMonthDay(temp, date);
                String description = data.getString(COL_WEATHER_DESC);
                String highString = Utility.formatTemperature(temp, high);
                String lowString = Utility.formatTemperature(temp, low);
                String humidityString = temp.getString(R.string.format_humidity, humidity);
                String pressureString = temp.getString(R.string.format_pressure, pressure);
                String windInfoString = Utility.getFormattedWind(temp, windSpeedStr, windDirStr);

                detailData.setWeatherId(Utility.getArtResourceForWeatherCondition(weatherId));
                detailData.setFriendlyDateText(friendlyDateText);
                detailData.setDateText(dateText);
                detailData.setDescription(description);
                detailData.setMaxTemp(highString);
                detailData.setLowTemp(lowString);
                detailData.setHumidity(humidityString);
                detailData.setPressure(pressureString);
                detailData.setWindInfo(windInfoString);

                mDetailFragmentPresenter.populateDetailFragmentWithData(detailData);
            }
        }
    }

    @Override
    public void onLocationChanged() {
        Context temp;
        if (mContext.get() != null) {
            temp = mContext.get();

            String newLocation = Utility.getPreferredLocation(temp);
            if (mUri != null) {
                long date = WeatherContract.WeatherEntry.getDateFromUri(mUri);
                mUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            }
            LoaderManager loaderTemp = mLoader.get();
            if(loaderTemp != null)
                loaderTemp.restartLoader(DETAIL_LOADER, null, this);

        }
    }

    @Override
    public void onLoaderReset(Loader < Cursor > loader) {
    }


    public void initLoader(LoaderManager loaderManager, Context context, Uri uri) {
        mContext = new WeakReference<>(context);
        mUri = uri;
        mLoader = new WeakReference<>(loaderManager);
        loaderManager.initLoader(DETAIL_LOADER, null, this);
    }
}
