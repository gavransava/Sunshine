package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.presenter.MainPresenterImpl;
import com.example.savagavran.sunshine.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private RequiredView.RequiredViewOps mActivity;

    public MainActivityModule(RequiredView.RequiredViewOps activity) {
        mActivity = activity;
    }

    @Provides
    Presenter.MainPresenter.PresenterOps providedPresenterOps(Model.ModelOps model) {
        return new MainPresenterImpl(mActivity, model);
    }
}
