package lmsierra.com.myweather.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lmsierra.com.myweather.R;

public class SuggestionRowViewHolder extends RecyclerView.ViewHolder{

    private Button deleteButton;
    private TextView suggestionTitleView;

    private DeleteListener deleteListener;
    private SelectRowListener selectRowListener;

    public SuggestionRowViewHolder(View itemView) {
        super(itemView);

        deleteButton = (Button) itemView.findViewById(R.id.row_suggestion_button_delete);
        suggestionTitleView = (TextView) itemView.findViewById(R.id.row_suggestion_textview_suggestion_title);

        configDeleteButtonListener();
        configureRowSelectListener(itemView);
    }

    private void configureRowSelectListener(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectRowListener != null) {
                    selectRowListener.onSelect();
                }
            }
        });
    }

    private void configDeleteButtonListener() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null) {
                    deleteListener.onDelete();
                }
            }
        });
    }

    public void setSuggestionTitle(String title){
        suggestionTitleView.setText(title);
    }

    public void setOnDeleteListener(DeleteListener listener){
        this.deleteListener = listener;
    }

    public void setOnSelectRowListener(SelectRowListener listener){
        this.selectRowListener = listener;
    }

    public interface DeleteListener{
        void onDelete();
    }

    public interface SelectRowListener{
        void onSelect();
    }
}
