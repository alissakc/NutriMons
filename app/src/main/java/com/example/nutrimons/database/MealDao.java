package com.example.nutrimons.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {

    @Query("SELECT * FROM meal ORDER BY mealName ASC")
    LiveData<List<Meal>> getAll();

    @Query("SELECT mealID FROM meal WHERE mealName =:mealName")
    int getID(String mealName);

    @Query("SELECT mealName FROM meal ORDER BY mealName ASC")
    List<String> getAllNames();

    @Query("SELECT * FROM meal WHERE mealID IN (:mealIds)")
    List<Meal> loadAllByIds(int[] mealIds);

    @Query("SELECT * FROM meal WHERE mealName LIKE :search")
    Meal findByName(String search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Meal... meals);

    @Insert
    void insert(Meal meal);

    @Update
    public void updateMeal(Meal meals);

    @Delete
    void delete(Meal meal);

}
