package com.example.nutrimons;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

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
                if(name.equals("pig_pets_1.png"))
                {
                    si.owned = 1;
                }
                si.name = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.category = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.cost = Integer.parseInt(s.substring(0, s.indexOf('.')));

                Bitmap bm = BAMM.scaleBitmap(BitmapFactory.decodeStream(is), 300);
                si.image = BAMM.BitMapToString(bm);

                db.shopItemDao().insert(si);
            }
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
