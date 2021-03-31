package com.example.nutrimons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailySummaryAdapter extends RecyclerView.Adapter<DailySummaryAdapter.DailySummaryViewHolder> {

    private List<String> DailySummaryList;

    public DailySummaryAdapter(List<String> DailySummaryList){
        this.DailySummaryList = DailySummaryList;
    }

    @NonNull
    @Override
    public DailySummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_summary_list_item, parent,false);
        return new DailySummaryViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailySummaryViewHolder holder, int position) {
        String DailySummaryName = DailySummaryList.get(position);
        holder.DailySummaryName.setText(DailySummaryName);
    }

    @Override
    public int getItemCount() {
        return DailySummaryList.size();
    }

    public static class DailySummaryViewHolder extends RecyclerView.ViewHolder{

        public TextView DailySummaryName;
        public DailySummaryViewHolder(@NonNull TextView itemView) {
            super(itemView);
            DailySummaryName = itemView;
        }
    }
}
