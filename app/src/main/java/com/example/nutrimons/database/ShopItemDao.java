package com.example.nutrimons.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShopItemDao {
    @Query(("SELECT * FROM shopitem"))
    List<ShopItem> getAll();

    @Query("SELECT * FROM shopitem WHERE shopItemID = :search ")
    ShopItem getShopItemByID(String search);

    @Query("SELECT * FROM shopitem WHERE name Like :search ")
    ShopItem getShopItemByName(String search);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //effectively makes this an update/insert
    void insert(ShopItem shopItem);

    @Update
    public void updateShopItem(ShopItem shopItem);
}
