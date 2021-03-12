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

    @ColumnInfo(name = "petName")
    public int petName;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "healthLevel")
    public int healthLevel;

    public TamagotchiPet(int healthLevel)
    {
        this.healthLevel = healthLevel;
    }

}
