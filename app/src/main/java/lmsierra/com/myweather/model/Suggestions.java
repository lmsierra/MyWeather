package lmsierra.com.myweather.model;

import java.util.LinkedList;
import java.util.List;

public class Suggestions {

    private List<Suggestion> suggestionList;

    public Suggestions() {
    }

    public void add(Suggestion suggestion) {
        getAllSuggestions().add(suggestion);
    }

    public void add(Suggestions suggestions){
        getAllSuggestions().addAll(suggestions.getAllSuggestions());
    }

    public List<Suggestion> getAllSuggestions() {

        if (suggestionList == null) {
            suggestionList = new LinkedList<>();
        }

        return suggestionList;
    }

    public int count() {
        return getAllSuggestions().size();
    }

    public Suggestion get(int position) {
        return getAllSuggestions().get(position);
    }

    public void remove(int position) {
        getAllSuggestions().remove(position);
    }

    public void remove(Suggestion suggestion) {
        suggestionList.remove(suggestion);
    }

    public void removeAll() {
        getAllSuggestions().clear();
    }
}