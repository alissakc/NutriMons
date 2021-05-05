package com.BAMM.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VitaminULsDAO {
    @Query("SELECT * FROM vitaminuls ORDER BY groupID ASC")
    List<VitaminULs> getAll();

    @Query("SELECT * FROM vitaminuls WHERE age LIKE :searchAge AND sex LIKE :searchSex AND babyStatus LIKE :searchBabyStatus")
    VitaminULs findByGroup(String searchAge, String searchSex, String searchBabyStatus);

    @Insert
    void insertAll(VitaminULs... vitaminULses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(VitaminULs vitaminULs);

    @Update
    public void updateUser(VitaminULs vitaminULs);

    @Delete
    void delete(VitaminULs vitaminULs);

    @Query("DELETE FROM vitaminULs")
    public void nukeTable();
}
