package com.fame.plumbum.lithicsin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.model.DataSetMiscQues;

import java.util.List;

/**
 * Created by pankaj on 28/2/17.
 */

public class MiscQuesAdapter extends RecyclerView.Adapter<MiscQuesAdapter.ViewHolder> {

    private List<DataSetMiscQues> DataList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView ques, ans;

        ViewHolder(View view) {
            super(view);
            ques = (TextView) view.findViewById(R.id.text_question);
            ans = (TextView) view.findViewById(R.id.text_ans);
        }
    }

    public MiscQuesAdapter(List<DataSetMiscQues> dataitem) {
        this.DataList = dataitem;
    }

//    public void setDataList(List<DataSetMiscQues> dataitem){
//        this.DataList = dataitem;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_misc_ques, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataSetMiscQues m = DataList.get(position);
        holder.ans.setText(m.getAns());
        holder.ques.setText(m.getQues());
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }
}