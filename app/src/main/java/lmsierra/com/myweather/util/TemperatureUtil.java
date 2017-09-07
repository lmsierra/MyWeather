package lmsierra.com.myweather.util;

public class TemperatureUtil {

    public static final int MIN_TEMPERATURE = -20;
    public static final int MAX_TEMPERATURE = 50;

    public static int getTemperaturePercentage(double temperature){
        return (int)(((temperature + Math.abs(MIN_TEMPERATURE)) / (Math.abs(MIN_TEMPERATURE) + Math.abs(MAX_TEMPERATURE))) * 100);
    }
}
