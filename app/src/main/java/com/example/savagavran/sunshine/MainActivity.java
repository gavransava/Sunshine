package com.example.savagavran.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.savagavran.sunshine.component.DaggerMainActivityComponent;
import com.example.savagavran.sunshine.module.MainActivityModule;
import com.example.savagavran.sunshine.presenter.Presenter;
import com.example.savagavran.sunshine.sync.SunshineSyncAdapter;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity
        implements ForecastFragment.Callback, RequiredView.RequiredViewOps {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final int REQUEST_SETTING = 0;
    private String mLocation;
    private boolean mTwoPane;
    private boolean mUnitChanged;

    @Inject
    public Presenter.MainPresenter.PresenterOps mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        mUnitChanged = Utility.isMetric(this);
        setContentView(R.layout.activity_main);

        DaggerMainActivityComponent
                .builder()
                .activityComponent(SunshineApp.getApp(this).getComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);


        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment forecastFragment =  ((ForecastFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);

        SunshineSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_SETTING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMainPresenter.hasLocationChanged(this)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != ff & !mTwoPane) {
                ff.onLocationChanged();
            }
            if ( null != df ) {
                df.onLocationChanged();
            }
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        if(requestCode == REQUEST_SETTING) {
            if(mUnitChanged != data.getBooleanExtra(SettingsActivity.UNIT_RESULT, true)){
                mUnitChanged = data.getBooleanExtra(SettingsActivity.UNIT_RESULT, true);
                ForecastFragment forecastFragment =  ((ForecastFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_forecast));
                forecastFragment.onUnitChanged();
            }
        }
    }

    public void setToast() {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }
}