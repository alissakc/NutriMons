package com.example.nutrimons.database;

import android.graphics.Bitmap;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShopItem {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int shopItemID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "cost")
    public int cost;

    @ColumnInfo(name = "owned")
    public int owned = 0;

    public ShopItem() {}
}
