package com.example.nutrimons;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<String> mealList;

    public MealAdapter(List<String> mealList){
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_exercise_list_item, parent,false);
        return new MealViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        String mealName = mealList.get(position);
        holder.mealName.setText(mealName);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class  MealViewHolder extends RecyclerView.ViewHolder{

        public TextView mealName;
        public MealViewHolder(@NonNull TextView itemView) {
            super(itemView);
            mealName = itemView;
        }
    }
}
