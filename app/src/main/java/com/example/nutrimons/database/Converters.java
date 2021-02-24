package com.example.nutrimons.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.lang.String;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<Meal> toMealList(String value) {
        if(value == null){
            return (null);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Meal>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromMealList(List<Meal> list) {
        if(list == null){
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Exercise> toExerciseList(String value) {
        if(value == null){
            return (null);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Exercise>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromExerciseList(List<Exercise> list) {
        if(list == null){
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<String> toStringList(String value) {
        if(value == null){
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromStringList(List<String> list) {
        if(list == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(list);
        return new Gson().toJson(list);
    }


}
