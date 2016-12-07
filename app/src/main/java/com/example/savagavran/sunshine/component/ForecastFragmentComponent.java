package com.example.savagavran.sunshine.component;

import com.example.savagavran.sunshine.ActivityComponent;
import com.example.savagavran.sunshine.ForecastFragment;
import com.example.savagavran.sunshine.module.ForecastFragmentModule;

import dagger.Component;

@ForecastScope
@Component(dependencies = ActivityComponent.class, modules = ForecastFragmentModule.class)
public interface ForecastFragmentComponent {
    void inject(ForecastFragment forecastFragment);
}