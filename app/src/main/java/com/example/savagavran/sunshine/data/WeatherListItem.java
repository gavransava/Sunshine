package com.example.savagavran.sunshine.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherListItem {

    @SerializedName("dt")
    private String dateTime;
    @SerializedName("temp")
    private Temperature temperature;
    @SerializedName("pressure")
    private String pressure;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("weather")
    private List<Weather> weather = new ArrayList<Weather>();
    @SerializedName("speed")
    private String speed;
    @SerializedName("deg")
    private String direction;
    @SerializedName("clouds")
    private String clouds;

    public WeatherListItem(String dateTime, Temperature temperature, String pressure,
                           String humidity, List<Weather> weather, String speed, String direction,
                           String clouds) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weather = weather;
        this.speed = speed;
        this.direction = direction;
        this.clouds = clouds;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
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

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
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

    public class Temperature {

        @SerializedName("day")
        private String day;
        @SerializedName("min")
        private String min;
        @SerializedName("max")
        private String max;
        @SerializedName("night")
        private String night;
        @SerializedName("eve")
        private String eve;
        @SerializedName("morn")
        private String morn;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getNight() {
            return night;
        }

        public void setNight(String night) {
            this.night = night;
        }

        public String getEve() {
            return eve;
        }

        public void setEve(String eve) {
            this.eve = eve;
        }

        public String getMorn() {
            return morn;
        }

        public void setMorn(String morn) {
            this.morn = morn;
        }
    }

    public class Weather {

        @SerializedName("id")
        private String id;
        @SerializedName("main")
        private String main;
        @SerializedName("description")
        private String description;
        @SerializedName("icon")
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}


