package lmsierra.com.myweather.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import lmsierra.com.myweather.R;
import lmsierra.com.myweather.fragments.ConnectionErrorFragment;
import lmsierra.com.myweather.fragments.DetailFragment;
import lmsierra.com.myweather.fragments.EmptyDetailFragment;
import lmsierra.com.myweather.fragments.NoConnectionFragment;
import lmsierra.com.myweather.fragments.SearchFragment;
import lmsierra.com.myweather.fragments.SuggestionListFragment;
import lmsierra.com.myweather.model.Suggestion;
import lmsierra.com.myweather.model.db.dao.SuggestionDAO;

public class MainActivity extends AppCompatActivity {

    public static final String ITEM_SELECTED = "lmsierra.com.myweather:item_selected";
    public static final String NO_CONNECTION = "lmsierra.com.myweather:NO_CONNECTION";
    public static final String CONNECTION_ERROR = "lmsierra.com.myweather:CONNECTION_ERROR";
    public static final String EXTRA_ITEM_SELECTED = "EXTRA_ITEM_SELECTED";

    public static final String EXTRA_CITY_NAME = "EXTRA_CITY_NAME";
    public static final int TABLET_CHANGE_CONFIG_RESULT = 1;

    private FragmentManager fragmentManager;

    private SuggestionListFragment suggestionListFragment;
    private SearchFragment searchFragment;

    private BroadcastReceiver itemSelectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showDetail(intent.getStringExtra(EXTRA_ITEM_SELECTED));
        }
    };

    private BroadcastReceiver noConnectionReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(isTabletLandscape()){
                fragmentManager.beginTransaction().replace(R.id.activity_main_placeholder_detail, new NoConnectionFragment()).commit();
            }
        }
    };

    private BroadcastReceiver connectionErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isTabletLandscape()){
                fragmentManager.beginTransaction().replace(R.id.activity_main_placeholder_detail, new ConnectionErrorFragment()).commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments(savedInstanceState);
    }

    private void initFragments(Bundle savedInstaceState) {


        if(savedInstaceState == null){
            searchFragment = new SearchFragment();
        }else{
            searchFragment = (SearchFragment) getSupportFragmentManager().getFragment(savedInstaceState, "searchFragment");
        }

        suggestionListFragment = new SuggestionListFragment();

        searchFragment.setSearchFinishedListener(new SearchFragment.SearchFinishedInterface() {
            @Override
            public void onSearchFinished(String searchText) {

                if (searchText.trim().length() > 0) {
                    showDetail(searchText);
                    new WriteSuggestionInDBTask(new Suggestion(searchText)).execute();
                }
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_placeholder_search, searchFragment)
                .replace(R.id.activity_main_placeholder_suggestions, suggestionListFragment)
                .commit();

        if(isTabletLandscape()){
            fragmentManager.beginTransaction().replace(R.id.activity_main_placeholder_detail, new EmptyDetailFragment()).commit();
        }
    }

    private void showDetail(String searchText) {

        if(!isTabletLandscape()){

            Intent intent = DetailActivity.intentFrom(MainActivity.this);
            intent.putExtra(DetailActivity.EXTRA_CITY, searchText);

            startActivityForResult(intent, TABLET_CHANGE_CONFIG_RESULT);
            this.overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
        }else{

            DetailFragment detailFragment = new DetailFragment(searchText);
            fragmentManager.beginTransaction().replace(R.id.activity_main_placeholder_detail, detailFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(itemSelectedReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(noConnectionReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectionErrorReceiver);
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(itemSelectedReceiver, new IntentFilter(ITEM_SELECTED));
        LocalBroadcastManager.getInstance(this).registerReceiver(noConnectionReceiver, new IntentFilter(NO_CONNECTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(connectionErrorReceiver, new IntentFilter(CONNECTION_ERROR));
    }

    private boolean isTabletLandscape(){
        return getResources().getBoolean(R.bool.is_tablet_landscape);
    }

    private class WriteSuggestionInDBTask extends AsyncTask<Void, Void, Void>{

        private Suggestion suggestion;

        public WriteSuggestionInDBTask(Suggestion suggestion){
            this.suggestion = suggestion;
        }

        @Override
        protected Void doInBackground(Void... params) {

            SuggestionDAO suggestionDAO = new SuggestionDAO(MainActivity.this);
            suggestionDAO.insert(suggestion);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            suggestionListFragment.loadSuggestions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TABLET_CHANGE_CONFIG_RESULT && data != null){
            String name = data.getStringExtra(EXTRA_CITY_NAME);
            showDetail(name);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "searchFragment", searchFragment);
    }
}
