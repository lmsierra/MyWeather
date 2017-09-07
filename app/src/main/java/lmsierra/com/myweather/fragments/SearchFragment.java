package lmsierra.com.myweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lmsierra.com.myweather.R;

public class SearchFragment extends Fragment {

    @Bind(R.id.fragment_search_edittext_city) EditText searchEditText;
    @Bind(R.id.fragment_search_button_submit) Button button;

    private static final String SEARCH_TEXT = "SEARCH_TEXT";

    private View rootView;

    private SearchFinishedInterface searchFinishedListener;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null){
            searchEditText.setText(savedInstanceState.getString(SEARCH_TEXT));
        }

        configSearchButtonListener();
        configSeachEditTextListener();
        return rootView;
    }

    private void configSearchButtonListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void configSeachEditTextListener() {

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return true;
            }
        });
    }

    private void search() {
        if (searchFinishedListener != null) {
            searchFinishedListener.onSearchFinished(searchEditText.getText().toString().trim());
        }
    }

    public void setSearchFinishedListener(SearchFinishedInterface listener){
        searchFinishedListener = listener;
    }

    public interface SearchFinishedInterface {
        void onSearchFinished(String searchText);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_TEXT, searchEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
