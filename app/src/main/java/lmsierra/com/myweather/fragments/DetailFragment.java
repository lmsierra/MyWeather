package lmsierra.com.myweather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import lmsierra.com.myweather.R;
import lmsierra.com.myweather.activities.MainActivity;
import lmsierra.com.myweather.model.City;
import lmsierra.com.myweather.model.Georeference;
import lmsierra.com.myweather.model.WeatherInfo;
import lmsierra.com.myweather.model.WeatherObservations;
import lmsierra.com.myweather.util.GsonRequest;
import lmsierra.com.myweather.util.TemperatureUtil;
import lmsierra.com.myweather.util.UrlUtil;
import lmsierra.com.myweather.util.VolleySingleton;

public class DetailFragment extends Fragment implements OnMapReadyCallback{

    @Bind(R.id.fragment_detail_layout_city_details)LinearLayout cityDetailsLayout;
    @Bind(R.id.fragment_detail_layout_no_cities_found)RelativeLayout noCitiesFoundLayout;
    @Bind(R.id.fragment_detail_textview_no_cities_found)TextView noCitiesFoundTextView;
    @Bind(R.id.fragment_detail_textview_name)TextView cityNameTextView;
    @Bind(R.id.fragment_detail_textview_date)TextView dateTextView;
    @Bind(R.id.fragment_detail_layout_temperature_container)LinearLayout temperatureContainer;
    @Bind(R.id.fragment_detail_textview_temperature)TextView temperatureTextView;
    @Bind(R.id.fragment_detail_textview_no_temperature)TextView noTemperatureTextView;
    @Bind(R.id.fragment_detail_progressbar_temperature)ProgressBar temperatureProgressBar;
    @Bind(R.id.fragment_detail_textview_min_temperature)TextView minTemperatureIndicator;
    @Bind(R.id.fragment_detail_textview_max_temperature)TextView maxTemperatureIndicator;
    @Bind(R.id.fragment_detail_scrollview_weather_detail)ScrollView weatherDetailsLayout;
    @Bind(R.id.fragment_detail_layout_error_temperature_load)RelativeLayout errorWeatherLayout;
    @Bind(R.id.fragment_detail_textview_country)TextView countryTextView;
    @Bind(R.id.fragment_detail_textview_population)TextView populationTextView;

    private static final String TAG = DetailFragment.class.toString();

    private City city;
    private final String cityName;
    private View rootView;

    private GoogleMap googleMap;

    public DetailFragment() {
        this("");
    }

    public DetailFragment(String cityName){
        this.cityName = cityName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        if(savedInstanceState == null) {

            configProgressBar();
            configMap();
            retrieveCityDetails();
        }
        return rootView;
    }

    private void configProgressBar() {
        minTemperatureIndicator.setText(String.format(getString(R.string.temperature_value), TemperatureUtil.MIN_TEMPERATURE));
        maxTemperatureIndicator.setText(String.format(getString(R.string.temperature_value), TemperatureUtil.MAX_TEMPERATURE));
        minTemperatureIndicator.setTextColor(getResources().getColor(R.color.minTemperatureColor));
        maxTemperatureIndicator.setTextColor(getResources().getColor(R.color.maxTemperatureIndicator));
    }

