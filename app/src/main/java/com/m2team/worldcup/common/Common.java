package com.m2team.worldcup.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class Common {

    public static String TAB_FONT = "fonts/Proxima Nova Alt Bold.otf";
    public static String PREF_FILE_NAME = "world_cup";

    public static void startActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static void initImageLoader(Context context, ImageLoader imageLoader, DisplayImageOptions options) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                //.bitmapConfig(Bitmap.Config.RGB_565)
                //.displayer(new RoundedBitmapDisplayer(12))
                .build();
    }

    public static String getPrefString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, "");
    }

    public static int getPrefInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getInt(key, 0);
    }

    public static long getPrefLong(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getLong(key, 0);
    }

    public static boolean getPrefBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putPrefValue(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer)
            editor.putInt(key, Integer.parseInt(value.toString()));
        else if (value instanceof String)
            editor.putString(key, value.toString());
        else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    public static void clearStringSet(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
}
