package com.example.nutrimons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.DateData;
import com.example.nutrimons.database.TamagotchiPet;
import com.example.nutrimons.database.User;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

public class BAMM {
    private static AppDatabase db;

    public BAMM(AppDatabase db) { this.db = db; }

    public static String getDateString()
    {
        long date = System.currentTimeMillis();
        SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
        return Date.format(date);
    }

    public static DateData getCurrentDateData()
    {
        return db.dateDataDao().findByDate(getDateString());
    }

    public static User getCurrentUser()
    {
        return db.userDao().findByUserID(db.tokenDao().getUserID());
    }

    public static TamagotchiPet getCurrentTamagotchi()
    {
        return db.tamagotchiDao().findByUserId(db.tokenDao().getUserID());
    }

    public static Bitmap StringToBitMap(String encodedString){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String BitMapToString(Bitmap bitmap){ //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap scaleBitmap(Bitmap image, int size) //http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/
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
