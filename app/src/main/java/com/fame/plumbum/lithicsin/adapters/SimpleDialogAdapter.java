package com.fame.plumbum.lithicsin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.model.ViewContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 5/3/17.
 */

public class SimpleDialogAdapter extends RecyclerView.Adapter<SimpleDialogAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private String parent;
    private boolean isGrid;
    private List<ViewContent> data = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        EditText editText;

        ViewHolder(View view) {
            super(view);
            editText = (EditText) view.findViewById(R.id.dialog_edittext);
        }
    }

    public SimpleDialogAdapter(Context context, boolean isGrid, List<ViewContent> data) {
        layoutInflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_edit_text, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.editText.setHint(data.get(position).getHint());
        holder.editText.setInputType(data.get(position).getInputType());
//        holder.editText.setMaxCharacters(data.get(position).getMaxSize());
//        holder.editText.setMinCharacters(data.get(position).getMinSize());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
