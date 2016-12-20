package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.presenter.DetailFragmentPresenterImpl;
import com.example.savagavran.sunshine.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailFragmentModule {

    private RequiredView.DetailViewOps detailFragment;

    public DetailFragmentModule(RequiredView.DetailViewOps fragment) {
        detailFragment = fragment;
    }

    @Provides
    Presenter.ForecastPresenter providedPresenterOps(Model.ModelOps model) {
        return new DetailFragmentPresenterImpl(detailFragment, model);
    }
}
