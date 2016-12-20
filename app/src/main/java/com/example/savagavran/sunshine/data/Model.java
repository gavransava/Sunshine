package com.example.savagavran.sunshine.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.presenter.DetailFragmentPresenterImpl;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenterImpl;

import java.util.List;

public interface Model {
    // Presenter -> Model
    interface ModelOps {
        boolean hasLocationChanged(Context context);
        void onLocationChanged(Context context, String location);
        void onUnitChanged(Context context, int position);
        void onNotificationChanged(Context context, boolean notificationEnabled);

        String getLocationValue(Context context);
        int getUnitValue(Context context);
        boolean getNotificationsValue(Context context);

        //ForecastFragmentPresenter -> Model
        void onLocationOrUnitChanged(Context context);

        ForecastAdapter setUpForecastAdapter(RequiredView.ForecastViewOps context, boolean useTodayLayout);

        void setUseTodayLayout(boolean useTodayLayout);

        void openPreferredLocationInMap(RequiredView.ForecastViewOps temp);

        String getPreferredLocation(FragmentActivity activity);

        void bulkInsert(List<WeatherListItem> weather, Time dayTime, int julianStartDay, String locationId);

        void setDetailPresenter(DetailFragmentPresenterImpl detailFragmentPresenter);

        void setForecastPresenter(ForecastFragmentPresenterImpl forecastFragmentPresenter);

        void getDetailData(int position, String location);
    }
}
