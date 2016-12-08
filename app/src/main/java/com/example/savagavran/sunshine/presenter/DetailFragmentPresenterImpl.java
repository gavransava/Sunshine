package com.example.savagavran.sunshine.presenter;

import android.content.Context;
import android.net.Uri;

import com.example.savagavran.sunshine.DetailData;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

public class DetailFragmentPresenterImpl
        implements  Presenter.DetailPresenter {

    private WeakReference<RequiredView.DetailViewOps> mView;
    private Model.DetailModelOps mModel;

    public DetailFragmentPresenterImpl(RequiredView.DetailViewOps view, Model.DetailModelOps model) {
        mView = new WeakReference<>(view);
        mModel = model;
        mModel.setPresenter(this);
    }

    @Override
    public void initLoader(Context context, Uri uri) {
        RequiredView.DetailViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            mModel.initLoader(temp.returnLoaderManager(), context, uri);
        }
    }

    @Override
    public void onLocationChanged() {
        mModel.onLocationChanged();
    }

    public void populateDetailFragmentWithData(DetailData data) {
        RequiredView.DetailViewOps temp;

        if (mView.get() != null) {
            temp = mView.get();
            temp.populateDetailFragmentWithData(data);
        }
    }
}
