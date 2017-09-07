package lmsierra.com.myweather.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import lmsierra.com.myweather.R;
import lmsierra.com.myweather.fragments.ConnectionErrorFragment;
import lmsierra.com.myweather.fragments.NoConnectionFragment;
import lmsierra.com.myweather.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_CITY = "city";
    private String city;

    public static Intent intentFrom(Context context){
        return new Intent(context, DetailActivity.class);
    }

    private BroadcastReceiver connectionErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            replaceFragment(new ConnectionErrorFragment());
        }
    };

    private BroadcastReceiver noConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            replaceFragment(new NoConnectionFragment());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_activity_title);

        city = getIntent().getStringExtra(EXTRA_CITY);

        if(savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment(city);
            replaceFragment(detailFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_detail_placeholder_detail, fragment)
                .commit();
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

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(noConnectionReceiver, new IntentFilter(MainActivity.NO_CONNECTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(connectionErrorReceiver, new IntentFilter(MainActivity.CONNECTION_ERROR));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(noConnectionReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectionErrorReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                showExitAnimation();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showExitAnimation() {
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showExitAnimation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(getResources().getBoolean(R.bool.is_tablet_landscape)){
                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXTRA_CITY_NAME, city);
                setResult(0, intent);
                finish();
            }
        }
    }
}
