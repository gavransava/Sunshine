package com.example.savagavran.sunshine.presenter;

import android.content.Context;

import com.example.savagavran.sunshine.RequiredView;

public interface Presenter {

    interface MainPresenter {

        // View -> Presenter
        interface PresenterOps {

            void onConfigurationChanged(RequiredView.RequiredViewOps view);
            void onDestroy(boolean isChangingConfig);
            boolean hasLocationChanged(Context context);
        }

        // Model -> Presenter
        interface RequiredPresenterOps {

        }
    }

    interface ForecastPresenter {

    }
}
