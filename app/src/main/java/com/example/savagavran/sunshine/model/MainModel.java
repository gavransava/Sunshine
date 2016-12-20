package com.example.savagavran.sunshine.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.View;

import com.example.savagavran.sunshine.DetailData;
import com.example.savagavran.sunshine.ForecastAdapter;
import com.example.savagavran.sunshine.R;
import com.example.savagavran.sunshine.RequiredView;
import com.example.savagavran.sunshine.Utility;
import com.example.savagavran.sunshine.data.Model;
import com.example.savagavran.sunshine.data.RealmDB;
import com.example.savagavran.sunshine.data.WeatherListItem;
import com.example.savagavran.sunshine.data.WeatherRealmObject;
import com.example.savagavran.sunshine.presenter.DetailFragmentPresenterImpl;
import com.example.savagavran.sunshine.presenter.ForecastFragmentPresenterImpl;
import com.example.savagavran.sunshine.sync.SunshineSyncAdapter;

import java.util.List;

import io.realm.RealmResults;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainModel
        implements Model.ModelOps {

    private ForecastFragmentPresenterImpl mForecastFragmentPresenter;
    private DetailFragmentPresenterImpl mDetailFragmentPresenter;
    private RealmDB mRealm;
    private boolean mLocationChanged;
    private ForecastAdapter mForecastAdapter;

    @Override
    public ForecastAdapter setUpForecastAdapter(RequiredView.ForecastViewOps context, boolean useTodayLayout) {
        mRealm = new RealmDB(this);

        mForecastAdapter = new ForecastAdapter(context.returnParentActivity(), mRealm.getAdapterData());
        mForecastAdapter.setUseTodayLayout(useTodayLayout);

        return mForecastAdapter;
    }

    @Override
    public void getDetailData(int position, String location) {
        WeatherRealmObject obj = mRealm.getDetailData(position, location);
        DetailData data = new DetailData();

        data.setWeatherId(Utility.getArtResourceForWeatherCondition(Integer.parseInt(obj.getId())));
        data.setFriendlyDateText(Utility.getFriendlyDayString(mForecastAdapter.getContext() ,Long.parseLong(obj.getDateTime())));
        data.setDescription(obj.getDescription());
        data.setMaxTemp(obj.getMax());
        data.setLowTemp(obj.getMin());
        data.setHumidity(obj.getHumidity());
        data.setPressure(obj.getPressure());
        data.setWindInfo(Utility.getFormattedWind(mForecastAdapter.getContext(),
                Float.parseFloat(obj.getSpeed()), Float.parseFloat(obj.getDirection())));

        mDetailFragmentPresenter.populateDetailFragmentWithData(data);
    }

    @Override
    public void setUseTodayLayout(boolean useTodayLayout) {
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(useTodayLayout);
        }
    }

    @Override
    public void openPreferredLocationInMap(RequiredView.ForecastViewOps temp) {
//        if (null != mForecastAdapter) {
//            Cursor c = mForecastAdapter.getCursor();
//            if (null != c) {
//                c.moveToPosition(0);
//                String posLat = c.getString(7);
//                String posLong = c.getString(8);
//                Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(geoLocation);
//
//                if (intent.resolveActivity(temp.returnParentActivity().getPackageManager()) != null) {
//                    temp.returnParentActivity().startActivity(intent);
//                } else {
//                    Log.d(LOG_TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
//                }
//            }
//        }
    }

    @Override
    public void onLocationOrUnitChanged(Context context) {
        SunshineSyncAdapter.syncImmediately(context);
    }

    private String mLocationValue;
    private boolean mNotificationValue;
    private int mUnitValue;

    @Override
    public String getLocationValue(Context context) {
        mLocationValue = Utility.getPreferredLocation(context);
        return mLocationValue;
    }

    @Override
    public int getUnitValue(Context context) {
        mUnitValue = Utility.isMetric(context) ? 0 : 1;
        return mUnitValue;
    }

    @Override
    public boolean getNotificationsValue(Context context) {
        mNotificationValue = Utility.isNotificationEnabled(context);
        return mNotificationValue;
    }

    @Override
    public void onNotificationChanged(Context context, boolean notificationEnabled) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(context.getString(R.string.pref_enable_notifications_key), notificationEnabled);
        mNotificationValue = notificationEnabled;
        editor.apply();
    }

    @Override
    public void onUnitChanged(Context context, int position) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putInt(context.getString(R.string.pref_units_key), position);
        mUnitValue = position;
        editor.apply();
    }

    @Override
    public void onLocationChanged(Context context, String location) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        if(mLocationValue.equals(location)){
            mLocationChanged = true;
            mLocationValue = location;
        }
        else {
            mLocationChanged = false;
        }
        editor.putString(context.getString(R.string.pref_location_key), location);

        editor.apply();
    }

    @Override
    public boolean hasLocationChanged(Context context) {
        mForecastFragmentPresenter.refreshListView();
        if(mLocationChanged){
            mLocationChanged = false;
            return true;
        } else {
            return false;
        }
    }

    public void removeRetryLayout(){
        mForecastFragmentPresenter.setRetryLayoutVisibility(View.GONE);
    }

    @Override
    public String getPreferredLocation(FragmentActivity activity) {
        return Utility.getPreferredLocation(activity);
    }

    @Override
    public void bulkInsert(List<WeatherListItem> weather, Time dayTime, int julianStartDay, String locationId) {
        mRealm.bulkInsert(weather, dayTime, julianStartDay, locationId);
    }

    @Override
    public void setDetailPresenter(DetailFragmentPresenterImpl detailFragmentPresenter) {
        mDetailFragmentPresenter = detailFragmentPresenter;
    }

    @Override
    public void setForecastPresenter(ForecastFragmentPresenterImpl forecastFragmentPresenter) {
        mForecastFragmentPresenter = forecastFragmentPresenter;
    }

    public MainModel() {
    }

    public void dataBaseChanged(RealmResults<WeatherRealmObject> element) {
        mForecastAdapter.notifyDataSetChanged();
    }

    public void revealRetryLayout() {
        mForecastFragmentPresenter.setRetryLayoutVisibility(View.VISIBLE);
    }
}