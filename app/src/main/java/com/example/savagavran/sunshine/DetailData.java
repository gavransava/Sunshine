package com.example.savagavran.sunshine;

public class DetailData {
    private int WeatherId;
    private String friendlyDateText;
    private String dateText;
    private String description;
    private String maxTemp;
    private String lowTemp;
    private String humidity;

    public String getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(String windInfo) {
        this.windInfo = windInfo;
    }

    private String windInfo;

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    public String getWindDirStr() {
        return windDirStr;
    }

    public void setWindDirStr(String windDirStr) {
        this.windDirStr = windDirStr;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    private String windDirStr;
    private String pressure;


    public int getWeatherId() {
        return WeatherId;
    }

    public void setWeatherId(int weatherId) {
        WeatherId = weatherId;
    }

    public String getFriendlyDateText() {
        return friendlyDateText;
    }

    public void setFriendlyDateText(String friendlyDateText) {
        this.friendlyDateText = friendlyDateText;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
