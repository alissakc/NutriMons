package com.example.nutrimons.database;

import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import androidx.room.Update;

import java.util.List;

@Dao
public interface TamagotchiDao {
    //@Query("SELECT petName FROM tamagotchipet")
    //String getName();

    //@Query("SELECT level FROM tamagotchipet")
    //int getLevel();


    @Query("SELECT healthLevel FROM tamagotchipet")
    int gethealthlevel();


    //@Query("SELECT waterLevel FROM tamagotchipet")
    //int getwaterlevel();

    //@Update
    //public void updateTamagotchi(TamagotchiPet tamagotchipet);
}
