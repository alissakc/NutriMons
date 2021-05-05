package com.BAMM.nutrimons;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<String> exerciseList;
    private SparseBooleanArray mSelectedItemsIds;

    public ExerciseAdapter(List<String> exerciseList){
        this.exerciseList = exerciseList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_exercise_list_item, parent,false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        String exerciseName = exerciseList.get(position);
        holder.exerciseName.setText(exerciseName);
        holder.checkBox.setChecked(mSelectedItemsIds.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(position, !mSelectedItemsIds.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{

        public TextView exerciseName;
        public CheckBox checkBox;
        
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.recycler_view_item);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }


    }
}
