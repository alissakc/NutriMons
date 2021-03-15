package com.example.nutrimons;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<String> exerciseList;

    public ExerciseAdapter(List<String> exerciseList){
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_exercise_list_item, parent,false);
        return new ExerciseViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        String exerciseName = exerciseList.get(position);
        holder.exerciseName.setText(exerciseName);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{

        public TextView exerciseName;
        public ExerciseViewHolder(@NonNull TextView itemView) {
            super(itemView);
            exerciseName = itemView;
        }
    }
}
