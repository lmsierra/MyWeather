package lmsierra.com.myweather.model;

import com.google.gson.Gson;

public class WeatherInfo {
    private String clouds;
    private String weatherCondition;
    private String Observation;
    private int windDirection;
    private String ICAO;
    private String cloudsCode;
    private double lng;
    private String temperature;
    private int windSpeed;
    private double dewPoint;
    private String stationName;
    private String datetime;
    private double lat;

    public WeatherInfo(String clouds, String weatherCondition, String observation, int windDirection, String ICAO, String cloudsCode, double lng, String temperature, int windSpeed, double dewPoint, String stationName, String datetime, double lat) {
        this.clouds = clouds;
        this.weatherCondition = weatherCondition;
        Observation = observation;
        this.windDirection = windDirection;
        this.ICAO = ICAO;
        this.cloudsCode = cloudsCode;
        this.lng = lng;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.dewPoint = dewPoint;
        this.stationName = stationName;
        this.datetime = datetime;
        this.lat = lat;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public String getICAO() {
        return ICAO;
    }

    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }

    public String getCloudsCode() {
        return cloudsCode;
    }

    public void setCloudsCode(String cloudsCode) {
        this.cloudsCode = cloudsCode;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
