package com.example.savagavran.sunshine.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WeatherRealmObject extends RealmObject {

    @PrimaryKey
    private String locationDateKey;

    private String locationId;
    private String dateTime;
    private String pressure;
    private String humidity;
    private String speed;
    private String direction;
    private String clouds;

    // Weather extra info
    private String id;
    private String main;
    private String description;
    private String icon;

    // Temperature
    private String day;
    private String min;
    private String max;
    private String night;
    private String eve;
    private String morn;


    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getLocationDateKey() {
        return locationDateKey;
    }

    public void setLocationDateKey(String locationDateKey) {
        this.locationDateKey = locationDateKey;
    }

    public  String getDay() {
        return day;
    }public void   setDay(String day) {
        this.day = day;
    }public String getId() {
        return id;
    }public void   setId(String id) {
        this.id = id;
    }public String getMain() {
        return main;
    }public void   setMain(String main) {
        this.main = main;
    }public String getDescription() {
        return description;
    }public void   setDescription(String description) {
        this.description = description;
    }public String getIcon() {
        return icon;
    }public void   setIcon(String icon) {
        this.icon = icon;
    }public String getMin() {
        return min;
    }public void   setMin(String min) {
        this.min = min;
    }public String getMax() {
        return max;
    }public void   setMax(String max) {
        this.max = max;
    }public String getNight() {
        return night;
    }public void   setNight(String night) {
        this.night = night;
    }public String getEve() {
        return eve;
    }public void   setEve(String eve) {
        this.eve = eve;
    }public String getMorn() {
        return morn;
    }public void   setMorn(String morn) {
        this.morn = morn;
    }
}
