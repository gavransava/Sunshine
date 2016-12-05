package com.example.savagavran.sunshine.model;

import android.content.Context;

import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.Model;

public class MainModel
        implements Model.ModelOps {

    private boolean dummy;
    private String mLocation;

    @Override
    public boolean hasLocationChanged(Context context) {

        String location = Utility.getPreferredLocation(context);
        if (location != null && !location.equals(mLocation)) {
            mLocation = location;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void synchronise(Context context) {

    }

    public MainModel() {
        dummy = true;
    }
}