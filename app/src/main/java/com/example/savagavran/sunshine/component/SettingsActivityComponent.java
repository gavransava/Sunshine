package com.example.savagavran.sunshine.component;

import com.example.savagavran.sunshine.ActivityComponent;
import com.example.savagavran.sunshine.SettingsActivity;
import com.example.savagavran.sunshine.module.SettingsActivityModule;

import dagger.Component;

@SettingsScope
@Component(dependencies = ActivityComponent.class, modules = SettingsActivityModule.class)
public interface SettingsActivityComponent {
    void inject(SettingsActivity settingsActivity);
}