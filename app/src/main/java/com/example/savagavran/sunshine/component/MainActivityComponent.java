package com.example.savagavran.sunshine.component;

import com.example.savagavran.sunshine.ActivityComponent;
import com.example.savagavran.sunshine.MainActivity;
import com.example.savagavran.sunshine.module.MainActivityModule;

import dagger.Component;

@ActivityScope
@Component(dependencies = ActivityComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}