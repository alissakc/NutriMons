package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import TamagotchiGame.Tamagotchi;

@Entity
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

    public TamagotchiPet(int healthLevel)
    {
        this.healthLevel = healthLevel;
    }
    public TamagotchiPet()
    {
    }

}
