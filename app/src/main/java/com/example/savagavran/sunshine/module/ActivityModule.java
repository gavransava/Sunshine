package com.example.savagavran.sunshine.module;

import android.content.Context;

import com.example.savagavran.sunshine.SunshineApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private SunshineApp app;

    public ActivityModule(SunshineApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return app;
    }
}
