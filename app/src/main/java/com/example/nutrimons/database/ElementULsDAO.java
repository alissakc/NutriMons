package com.example.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ElementULsDAO {
    @Query("SELECT * FROM elementuls ORDER BY groupID ASC")
    List<ElementULs> getAll();

    @Query("SELECT * FROM elementuls WHERE age = :searchAge AND sex = :searchSex AND babyStatus = :searchBabyStatus")
    ElementULs findByGroup(String searchAge, String searchSex, String searchBabyStatus);

    @Insert
    void insertAll(ElementULs... elementULses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(ElementULs elementULs);

    @Update
    public void updateUser(ElementULs elementULs);

    @Delete
    void delete(ElementULs elementULs);

    @Query("DELETE FROM elementULs")
    public void nukeTable();
}
