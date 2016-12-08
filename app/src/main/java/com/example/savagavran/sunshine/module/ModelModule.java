package com.example.savagavran.sunshine.module;

import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.model.DetailModel;
import com.example.savagavran.sunshine.model.MainModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    @Singleton
    public Model.ModelOps provideModel(){
        return new MainModel();
    }

    @Provides
    @Singleton
    public Model.DetailModelOps provideDetailModel() {
        return new DetailModel();
    }
}
