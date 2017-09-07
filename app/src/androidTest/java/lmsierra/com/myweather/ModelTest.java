package lmsierra.com.myweather;

import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.util.List;

import lmsierra.com.myweather.model.City;
import lmsierra.com.myweather.model.Georeference;
import lmsierra.com.myweather.model.WeatherInfo;
import lmsierra.com.myweather.model.WeatherObservations;
import lmsierra.com.myweather.util.FileReaderUtil;

public class ModelTest extends AndroidTestCase{

    private static final String TEST_CITY_FILE = "CityTestJson.json";
    private static final String TEST_GEOREFENRENCE_FILE = "GeoreferenceTestJson.json";
    private static final String TEST_WEATHERINFO_FILE = "WeatherInfoTestJson.json";
    private static final String TEST_WEATHERINFO_NO_TEMPERATURE_FILE = "WeatherInfoNoTemperatureTestJson.json";
    private static final String TEST_WEATHEROBSERVATIONS_FILE = "WeatherObservationsTestJson.json";
    private static final String TEST_WEATHEROBSERVATIONS_NO_TEMPERATURE_FILE = "WeatherObservationsNoTemperatureTestJson.json";

    public void testCanCreateCityFromGson(){
        City city = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_CITY_FILE, getContext()), City.class);
        String suv = city.getName();
        assertNotNull(suv);
    }

    public void testCanCreateGeoreferenceFromGson(){
        Georeference georeference = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_GEOREFENRENCE_FILE, getContext()), Georeference.class);
        List<City> suv = georeference.getGeonames();
        assertNotNull(suv);
    }

    public void testCanCreateWeatherInfoFromGson(){
        WeatherInfo weatherInfo = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_WEATHERINFO_FILE, getContext()), WeatherInfo.class);
        String suv = weatherInfo.getTemperature();
        assertNotNull(suv);
    }

    public void testCanCreateWeatherInfoFromGsonWithNoTemperature(){
        WeatherInfo weatherInfo = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_WEATHERINFO_NO_TEMPERATURE_FILE, getContext()), WeatherInfo.class);
        String suv = weatherInfo.getTemperature();
        assertNotNull(suv);
    }

    public void testCanCreateWeatherObservationsFromGson(){
        WeatherObservations weatherObservations = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_WEATHEROBSERVATIONS_FILE, getContext()), WeatherObservations.class);
        List<WeatherInfo> suv = weatherObservations.getWeatherObservations();
        assertNotNull(suv);
    }

    public void testGetAverageTemperature(){
        WeatherObservations weatherObservations = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_WEATHEROBSERVATIONS_FILE, getContext()), WeatherObservations.class);
        double suv = weatherObservations.getAverageTemperature();
        assertTrue(suv > WeatherObservations.BELOW_ABSOLUTE_ZERO);
    }

    public void testGetAverageTemperatureWithNoTemperature(){
        WeatherObservations weatherObservations = new Gson().fromJson(FileReaderUtil.readFileFromAssets(TEST_WEATHEROBSERVATIONS_NO_TEMPERATURE_FILE, getContext()), WeatherObservations.class);
        double suv = weatherObservations.getAverageTemperature();
        assertEquals(WeatherObservations.BELOW_ABSOLUTE_ZERO, suv);
    }
}
