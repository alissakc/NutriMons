package com.BAMM.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MacronutrientRangesDAO {
    @Query("SELECT * FROM macronutrientranges ORDER BY groupID ASC")
    List<MacronutrientRanges> getAll();

    @Query("SELECT * FROM macronutrientranges WHERE age LIKE :searchAge")
    MacronutrientRanges findByGroup(String searchAge);

    @Insert
    void insertAll(MacronutrientRanges... macronutrientRangeses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(MacronutrientRanges macronutrientRanges);

    @Update
    public void updateUser(MacronutrientRanges macronutrientRanges);

    @Delete
    void delete(MacronutrientRanges macronutrientRanges);

    @Query("DELETE FROM macronutrientRanges")
    public void nukeTable();
}
