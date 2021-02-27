package com.example.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ElementDRIsDAO {
    @Query("SELECT * FROM elementdris ORDER BY groupID ASC")
    List<ElementDRIs> getAll();

    @Query("SELECT * FROM elementdris WHERE age LIKE :searchAge AND sex LIKE :searchSex AND babyStatus LIKE :searchBabyStatus")
    ElementDRIs findByGroup(String searchAge, String searchSex, String searchBabyStatus);

    @Insert
    void insertAll(ElementDRIs... elementDRIses);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(ElementDRIs elementDRIs);

    @Update
    public void updateUser(ElementDRIs elementDRIs);

    @Delete
    void delete(ElementDRIs elementDRIs);

    @Query("DELETE FROM elementDRIs")
    public void nukeTable();
}
