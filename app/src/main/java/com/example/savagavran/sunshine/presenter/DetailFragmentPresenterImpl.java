package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.example.savagavran.sunshine.DetailData;
import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

public class DetailFragmentPresenterImpl
        implements  Presenter.ForecastPresenter {

    private WeakReference<RequiredView.DetailViewOps> mView;
    private Model.ModelOps mModel;

    public DetailFragmentPresenterImpl(RequiredView.DetailViewOps view, Model.ModelOps model) {
        mView = new WeakReference<>(view);
        mModel = model;
        mModel.setDetailPresenter(this);
    }

    @Override
    public void getDetailData(int position, String location) {
        mModel.getDetailData(position, location);
    }

    public void populateDetailFragmentWithData(DetailData obj) {
        RequiredView.DetailViewOps temp;
        if (mView.get() != null) {
            temp = mView.get();
            temp.populateDetailFragmentWithData(obj);
        }
    }

    @Override
    public void onConfigurationChanged(RequiredView.RequiredViewOps view) {

    }

    @Override
    public void onDestroy(boolean isChangingConfig) {

    }

    @Override
    public ForecastAdapter setUpForecastAdapter(boolean useTodayLayout) {
        return null;
    }

    @Override
    public void setUseTodayLayout(boolean useTodayLayout) {

    }

    @Override
    public void openPreferredLocationInMap() {

    }

    @Override
    public void onLocationOrUnitChanged(Context context) {

    }

    @Override
    public String getPreferredLocation(FragmentActivity activity) {
        return null;
    }
}
