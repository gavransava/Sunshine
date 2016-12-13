package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.AdapterView;

import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

public class ForecastFragmentPresenterImpl
        implements Presenter.ForecastPresenter {

    @Override
    public Uri buildWeatherLocationWithDate(AdapterView<?> adapterView, int position, String locationSetting) {
        return mModel.buildWeatherLocationWithDate(adapterView, position, locationSetting);
    }

    private WeakReference<RequiredView.ForecastViewOps> mView;
    private Model.ModelOps mModel;

    @Override
    public String getPreferredLocation(FragmentActivity activity) {
        return mModel.getPreferredLocation(activity);
    }

    public ForecastFragmentPresenterImpl(RequiredView.ForecastViewOps view, Model.ModelOps model) {
        mView = new WeakReference<>(view);
        mModel = model;
        mModel.setPresenter(this);
    }

    @Override
    public void onLocationOrUnitChanged(Context context) {
        RequiredView.ForecastViewOps temp;
        if (mView.get() != null) {
            temp = mView.get();
            mModel.onLocationOrUnitChanged(context, temp.returnLoaderManager());
        }
    }

    @Override
    public void setUseTodayLayout(boolean useTodayLayout) {
        mModel.setUseTodayLayout(useTodayLayout);
    }

    @Override
    public ForecastAdapter setUpForecastAdapter(boolean useTodayLayout) {
        RequiredView.ForecastViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            return mModel.setUpForecastAdapter(temp, useTodayLayout);
        }
        return null;
    }

    @Override
    public void openPreferredLocationInMap() {
        RequiredView.ForecastViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            mModel.openPreferredLocationInMap(temp);
        }
    }


    @Override
    public void onConfigurationChanged(RequiredView.RequiredViewOps view) {

    }

    @Override
    public void onDestroy(boolean isChangingConfig) {

    }

    @Override
    public void initLoader(Context context) {
        RequiredView.ForecastViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            mModel.initLoader(temp.returnLoaderManager(), context);
        }
    }


    public void setRetryLayoutVisibility(int gone) {
        RequiredView.ForecastViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            temp.setRetryLayoutVisibility(gone);
        }
    }

    public void scrollToPosition() {
        RequiredView.ForecastViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            temp.scrollToPosition();
        }
    }
}
