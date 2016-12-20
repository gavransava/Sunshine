package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.SettingsActivity;
import com.example.savagavran.sunshine.data.WeatherListItem;

import java.util.List;

public interface Presenter {

    interface MainPresenter {

        // View -> Presenter
        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);
        boolean hasLocationChanged(Context context);
        void bulkInsert(List<WeatherListItem> weather, Time dayTime, int julianStartDay, String locationId);
    }

    interface ForecastPresenter {
        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);

        ForecastAdapter setUpForecastAdapter(boolean useTodayLayout);

        void setUseTodayLayout(boolean useTodayLayout);

        void openPreferredLocationInMap();

        void onLocationOrUnitChanged(Context context);

        String getPreferredLocation(FragmentActivity activity);

        void getDetailData(int position, String location);
    }

    interface SettingsPresenter {

        // View -> Presenter
        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);

        void onLocationChanged(Context context, String location);
        void onUnitChanged(Context context, int position);
        void onNotificationsChange(Context context, boolean notificationEnabled);

        void getLocationValue(SettingsPresenter presenter, Context context);
        void getUnitValue(SettingsPresenter presenter, Context context);
        void getNotificationsValue(SettingsPresenter presenter, Context context);
        void onUnitChanged(SettingsActivity settingsActivity);

        //Presenter helper methods (called on presenter argument from above get methods)
        void returnLocationValue(String value);
        void returnUnitValue(int value);
        void returnNotificationsValue(boolean value);
    }
}
