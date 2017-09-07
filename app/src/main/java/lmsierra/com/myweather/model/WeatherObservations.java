package lmsierra.com.myweather.model;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherObservations {

    public static double BELOW_ABSOLUTE_ZERO = -274;

    private List<WeatherInfo> weatherObservations;

    public WeatherObservations(List<WeatherInfo> weatherObservations) {
        this.weatherObservations = weatherObservations;
    }

    public List<WeatherInfo> getWeatherObservations() {
        return weatherObservations;
    }

    public void setWeatherObservations(List<WeatherInfo> weatherObservations) {
        this.weatherObservations = weatherObservations;
    }

    public double getAverageTemperature(){

        int temp = 0;
        int count = weatherObservations.size();

        for(WeatherInfo info : weatherObservations){
            if(info.getTemperature().length() > 0){
                temp += Double.valueOf(info.getTemperature());
            }else {
                count --;
            }
        }

        if(count > 0){

            return temp / count;

        }else{
            return BELOW_ABSOLUTE_ZERO;
        }
    }

    public String getUpdatedDate(){

        if(getWeatherObservations().size() > 0) {
            String date = getWeatherObservations().get(0).getDatetime();

            try {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
                Date d = format.parse(date);
                format = new SimpleDateFormat("dd/MM HH:mm", new Locale("es", "Es"));

                return format.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
