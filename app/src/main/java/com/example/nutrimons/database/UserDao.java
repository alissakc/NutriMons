package com.example.nutrimons.database;

import androidx.lifecycle.LiveData;
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
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY email ASC")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email LIKE :search")
    User findByEmail(String search);

    @Query("SELECT * FROM user WHERE userID LIKE :search")
    User findByUserID(int search);

    @Insert
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(User user);

    @Update
    public void updateUser(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    public void nukeTable();

}
