package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.presenter.Presenter;
import com.example.savagavran.sunshine.presenter.SettingsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsActivityModule {

    private RequiredView.SettingsViewOps mActivity;

    public SettingsActivityModule(RequiredView.SettingsViewOps activity) {
        mActivity = activity;
    }

    @Provides
    Presenter.SettingsPresenter providedPresenterOps(Model.ModelOps model) {
        return new SettingsPresenterImpl(mActivity, model);
    }
}
