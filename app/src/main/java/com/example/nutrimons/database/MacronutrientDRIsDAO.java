package com.example.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MacronutrientDRIsDAO {
    @Query("SELECT * FROM macronutrientdris ORDER BY groupID ASC")
    List<MacronutrientDRIs> getAll();

    @Query("SELECT * FROM macronutrientdris WHERE age LIKE :searchAge AND sex LIKE :searchSex AND babyStatus LIKE :searchBabyStatus")
    MacronutrientDRIs findByGroup(String searchAge, String searchSex, String searchBabyStatus);

    @Insert
    void insertAll(MacronutrientDRIs... macronutrientDRIses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(MacronutrientDRIs macronutrientDRIs);

    @Update
    public void updateUser(MacronutrientDRIs macronutrientDRIs);

    @Delete
    void delete(MacronutrientDRIs macronutrientDRIs);


    @Query("DELETE FROM macronutrientDRIs")
    public void nukeTable();
}
