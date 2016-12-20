package com.example.savagavran.sunshine;

import android.app.Application;
import android.content.Context;

import com.example.savagavran.sunshine.module.ActivityModule;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SunshineApp extends Application {

    private ActivityComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();

        Realm.setDefaultConfiguration(config);
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                RealmInspectorModulesProvider.builder(this).build()
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
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
