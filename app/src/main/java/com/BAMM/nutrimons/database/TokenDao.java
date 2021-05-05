package com.BAMM.nutrimons.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TokenDao {
    @Query(("SELECT * FROM token WHERE tokenID = 1"))
    Token getToken();

    @Query(("SELECT * FROM token"))
    List<Token> getAll();

    @Query("SELECT userID FROM token WHERE tokenID = 1")
    int getUserID();

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(Token token);

    @Update
    public void updateToken(User user);
}
