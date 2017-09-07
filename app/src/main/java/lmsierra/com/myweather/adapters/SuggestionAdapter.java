package lmsierra.com.myweather.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import lmsierra.com.myweather.R;
import lmsierra.com.myweather.activities.MainActivity;
import lmsierra.com.myweather.model.Suggestion;
import lmsierra.com.myweather.model.Suggestions;
import lmsierra.com.myweather.model.db.dao.SuggestionDAO;
import lmsierra.com.myweather.views.SuggestionRowViewHolder;

import static lmsierra.com.myweather.views.SuggestionRowViewHolder.DeleteListener;
import static lmsierra.com.myweather.views.SuggestionRowViewHolder.SelectRowListener;

public class SuggestionAdapter extends RecyclerView.Adapter<lmsierra.com.myweather.views.SuggestionRowViewHolder>{

    private Suggestions suggestions;
    private final LayoutInflater inflater;
    private WeakReference<Context> context;
    private SelectRowListener selectRowListener;
    private DeleteListener deleteListener;

    public SuggestionAdapter(Context context, Suggestions suggestions) {
        this.suggestions = suggestions;
        this.inflater = LayoutInflater.from(context);
        this.context = new WeakReference<>(context);
    }

    public void delete(int position){
        suggestions.remove(position);
    }

    @Override
    public SuggestionRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_suggestion, parent, false);
        SuggestionRowViewHolder viewHolder = new SuggestionRowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SuggestionRowViewHolder holder, final int position) {
        final Suggestion suggestion = suggestions.get(position);
        holder.setSuggestionTitle(suggestion.getTitle());
        holder.setOnDeleteListener(new DeleteListener() {
            @Override
            public void onDelete() {
                SuggestionDAO suggestionDAO = new SuggestionDAO(context.get());
                suggestionDAO.delete(suggestions.get(position).getId());

                if(deleteListener != null){
                    deleteListener.onDelete();
                }
            }
        });

        holder.setOnSelectRowListener(new SelectRowListener() {
            @Override
            public void onSelect() {
                String suggestionTitle = suggestions.get(position).getTitle();
                Intent intent = new Intent(MainActivity.ITEM_SELECTED);
                intent.putExtra(MainActivity.EXTRA_ITEM_SELECTED, suggestionTitle);

                LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
            }
        });

    }

    public void setDeleteListener(DeleteListener listener){
        this.deleteListener = listener;
    }

    @Override
    public int getItemCount() {
        return suggestions.count();
    }
}
