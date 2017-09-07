package lmsierra.com.myweather.util;

import java.util.Locale;

public class UrlUtil {

    public static final String GEOREFERENCE_URL = "http://api.geonames.org/searchJSON?q=%s&maxRows=%d&startRow=%d&lang=%s&isNameRequired=%b&style=%s&username=%s";

    public static final String WEATHER_URL = "http://api.geonames.org/weatherJSON?north=%f&south=%f&east=%f&west=%f&username=%s";


    public static final int DEFAULT_MAX_ROWS = 20;
    public static final int DEFAULT_START_ROW = 0;
    public static final String DEFAULT_LANGUAGE = "en";
    public static boolean DEFAULT_NAME_REQUIRED = true;
    public static String DEFAULT_STYLE = "full";
    public static String DEFAULT_USERNAME = "ilgeonamessample";

    public static String formatUrlGeoreference(String city){
        return formatUrlGeoreference(city,
                                     DEFAULT_MAX_ROWS,
                                     DEFAULT_START_ROW,
                                     DEFAULT_LANGUAGE,
                                     DEFAULT_NAME_REQUIRED,
                                     DEFAULT_STYLE,
                                     DEFAULT_USERNAME);
    }

    public static String formatUrlGeoreference(String city,
                                               int maxRows,
                                               int startRow,
                                               String language,
                                               boolean isNameRequired,
                                               String style,
                                               String username){

        // Using Locale.US in order to avoid replacing . for ,
        return String.format(Locale.US, GEOREFERENCE_URL, city, maxRows, startRow, language, isNameRequired, style, username);
    }


    public static String formatUrlWeather(double north, double south, double east, double west){
        return formatUrlWeather(north, south, east, west, DEFAULT_USERNAME);
    }

    public static String formatUrlWeather(double north, double south, double east, double west, String username){
        return String.format(Locale.US, WEATHER_URL, north, south, east, west, username);
    }
}
