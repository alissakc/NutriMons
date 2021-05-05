package com.BAMM.nutrimons;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.ShopItem;

import java.io.IOException;
import java.io.InputStream;

public class InitializeShop {
    final String FOLDER = "ShopItems";

    public InitializeShop(AppDatabase db, AssetManager am) {
        try {
            String[] filePaths = am.list(FOLDER);
            for(String s : filePaths)
            {
                //String name = s;
                InputStream is = am.open(FOLDER + "/" + s);
                ShopItem si = new ShopItem();
                System.out.println(s);

                if(s.equals("pig_pets_100.png"))
                {
                    si.owned = 1;
                }
                si.name = s.substring(0, s.indexOf('_'));
                si.name = Character.toUpperCase(si.name.charAt(0))+si.name.substring(1);

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
