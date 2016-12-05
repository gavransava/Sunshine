package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.ForecastFragment;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenter;
import com.example.savagavran.sunshine.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ForecastFragmentModule {
    ForecastFragment forecastFragment;

    public ForecastFragmentModule(ForecastFragment fragment) {
        forecastFragment = fragment;
    }

    @Provides
    ForecastFragment providesMainActivity() {
        return forecastFragment;
    }

    @Provides
    Presenter.ForecastPresenter providedPresenterOps() {
        ForecastFragmentPresenter presenter = new ForecastFragmentPresenter(forecastFragment);
        return presenter;
    }
}
