package com.example.savagavran.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savagavran.sunshine.component.DaggerDetailFragmentComponent;
import com.example.savagavran.sunshine.module.DetailFragmentModule;
import com.example.savagavran.sunshine.presenter.Presenter;

import javax.inject.Inject;

public class DetailFragment extends Fragment
        implements RequiredView.DetailViewOps {

    @Override
    public LoaderManager returnLoaderManager() {
        return getLoaderManager();
    }

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_POSITION = "POSITION";
    public static final String DETAIL_LOCATION = "LOCATION";

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    private int mPosition;
    private String mLocation;

    @Inject
    Presenter.ForecastPresenter mForecastPresenter;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPosition = arguments.getInt(DETAIL_POSITION);
            mLocation = arguments.getString(DETAIL_LOCATION);
        }

        DaggerDetailFragmentComponent
                .builder()
                .activityComponent(SunshineApp.getApp(getActivity()).getComponent())
                .detailFragmentModule(new DetailFragmentModule(this))
                .build()
                .inject(this);


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);

        mForecastPresenter.getDetailData(mPosition, mLocation);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void populateDetailFragmentWithData(DetailData data) {

        mIconView.setImageResource(data.getWeatherId());
        mFriendlyDateView.setText(data.getFriendlyDateText());
        mDateView.setText(data.getDateText());
        mDescriptionView.setText(data.getDescription());
        mIconView.setContentDescription(data.getDescription());
        mHighTempView.setText(data.getMaxTemp());
        mLowTempView.setText(data.getLowTemp());
        mHumidityView.setText(data.getHumidity());
        mPressureView.setText(data.getPressure());
        mWindView.setText(data.getWindInfo());

        mForecast = String.format("%s - %s - %s/%s",
                data.getDateText(),
                data.getDescription(),
                data.getMaxTemp(),
                data.getLowTemp());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }
}

