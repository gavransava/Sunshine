package com.example.savagavran.sunshine.data;

import android.content.Context;

public interface Model {
    // Presenter -> Model
    interface ModelOps {
        boolean hasLocationChanged(Context context);
        void synchronise(Context context);
    }
}
