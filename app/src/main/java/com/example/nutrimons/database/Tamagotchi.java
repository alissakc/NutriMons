package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tamagotchi {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int petId;

    @ColumnInfo(name = "petName")
    public int petName;

    @ColumnInfo(name = "level")
    public int level;
}
