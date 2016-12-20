package com.example.savagavran.sunshine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.savagavran.sunshine.component.DaggerForecastComponent;
import com.example.savagavran.sunshine.module.ForecastFragmentModule;
import com.example.savagavran.sunshine.presenter.Presenter;
import com.example.savagavran.sunshine.sync.SunshineSyncAdapter;

import javax.inject.Inject;

public class ForecastFragment extends Fragment
        implements RequiredView.ForecastViewOps {

    public static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    @Override
    public void setRetryLayoutVisibility(int visibility) {
        if(mRetryLayout!=null)
            mRetryLayout.setVisibility(visibility);
    }

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private ImageView mRetryButton;
    private LinearLayout mRetryLayout;
    private static final String SELECTED_KEY = "selected_position";
    private static final String SUNSHINE_SERVICE = "sunshine_service";

    @Inject
    public Presenter.ForecastPresenter  mForecastPresenter;


    // When called from activity just return this instead of getActivity.
    @Override
    public Activity returnParentActivity() {
        return getActivity();
    }

    /**
     +     * A callback interface that all activities containing this fragment must
     +     * implement. This mechanism allows activities to be notified of item
     +     * selections.
     +     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int position, String locationSetting);
    }


    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DaggerForecastComponent
                .builder()
                .activityComponent(SunshineApp.getApp(getActivity()).getComponent())
                .forecastFragmentModule(new ForecastFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        mForecastPresenter.setUseTodayLayout(mUseTodayLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastPresenter.setUpForecastAdapter(mUseTodayLayout));

        mRetryLayout = (LinearLayout) rootView.findViewById(R.id.retry_layout);
        setRetryLayoutVisibility(View.GONE);

        mRetryButton = (ImageView) rootView.findViewById(R.id.button_retry);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(2000);
                mRetryButton.startAnimation(rotateAnimation);
                SunshineSyncAdapter.syncImmediately(getActivity());
            }
        });

        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    String locationSetting = mForecastPresenter.getPreferredLocation(getActivity());
                    ((Callback) getActivity()).onItemSelected(position, locationSetting);

                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void onLocationOrUnitChanged(Context context) {
        mForecastPresenter.onLocationOrUnitChanged(context);
    }

    @Override
    public void scrollToPosition() {
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_refresh) {
        //    updateWeather();
        //    return true;
        //}

        if (id == R.id.action_map) {
            mForecastPresenter.openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshListView() {
        ((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
    }
}