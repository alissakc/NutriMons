package com.example.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VitaminDRIsDAO {
    @Query("SELECT * FROM vitamindris ORDER BY groupID ASC")
    List<VitaminDRIs> getAll();

    @Query("SELECT * FROM vitamindris WHERE age LIKE :searchAge AND sex LIKE :searchSex AND babyStatus LIKE :searchBabyStatus")
    VitaminDRIs findByGroup(String searchAge, String searchSex, String searchBabyStatus);

    @Insert
    void insertAll(VitaminDRIs... vitaminDRIses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(VitaminDRIs vitaminDRIs);

    @Update
    public void updateUser(VitaminDRIs vitaminDRIs);

    @Delete
    void delete(VitaminDRIs vitaminDRIs);


    @Query("DELETE FROM vitaminDRIs")
    public void nukeTable();
}
