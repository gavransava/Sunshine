package com.example.savagavran.sunshine;

import android.app.Activity;
import android.support.v4.app.LoaderManager;

// Presenter/s -> View/s
public interface RequiredView {

    interface RequiredViewOps {

    }

    interface ForecastViewOps {
        Activity returnParentActivity();
        void setRetryLayoutVisibility(int visibility);
        void scrollToPosition();
        public void refreshListView();
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
