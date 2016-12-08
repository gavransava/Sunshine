package com.example.savagavran.sunshine.component;

import com.example.savagavran.sunshine.ActivityComponent;
import com.example.savagavran.sunshine.DetailFragment;
import com.example.savagavran.sunshine.module.DetailFragmentModule;

import dagger.Component;

@DetailScope
@Component(dependencies = ActivityComponent.class, modules = DetailFragmentModule.class)
public interface DetailFragmentComponent {
    void inject(DetailFragment detailFragment);
}