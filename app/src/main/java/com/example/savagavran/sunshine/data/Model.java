package com.example.savagavran.sunshine.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.widget.AdapterView;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.presenter.DetailFragmentPresenterImpl;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenterImpl;

public interface Model {
    // Presenter -> Model
    interface ModelOps {
        boolean hasLocationChanged(Context context);
        void synchronise(Context context);
        void onLocationChanged(Context context, String location);
        void onUnitChanged(Context context, int position);
        void onNotificationChanged(Context context, boolean notificationEnabled);

        String getLocationValue();
        int getUnitValue();
        boolean getNotificationsValue();

        //ForecastFragmentPresenter -> Model
        void onLocationOrUnitChanged(Context context,  LoaderManager locationManager);

        void initLoader(LoaderManager loaderManager, Context context);

        ForecastAdapter setUpForecastAdapter(RequiredView.ForecastViewOps context, boolean useTodayLayout);

        void setUseTodayLayout(boolean useTodayLayout);

        void openPreferredLocationInMap(RequiredView.ForecastViewOps temp);

        void setPresenter(ForecastFragmentPresenterImpl forecastFragmentPresenter);

        String getPreferredLocation(FragmentActivity activity);

        Uri buildWeatherLocationWithDate(AdapterView<?> adapterView, int position, String locationSetting);
    }

    interface DetailModelOps {

        void initLoader(LoaderManager loaderManager, Context context, Uri uri);

        void setPresenter(DetailFragmentPresenterImpl detailFragmentPresenter);

        void onLocationChanged();
    }
}
