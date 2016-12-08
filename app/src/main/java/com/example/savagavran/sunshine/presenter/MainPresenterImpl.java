package com.example.savagavran.sunshine.presenter;

import android.content.Context;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

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
}
