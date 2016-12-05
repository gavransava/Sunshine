package com.example.savagavran.sunshine.presenter;

import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

public class ForecastFragmentPresenter
implements Presenter.ForecastPresenter {

    private WeakReference<Presenter.ForecastPresenter> mView;
    private Model.ModelOps mModel;

    public ForecastFragmentPresenter(Presenter.ForecastPresenter view) {
        mView = new WeakReference<>(view);
    }
}
