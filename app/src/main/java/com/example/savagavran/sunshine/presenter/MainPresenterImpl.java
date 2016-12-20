package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.text.format.Time;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.data.WeatherListItem;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainPresenterImpl
        implements Presenter.MainPresenter {

    private WeakReference<RequiredView.RequiredViewOps> mView;
    private Model.ModelOps mModel;

    public MainPresenterImpl(RequiredView.RequiredViewOps view, Model.ModelOps model) {
        mView = new WeakReference<>(view);
        mModel = model;
    }

    @Override
    public void onConfigurationChanged(RequiredView.RequiredViewOps view) {

    }

    @Override
    public void onDestroy(boolean isChangingConfig) {

    }

    public void setModel( Model.ModelOps model) {
        mModel = model;
    }

    @Override
    public boolean hasLocationChanged(Context context) {
        return mModel.hasLocationChanged(context);
    }

    @Override
    public void bulkInsert(List<WeatherListItem> weather, Time dayTime, int julianStartDay, String locationId) {
        mModel.bulkInsert(weather, dayTime, julianStartDay, locationId);
    }
}
