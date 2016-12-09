package com.example.savagavran.sunshine.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.savagavran.sunshine.BuildConfig;
import com.example.savagavran.sunshine.ForecastFragment;
import com.example.savagavran.sunshine.MainActivity;
import com.example.savagavran.sunshine.R;
import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.WeatherContract;
import com.example.savagavran.sunshine.data.WeatherListItem;
import com.example.savagavran.sunshine.data.WeatherResponse;
import com.example.savagavran.sunshine.sync.error.APIError;
import com.example.savagavran.sunshine.sync.error.ErrorUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SunshineSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private WeakReference<ForecastFragment> mForecastFragment;

    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[] {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;



    public SunshineSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        String locationQuery = Utility.getPreferredLocation(getContext());

        String format = "json";
        String units = "metric";
        int numDays = 14;
        TrafficStats.setThreadStatsTag(0xF00D);

        try {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<WeatherResponse> call = apiService.getDailyWeatherData(
                    locationQuery,
                    format,
                    units,
                    numDays,
                    BuildConfig.OPEN_WEATHER_MAP_API_KEY);

            // Sync
             Response<WeatherResponse> response = call.execute();

            // Async
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if(response.isSuccessful()) {
                        List<WeatherListItem> weather = response.body().getResults();
                        Log.d(LOG_TAG, "Number of weather days received: " + weather.size());
                        writeToDatabase(response);
                    } else {
                        APIError error = ErrorUtils.parseError(response.errorBody());
                        Log.d(LOG_TAG, error.getMessage());
                        showRetry(error.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.toString());
                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        TrafficStats.clearThreadStatsTag();
    }

    private void showRetry(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void writeToDatabase(Response<WeatherResponse> weather){
        try {

            long locationId = addLocation(weather.body().getCity());

            Time dayTime = new Time();
            dayTime.setToNow();
            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
            // now we work exclusively in UTC
            dayTime = new Time();

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Vector<ContentValues> cVVector = createContentValues(weather, dayTime, julianStartDay, locationId);
            writeContentValuesToDatabase(cVVector, dayTime, julianStartDay);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private long addLocation(WeatherResponse.City city ) {
        String locationSetting = city.getId();
        String cityName = city.getName();
        double cityLatitude = city.getCoordinates().getLatitude();
        double cityLongitude = city.getCoordinates().getLongitude();
        long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

        return locationId;
    }

    private Vector<ContentValues> createContentValues(Response<WeatherResponse> weather,
                                                      Time dayTime, int julianStartDay, long locationId) {

        final int WEATHER_IS_A_ONE_ITEM_LIST = 0;
        List<WeatherListItem> listItems = weather.body().getResults();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(listItems.size());


        for(int i = 0; i < listItems.size(); i++) {
            // Cheating to convert this to UTC time, which is what we want anyhow
            long dateTime = dayTime.setJulianDay(julianStartDay + i);
            WeatherListItem listItem = listItems.get(i);

            ContentValues weatherValues = new ContentValues();

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTime);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, listItem.getHumidity());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, listItem.getPressure());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, listItem.getSpeed());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, listItem.getDirection());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, listItem.getTemperature().getMax());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, listItem.getTemperature().getMin());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, listItem.getWeather().get(WEATHER_IS_A_ONE_ITEM_LIST).getDescription());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, listItem.getWeather().get(WEATHER_IS_A_ONE_ITEM_LIST).getId());

            cVVector.add(weatherValues);
        }
        return  cVVector;
    }

    private void writeContentValuesToDatabase(Vector<ContentValues> cVVector, Time dayTime, int julianStartDay) {
        int inserted = 0;
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = getContext().getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);

            getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
                    WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
                    new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});

            notifyWeather();
        }
        Log.d(LOG_TAG, "Sync Complete. " + inserted + " Inserted");
    }

    private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        String weatherNotification = context.getString(R.string.pref_enable_notifications_key);
        boolean notifyMe = prefs.getBoolean(weatherNotification, true);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS  && notifyMe) {
            // Last sync was more than 1 day ago, let's send a notification with the weather.
            String locationQuery = Utility.getPreferredLocation(context);

            Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

            // we'll query our contentProvider, as always
            Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

            if (cursor.moveToFirst()) {
                int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                double high = cursor.getDouble(INDEX_MAX_TEMP);
                double low = cursor.getDouble(INDEX_MIN_TEMP);
                String desc = cursor.getString(INDEX_SHORT_DESC);

                int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
                String title = context.getString(R.string.app_name);

                // Define the text of the forecast.
                String contentText = String.format(context.getString(R.string.format_notification),
                        desc,
                        Utility.formatTemperature(context, high),
                        Utility.formatTemperature(context, low));

                Intent resultIntent = new Intent(getContext(), MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(iconId)
                                .setContentTitle(title)
                                .setContentText(contentText);

                mBuilder.setAutoCancel(true);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotifyMgr =
                        (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());

                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }
        }
    }

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param locationSetting The location string used to request updates from the server.
     * @param cityName A human-readable city name, e.g "Mountain View"
     * @param lat the latitude of the city
     * @param lon the longitude of the city
     * @return the row ID of the added location.
     */
    long addLocation(String locationSetting, String cityName, double lat, double lon) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = getContext().getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);

            // Finally, insert location data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    WeatherContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();

        return locationId;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
          *   Since we've created an account
         */
        SunshineSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }
    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}