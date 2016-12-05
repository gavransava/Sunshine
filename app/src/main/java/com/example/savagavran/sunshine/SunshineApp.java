package com.example.savagavran.sunshine;

import android.app.Application;
import android.content.Context;

import com.example.savagavran.sunshine.module.ActivityModule;

public class SunshineApp extends Application {

    private ActivityComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        appComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent getComponent() {
        return appComponent;
    }

    public static SunshineApp getApp(Context context) {
        return (SunshineApp) context.getApplicationContext();
    }
}
