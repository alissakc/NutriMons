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
    public static final int MAX_DAILY_COINS = 20;

    private static AppDatabase mDb;

    public BAMM(AppDatabase mDb) { this.mDb = mDb; }

    public static String getDateString()
    {
        long date = System.currentTimeMillis();
        SimpleDateFormat Date = new SimpleDateFormat("MM/dd/yyyy");
        return Date.format(date);
    }

    public static DateData getDateData()
    {
        return mDb.dateDataDao().findByDate(getDateString());
    }

    public static DateData getDateData(String date)
    {
        return mDb.dateDataDao().findByDate(date);
    }

    public static User getCurrentUser()
    {
        return mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
    }

    public static TamagotchiPet getCurrentTamagotchi()
    {
        return mDb.tamagotchiDao().findByUserId(mDb.tokenDao().getUserID());
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
    
    public static void giveCoin()
    {
        DateData dd = getDateData();
        if(dd.coinsLeft > 0)
        {
            --dd.coinsLeft;
            mDb.dateDataDao().updateDateData(dd);

            User u = mDb.userDao().findByUserID(mDb.tokenDao().getUserID());
            u.nutriCoins += 1;
            mDb.userDao().insert(u);
        }
    }
}
