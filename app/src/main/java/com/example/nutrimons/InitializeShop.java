package com.example.nutrimons;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.ShopItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InitializeShop {
    final String FOLDER = "ShopItems";

    public InitializeShop(AppDatabase db, AssetManager am) {
        try {
            String[] filePaths = am.list(FOLDER);
            for(String s : filePaths)
            {
                String name = s;
                InputStream is = am.open(FOLDER + "/" + s);
                ShopItem si = new ShopItem();


                si.name = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.category = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.cost = Integer.parseInt(s.substring(0, s.indexOf('.')));

                si.image = BitMapToString(BitmapFactory.decodeStream(is));
                db.shopItemDao().insert(si);

            }
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public String BitMapToString(Bitmap bitmap){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
