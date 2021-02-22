package com.example.nutrimons.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface DateDataDao {

    @Query("SELECT * FROM dateData WHERE date LIKE :search")
    DateData findByDate(String search);

    // INSERT QUERIES
    // inserts into the breakfast column only
    @Query("INSERT INTO dateData (breakfast) VALUES (:breakfast)")
    void insertBreakfast(List<String> breakfast);

    // inserts into the lunch column only
    @Query("INSERT INTO dateData (lunch) VALUES (:lunch)")
    void insertLunch(List<String> lunch);

    // inserts into the dinner column only
    @Query("INSERT INTO dateData (dinner) VALUES (:dinner)")
    void insertDinner(List<String> dinner);

    // inserts into the snack column only
    @Query("INSERT INTO dateData (snack) VALUES (:snack)")
    void insertSnack(List<String> snack);

    // insert into the breakfast, lunch, dinner, and snack columns only
    @Query("INSERT INTO dateData (breakfast, lunch, dinner, snack) VALUES (:breakfast, :lunch, :dinner, :snack)")
    void insertMealPlan(List<String> breakfast, List<String> lunch, List<String> dinner, List<String> snack);

    // inserts into the exercise column only
    @Query("INSERT INTO dateData (exercise) VALUES (:exercise)")
    void insertExercise(List<String> exercise);

    // inserts into the water column only
    @Query("INSERT INTO dateData (water) VALUES (:water)")
    void insertWater(double water);


    // UPDATE QUERIES
    // updates the breakfast column only
    @Query("UPDATE dateData SET breakfast=:breakfast")
    void updateBreakfast(List<String> breakfast);

    // updates the lunch column only
    @Query("UPDATE dateData SET lunch=:lunch")
    void updateLunch(List<String> lunch);

    // updates the dinner column only
    @Query("UPDATE dateData SET dinner=:dinner")
    void updateDinner(List<String> dinner);

    // updates the snack column only
    @Query("UPDATE dateData SET snack=:snack")
    void updateSnack(List<String> snack);

    // updates the snack column only
    @Query("UPDATE dateData SET breakfast=:breakfast, lunch=:lunch, dinner=:dinner, snack=:snack")
    void updateMealPlan(List<String> breakfast, List<String> lunch, List<String> dinner, List<String> snack);

    // updates the exercise column only
    @Query("UPDATE dateData SET exercise=:exercise")
    void updateExercise(List<String> exercise);

    // updates the water column only
    @Query("UPDATE dateData SET water=:water")
    void updateWater(double water);

    @Insert
    void insert(DateData dateData);

    @Update
    public void updateDateDate(DateData dateData);

    @Delete
    void delete(DateData dateData);
}
