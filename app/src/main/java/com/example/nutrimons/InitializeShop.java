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

                si.name = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.category = s.substring(0, s.indexOf('_'));
                s = s.substring(s.indexOf('_') + 1);

                si.cost = Integer.parseInt(s.substring(0, s.indexOf('.')));

                Bitmap bm = scaleBitmap(BitmapFactory.decodeStream(is), 300);
                si.image = BitMapToString(bm);

                if(s.equals("pig_pets_1.png"))
                    si.owned = 1;

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

    private Bitmap scaleBitmap(Bitmap image, int size) //http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/
    {
        int width = image.getWidth(), height = image.getHeight();
        float ratio = (float) width / (float) height;

        if(ratio > 1)
        {
            width = size;
            height = (int) (width / ratio);
        }
        else
        {
            height = size;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
