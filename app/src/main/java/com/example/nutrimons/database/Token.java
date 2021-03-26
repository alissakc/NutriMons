package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Token {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int tokenID;

    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "areTablesInitialized")
    public boolean areTablesInitialized;

    @ColumnInfo(name = "isShopInitialized")
    public boolean isShopInitialized;

    public Token(int userID)
    {
        this.userID = userID;
    }
}
