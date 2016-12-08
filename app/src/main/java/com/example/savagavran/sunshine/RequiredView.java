package com.example.savagavran.sunshine;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.LoaderManager;

// Presenter/s -> View/s
public interface RequiredView {

    interface RequiredViewOps {

    }

    interface ForecastViewOps {
        Context returnContext();
        Activity returnParentActivity();

        LoaderManager returnLoaderManager();
        void setRetryLayoutVisibility(int visibility);
        void scrollToPosition();
    }

    interface DetailViewOps {
        LoaderManager returnLoaderManager();
        void populateDetailFragmentWithData(DetailData data);
    }

    interface SettingsViewOps {
        void updateLocationSettingValue(String value);
        void updateUnitSettingValue(int value);
        void updateNotifitacionSettingValue(boolean value);

    }
}
