package com.example.nutrimons.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity//(indices = {@Index(value = {"user_name"}, unique = false)})
public class User {

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int userID;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;



    /*public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }*/

}
