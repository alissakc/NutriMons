package com.example.nutrimons.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY email ASC")
    List<User> getAll();

    @Query("SELECT email FROM user ORDER BY email ASC")
    List<String> getAllEmail();

    /*@Query("SELECT * FROM meal WHERE mealID IN (:mealIds)")
    List<Meal> loadAllByIds(int[] mealIds);*/

    @Query("SELECT * FROM user WHERE email LIKE :search")
    User findByName(String search);

    @Insert
    void insertAll(User... users);

    @Insert
    void insert(User user);

    @Update
    public void updateUser(User user);

    @Delete
    void delete(User user);


    @Query("DELETE FROM user")
    public void nukeTable();

}
