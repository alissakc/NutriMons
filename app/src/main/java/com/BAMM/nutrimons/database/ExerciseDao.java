package com.BAMM.nutrimons.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getAll();

    @Query("SELECT exercise_name FROM exercise ORDER BY exercise_name ASC")
    List<String> getAllNames();

    @Query("SELECT * FROM exercise WHERE exerciseID IN (:exerciseIds)")
    List<Exercise> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM exercise WHERE exercise_name LIKE :search")
    Exercise findByName(String search);

    @Insert
    void insertAll(Exercise... exercises);

    @Insert
    void insert(Exercise exercise);

    @Update
    public void updateExercise(Exercise exercises);

    @Delete
    void delete(Exercise exercise);
}
