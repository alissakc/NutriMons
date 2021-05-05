package com.BAMM.nutrimons.database;

import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

@Dao
@TypeConverters({Converters.class})
public interface TamagotchiDao {

    @Query("SELECT * FROM tamagotchipet")
    List<TamagotchiPet> getAll();

    @Query("SELECT * FROM TamagotchiPet WHERE petID LIKE :search")
    TamagotchiPet findByPetID(int search);

    @Query("SELECT * FROM TamagotchiPet WHERE userId LIKE :search")
    TamagotchiPet findByUserId(int search);

    @Query("SELECT petName FROM tamagotchipet")
    String getPetName();

    @Query("SELECT level FROM tamagotchipet")
    int getLevel();


    @Query("SELECT healthLevel FROM tamagotchipet WHERE petId == 1")
    int gethealthlevel();


    @Query("SELECT waterLevel FROM tamagotchipet")
    int getwaterlevel();

    @Query("SELECT coins FROM tamagotchipet")
    int getCoins();

    @Query("SELECT lastLoggedIn FROM tamagotchipet")
    String getLastLoggedIn();

    @Query("SELECT totalClicks FROM tamagotchipet")
    int getTotalClicls();

    @Update
    void updateTamagotchi(TamagotchiPet tamagotchipet);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(TamagotchiPet tamagotchipet);
}
