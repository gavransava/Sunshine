package com.example.savagavran.sunshine;

import android.content.Context;

import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.module.ActivityModule;
import com.example.savagavran.sunshine.module.ModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, ModelModule.class})
public interface ActivityComponent {
    Context getContext();

    Model.ModelOps getModel();
    Model.DetailModelOps getDetailModel();

}
