package com.example.savagavran.sunshine.data;

import android.text.format.Time;

import com.example.savagavran.sunshine.model.MainModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RealmDB {

    private static final String TAG = RealmDB.class.getSimpleName();
    private Realm mRealm;
    private MainModel mMainModel;
    private RealmResults<WeatherRealmObject> mData;

    public RealmDB(final MainModel mainModel) {
        mRealm = Realm.getDefaultInstance();
        mMainModel = mainModel;


        mData = mRealm.where(WeatherRealmObject.class)
                .findAll();

        mData.addChangeListener(new RealmChangeListener<RealmResults<WeatherRealmObject>>() {
            @Override
            public void onChange(RealmResults<WeatherRealmObject> element) {
                mMainModel.dataBaseChanged(element);
            }
        });
    }

    public void bulkInsert(List<WeatherListItem> weather, Time dayTime, int julianStartDay, String locationId) {

        for(int i = 0; i < weather.size(); i++) {
            long dateTime = dayTime.setJulianDay(julianStartDay + i);
            WeatherListItem item = weather.get(i);
            String locationDateKey = dateTime + locationId + "";
            final WeatherRealmObject obj = new WeatherRealmObject();
            obj.setLocationDateKey(locationDateKey);
            obj.setLocationId(locationId);
            obj.setDateTime(dateTime+"");
            obj.setPressure(item.getPressure());
            obj.setHumidity(item.getHumidity());
            obj.setSpeed(item.getSpeed());
            obj.setDirection(item.getDirection());
            obj.setClouds(item.getClouds());
            obj.setId(item.getWeather().get(0).getId());
            obj.setMain(item.getWeather().get(0).getMain());
            obj.setDescription(item.getWeather().get(0).getDescription());
            obj.setIcon(item.getWeather().get(0).getIcon());
            obj.setDay(item.getTemperature().getDay());
            obj.setMin(item.getTemperature().getMin());
            obj.setMax(item.getTemperature().getMax());
            obj.setNight(item.getTemperature().getNight());
            obj.setEve(item.getTemperature().getEve());
            obj.setMorn(item.getTemperature().getMorn());

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(obj);
                }
            });
        }
    }

    public ArrayList<WeatherRealmObject> getAdapterData() {
        ArrayList<WeatherRealmObject>arrayList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++){
            arrayList.add(mData.get(i));
        }
        if(!mData.isEmpty())
            mMainModel.removeRetryLayout();
        else {
            mMainModel.revealRetryLayout();
        }

        return arrayList;
    }

    public WeatherRealmObject getDetailData(int position, String location) {
        return mRealm.where(WeatherRealmObject.class)
                .equalTo("locationId", location)
                .findAll().get(position);
    }
}
