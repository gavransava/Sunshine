package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.SettingsActivity;

public interface Presenter {

    interface MainPresenter {

        // View -> Presenter

        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);
        boolean hasLocationChanged(Context context);

        // Model -> Presenter
    }

    interface ForecastPresenter {
        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);

        void initLoader(Context context);

        ForecastAdapter setUpForecastAdapter(boolean useTodayLayout);

        void setUseTodayLayout(boolean useTodayLayout);

        void openPreferredLocationInMap();

        void onLocationOrUnitChanged(Context context);

        String getPreferredLocation(FragmentActivity activity);
    }

    interface SettingsPresenter {

        // View -> Presenter
        void onConfigurationChanged(RequiredView.RequiredViewOps view);
        void onDestroy(boolean isChangingConfig);

        void onLocationChanged(Context context, String location);
        void onUnitChanged(Context context, int position);
        void onNotificationsChange(Context context, boolean notificationEnabled);

        void getLocationValue(Presenter.SettingsPresenter presenter);
        void getUnitValue(Presenter.SettingsPresenter presenter);
        void getNotificationsValue(Presenter.SettingsPresenter presenter);
        void onUnitChanged(SettingsActivity settingsActivity);

        //Presenter helper methods (called on presenter argument from above get methods)
        void returnLocationValue(String value);
        void returnUnitValue(int value);
        void returnNotificationsValue(boolean value);


    }
}
