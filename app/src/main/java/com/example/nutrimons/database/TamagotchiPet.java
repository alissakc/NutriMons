package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;


import TamagotchiGame.Tamagotchi;

@Entity
@TypeConverters({Converters.class})
public class TamagotchiPet {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int petId;

    @ColumnInfo(name = "userId")
    public int userId;

    @ColumnInfo(name = "petName")
    public String petName;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "healthLevel")
    public int healthLevel;

    @ColumnInfo(name = "waterLevel")
    public int waterLevel;

    @ColumnInfo(name = "spriteId")
    public int spriteId;

    @ColumnInfo(name = "coins")
    public int coins;


    @ColumnInfo(name = "totalClicks")
    public int totalClicks;

    @ColumnInfo(name = "lastLoggedIn")
    public String lastLoggedIn;



    @ColumnInfo(name = "shopItems")
    public List<String> shopItems;

    public TamagotchiPet(int healthLevel)
    {
        this.healthLevel = healthLevel;
        shopItems = new ArrayList<>();
    }

    public TamagotchiPet()
    {
        shopItems = new ArrayList<>();
    }

}
