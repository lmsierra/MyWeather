package lmsierra.com.myweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lmsierra.com.myweather.R;
import lmsierra.com.myweather.adapters.SuggestionAdapter;
import lmsierra.com.myweather.model.Suggestions;
import lmsierra.com.myweather.model.db.dao.SuggestionDAO;
import lmsierra.com.myweather.views.SuggestionRowViewHolder;

public class SuggestionListFragment extends Fragment {

    @Bind(R.id.fragment_suggestion_list_recyclerview) RecyclerView suggestionList;
    @Bind(R.id.fragment_suggestion_list_layout_suggestions) LinearLayout suggestionsLayout;
    @Bind(R.id.fragment_suggestion_list_textview_no_suggestions) TextView noSuggestionsTextView;

    private View rootView;

    private Suggestions suggestions;
    private SuggestionAdapter adapter;

    public SuggestionListFragment() {
        this.suggestions = new Suggestions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        ButterKnife.bind(this, rootView);

        adapter = new SuggestionAdapter(getActivity(), this.suggestions);
        adapter.setDeleteListener(new SuggestionRowViewHolder.DeleteListener() {
            @Override
            public void onDelete() {
                loadSuggestions();
            }
        });

        suggestionList.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestionList.setAdapter(adapter);

        loadSuggestions();

        return rootView;
    }

    public void loadSuggestions() {
        SuggestionDAO suggestionDAO = new SuggestionDAO(getActivity());
        this.suggestions.removeAll();
        this.suggestions.add(suggestionDAO.query());

        if(this.suggestions.count() > 0){

            showSuggestions();

        }else{

            showNoSuggestionsMessage();

        }

        adapter.notifyDataSetChanged();
    }

    private void showNoSuggestionsMessage() {
        noSuggestionsTextView.setVisibility(View.VISIBLE);
        suggestionsLayout.setVisibility(View.GONE);
    }

    private void showSuggestions() {
        noSuggestionsTextView.setVisibility(View.GONE);
        suggestionsLayout.setVisibility(View.VISIBLE);
    }
}
