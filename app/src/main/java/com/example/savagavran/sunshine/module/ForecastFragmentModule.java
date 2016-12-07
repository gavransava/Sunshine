package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenterImpl;
import com.example.savagavran.sunshine.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ForecastFragmentModule {

    private RequiredView.ForecastViewOps forecastFragment;

    public ForecastFragmentModule(RequiredView.ForecastViewOps fragment) {
        forecastFragment = fragment;
    }

    @Provides
    Presenter.ForecastPresenter providedPresenterOps(Model.ModelOps model) {
        return new ForecastFragmentPresenterImpl(forecastFragment, model);
    }
}
