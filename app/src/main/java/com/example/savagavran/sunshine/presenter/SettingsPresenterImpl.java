package com.example.savagavran.sunshine.presenter;

import android.content.Context;

import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.SettingsActivity;
import com.example.savagavran.sunshine.data.Model;

import java.lang.ref.WeakReference;

public class SettingsPresenterImpl
        implements Presenter.SettingsPresenter {

    private WeakReference<RequiredView.SettingsViewOps> mView;
    private Model.ModelOps mModel;

    @Override
    public void onUnitChanged(SettingsActivity settingsActivity) {
        mModel.onLocationOrUnitChanged(settingsActivity, null);
    }

    @Override
    public void returnLocationValue(String value) {
        RequiredView.SettingsViewOps temp = mView.get();
        if(temp != null)
            temp.updateLocationSettingValue(value);
    }

    @Override
    public void returnNotificationsValue(boolean value) {
        RequiredView.SettingsViewOps temp = mView.get();
        if(temp != null)
            temp.updateNotifitacionSettingValue(value);

    }

    @Override
    public void returnUnitValue(int value) {
        RequiredView.SettingsViewOps temp = mView.get();
        if(temp != null)
            temp.updateUnitSettingValue(value);
    }

    @Override
    public void getLocationValue(Presenter.SettingsPresenter  presenter) {
        String result = mModel.getLocationValue();
        presenter.returnLocationValue(result);
    }

    @Override
    public void getUnitValue(Presenter.SettingsPresenter  presenter) {
        int result = mModel.getUnitValue();
        presenter.returnUnitValue(result); // wont focus element
    }

    @Override
    public void getNotificationsValue(Presenter.SettingsPresenter  presenter) {
        boolean result = mModel.getNotificationsValue();
        presenter.returnNotificationsValue(result);
    }

    @Override
    public void onConfigurationChanged(RequiredView.RequiredViewOps view) {

    }

    @Override
    public void onDestroy(boolean isChangingConfig) {

    }

    @Override
    public void onLocationChanged(Context context, String location) {
        mModel.onLocationChanged(context, location);
    }

    @Override
    public void onNotificationsChange(Context context, boolean notificationEnabled) {
        mModel.onNotificationChanged(context, notificationEnabled);
    }

    @Override
    public void onUnitChanged(Context context, int unit) {
        mModel.onUnitChanged(context, unit);
    }

    public SettingsPresenterImpl(RequiredView.SettingsViewOps view, Model.ModelOps model) {
        mView = new WeakReference<>(view);
        mModel = model;
    }

}
