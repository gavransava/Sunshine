package com.example.savagavran.sunshine.component;


import com.example.savagavran.sunshine.ForecastFragment;
import com.example.savagavran.sunshine.module.ActivityModule;
import com.example.savagavran.sunshine.module.ForecastFragmentModule;

import dagger.Component;

@ForecastScope
@Component(dependencies = ActivityModule.class, modules = ForecastFragmentModule.class)
public interface ForecastComponent {
    void inject(ForecastFragment forecastFragment);
}

