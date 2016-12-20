package com.example.savagavran.sunshine.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.savagavran.sunshine.BuildConfig;
import com.example.savagavran.sunshine.ForecastFragment;
import com.example.savagavran.sunshine.R;
import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.WeatherListItem;
import com.example.savagavran.sunshine.data.WeatherResponse;
import com.example.savagavran.sunshine.presenter.Presenter;
import com.example.savagavran.sunshine.sync.error.APIError;
import com.example.savagavran.sunshine.sync.error.ErrorUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SunshineSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private WeakReference<ForecastFragment> mForecastFragment;

    private static Presenter.MainPresenter mMainPresenter;


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
            // Response<WeatherResponse> response = call.execute();

            // Async
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if(response.isSuccessful()) {
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


    private void writeToDatabase(Response<WeatherResponse> response){
        try {
            List<WeatherListItem> weather = response.body().getResults();
            Log.d(LOG_TAG, "Number of weather days received: " + weather.size());
            String locationId = response.body().getCity().getId();
            Time dayTime = new Time();
            dayTime.setToNow();
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
            dayTime = new Time();

            if(weather.size() > 0) {
                mMainPresenter.bulkInsert(weather, dayTime, julianStartDay,locationId);
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
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

            // we'll query our contentProvider, as always
//            Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

//            if (cursor.moveToFirst()) {
//                int weatherId = cursor.getInt(INDEX_WEATHER_ID);
//                double high = cursor.getDouble(INDEX_MAX_TEMP);
//                double low = cursor.getDouble(INDEX_MIN_TEMP);
//                String desc = cursor.getString(INDEX_SHORT_DESC);
//
//                int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
//                String title = context.getString(R.string.app_name);
//
//                // Define the text of the forecast.
//                String contentText = String.format(context.getString(R.string.format_notification),
//                        desc,
//                        Utility.formatTemperature(context, high),
//                        Utility.formatTemperature(context, low));
//
//                Intent resultIntent = new Intent(getContext(), MainActivity.class);
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//                stackBuilder.addNextIntent(resultIntent);
//                PendingIntent resultPendingIntent =
//                        stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_CANCEL_CURRENT
//                        );
//
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(getContext())
//                                .setSmallIcon(iconId)
//                                .setContentTitle(title)
//                                .setContentText(contentText);
//
//                mBuilder.setAutoCancel(true);
//                mBuilder.setContentIntent(resultPendingIntent);
//
//                NotificationManager mNotifyMgr =
//                        (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
//                mNotifyMgr.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());
//
//                //refreshing last sync
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putLong(lastNotificationKey, System.currentTimeMillis());
//                editor.commit();
//            }
        }
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
    public static void initializeSyncAdapter(Context context, Presenter.MainPresenter presenter) {
        mMainPresenter = presenter;
        getSyncAccount(context);
    }
}