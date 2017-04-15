package com.fame.plumbum.lithicsin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.model.DataSetMiscQues;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by pankaj on 28/2/17.
 */

public class MiscQuesAdapter extends RecyclerView.Adapter<MiscQuesAdapter.ViewHolder> {

    private List<DataSetMiscQues> DataList;
    private String parentActivity;
    private Context mContext;
    private static List<Integer> values;
    private ArrayAdapter<String> adapter;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView ques, ans;
        MaterialEditText ans_text;
        Spinner ans_binary;

        ViewHolder(View view) {
            super(view);
            ques = (TextView) view.findViewById(R.id.text_question);
            ans = (TextView) view.findViewById(R.id.text_ans);
            ans_text = (MaterialEditText) view.findViewById(R.id.ans_text);
            ans_binary = (Spinner) view.findViewById(R.id.ans_binary);
            ans_binary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    values.add(getAdapterPosition(), i);
                    Log.e("POSITION_SPINNER", i + "");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    public MiscQuesAdapter(List<DataSetMiscQues> dataitem, String parentActivity, Context mContext) {
        this.DataList = dataitem;
        this.parentActivity = parentActivity;
        this.mContext = mContext;
        values = new ArrayList<>(Collections.nCopies(20, 0));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
//        if (parentActivity.contentEquals("answers")) {
//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_misc_ques, parent, false);
//        }else{
        List<String> binary = Arrays.asList(mContext.getResources().getStringArray(R.array.binary));
        adapter = new ArrayAdapter<>(mContext, R.layout.spinner_text_view, binary);
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_misc_ques_diag, parent, false);
//        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DataSetMiscQues m = DataList.get(position);
        holder.ques.setText(m.getQues());
        if (parentActivity.contentEquals("answers")) {
            holder.ans_binary.setVisibility(View.GONE);
            holder.ans_text.setVisibility(View.GONE);
            holder.ans.setVisibility(View.VISIBLE);
            holder.ans.setText(m.getAns());
        } else {
            if (m.getType().contentEquals("binary")) {
                holder.ans_binary.setVisibility(View.VISIBLE);
                holder.ans_binary.setAdapter(adapter);
                holder.ans_text.setVisibility(View.GONE);
                holder.ans_binary.setSelection(values.get(holder.getAdapterPosition()));
                holder.ans.setVisibility(View.GONE);
            } else {
                holder.ans_binary.setVisibility(View.GONE);
                holder.ans_text.setVisibility(View.VISIBLE);
                holder.ans.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }
}