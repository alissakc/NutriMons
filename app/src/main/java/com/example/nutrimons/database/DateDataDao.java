package com.example.nutrimons.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

@Dao
@TypeConverters({Converters.class})
public interface DateDataDao {

    @Query("SELECT * FROM dateData WHERE date LIKE :search")
    DateData findByDate(String search);

    @Query("SELECT breakfast FROM dateData WHERE date LIKE :search")
    //LiveData<List<String>> findBreakfastByDate(String search);
    List<String> findBreakfastByDate(String search);

    @Query("SELECT lunch FROM dateData WHERE date LIKE :search")
    //LiveData<List<String>> findLunchByDate(String search);
    List<String> findLunchByDate(String search);

    @Query("SELECT dinner FROM dateData WHERE date LIKE :search")
    //LiveData<List<String>> findDinnerByDate(String search);
    List<String> findDinnerByDate(String search);

    @Query("SELECT snack FROM dateData WHERE date LIKE :search")
    //LiveData<List<String>> findSnackByDate(String search);
    List<String> findSnackByDate(String search);

    @Query("SELECT exercise FROM dateData WHERE date LIKE :search")
    //iveData<List<String>> findExercisesByDate(String search);
    List<String> findExercisesByDate(String search);


    @Query("SELECT water FROM dateData WHERE date LIKE :search")
    List<Double> findWaterByDate(String search);


    // UPDATE QUERIES
    // updates the breakfast column only
    @Query("UPDATE dateData SET breakfast=:breakfast WHERE date = :date")
    void updateBreakfast(final List<String> breakfast, String date);

    // updates the lunch column only
    @Query("UPDATE dateData SET lunch=:lunch WHERE date = :date")
    void updateLunch(final List<String> lunch, String date);

    // updates the dinner column only
    @Query("UPDATE dateData SET dinner=:dinner WHERE date = :date")
    void updateDinner(final List<String> dinner, String date);

    // updates the snack column only
    @Query("UPDATE dateData SET snack=:snack WHERE date = :date")
    void updateSnack(final List<String> snack, String date);

    // updates the meal plan columns only
    @Query("UPDATE dateData SET breakfast=(:breakfast), lunch=(:lunch), dinner=(:dinner), snack=(:snack) WHERE date = :date")
    public abstract void updateMealPlan(final List<String> breakfast, final List<String> lunch, final List<String> dinner, final List<String> snack, String date);

    // updates the exercise column only
    @Query("UPDATE dateData SET exercise=(:exercise) WHERE date = :date")
    public abstract void updateExercise(final List<String> exercise, String date);

    // updates the water column only
    @Query("UPDATE dateData SET water=:water WHERE date = :date")
    void updateWater(double water, String date);

    @Insert
    void insert(DateData dateData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateDateData(DateData dateData);

    @Delete
    void delete(DateData dateData);

    /*
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
     */
}