    private void configMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_map);
        mapFragment.getMapAsync(this);
    }

    private void retrieveCityDetails() {

        Log.d(TAG, "Sending GET request: " + UrlUtil.formatUrlGeoreference(cityName));

        GsonRequest<City> georeferenceRequest = new GsonRequest<>(Request.Method.GET, UrlUtil.formatUrlGeoreference(cityName), null, Georeference.class, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                Georeference georeferences = (Georeference) response;
                Log.d(TAG, "Response received: " + georeferences.toString());

                if(georeferences.getTotalResultsCount() > 0){

                    showCityDetailsLayout();
                    city = georeferences.getGeonames().get(0);
                    Log.d(TAG, "City georeference extracted from response: " + city.toString());

                    cityNameTextView.setText(city.getName());
                    populationTextView.setText(String.valueOf(city.getPopulation()));
                    countryTextView.setText(city.getCountryName());

                    drawPositionInMap();

                    retrieveWeatherDetails(city);
                }else{
                    showNoCityFoundLayout();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                if(networkResponse == null){
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(MainActivity.NO_CONNECTION));
                }else{
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(MainActivity.CONNECTION_ERROR));
                }

            }
        });

        startRequest(georeferenceRequest);
    }

    private void showNoCityFoundLayout() {
        noCitiesFoundTextView.setText(String.format(getString(R.string.no_cities_found), cityName));
        noCitiesFoundLayout.setVisibility(View.VISIBLE);
        cityDetailsLayout.setVisibility(View.GONE);
    }

    private void showCityDetailsLayout() {
        cityDetailsLayout.setVisibility(View.VISIBLE);
        noCitiesFoundLayout.setVisibility(View.GONE);
    }

    private void retrieveWeatherDetails(City city){

        Log.d(TAG, "Sending GET request: " + UrlUtil.formatUrlWeather(city.getNorth(), city.getSouth(), city.getEast(), city.getWest()));
        GsonRequest<WeatherObservations> weatherRequest = new GsonRequest<>(Request.Method.GET, UrlUtil.formatUrlWeather(city.getNorth(), city.getSouth(), city.getEast(), city.getWest()), null, WeatherObservations.class, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                showWeatherDetails();

                WeatherObservations weatherObservations = (WeatherObservations) response;
                Log.d(TAG, "Weather response received: " + weatherObservations.toString());

                setDate(weatherObservations);
                drawStationsInMap(weatherObservations);

                double averageTemp = weatherObservations.getAverageTemperature();
                setTemperature(averageTemp);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showWeatherError();
            }
        });

        startRequest(weatherRequest);
    }

    private void showWeatherError() {
        errorWeatherLayout.setVisibility(View.VISIBLE);
        weatherDetailsLayout.setVisibility(View.GONE);
    }

    private void showWeatherDetails(){
        weatherDetailsLayout.setVisibility(View.VISIBLE);
        errorWeatherLayout.setVisibility(View.GONE);
    }

    private void setDate(WeatherObservations weatherObservations) {
        String date = weatherObservations.getUpdatedDate();
        if(date != null){
            dateTextView.setText(String.format(getString(R.string.updated_date), date));
        }
    }

    private void setTemperature(double temperature) {

        if(temperature == WeatherObservations.BELOW_ABSOLUTE_ZERO){
            showNoTemperature();
        }else{
            showTemperature(temperature);
        }
    }

    private void showNoTemperature() {
        noTemperatureTextView.setVisibility(View.VISIBLE);
        temperatureContainer.setVisibility(View.GONE);
    }

    private void showTemperature(double temperature){
        temperatureTextView.setText(String.format(getString(R.string.temperature_value), (int) temperature));
        temperatureProgressBar.setProgress(TemperatureUtil.getTemperaturePercentage(temperature));
        temperatureContainer.setVisibility(View.VISIBLE);
        noTemperatureTextView.setVisibility(View.GONE);
    }

    private void drawStationsInMap(WeatherObservations weatherObservations) {
        if(googleMap != null){
            for(WeatherInfo info : weatherObservations.getWeatherObservations()){
                drawStationInMap(info);
            }
        }
    }

    private void drawStationInMap(WeatherInfo info){
        LatLng coords = new LatLng(info.getLat(), info.getLng());
        googleMap.addMarker(new MarkerOptions().position(coords).title(info.getStationName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private void drawPositionInMap() {
        if(googleMap != null){
            LatLng coords = new LatLng(city.getLat(), city.getLng());
            googleMap.addMarker(new MarkerOptions().position(coords).title(city.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    private void startRequest(Request request){
        request.setTag(TAG);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void cancelRequests(){
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRequests();
    }
}
