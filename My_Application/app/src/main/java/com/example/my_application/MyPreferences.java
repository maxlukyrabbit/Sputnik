package com.example.my_application;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private static final String PREFERENCE_NAME = "MyPreferences";
    private static final String KEY_EDIT_TEXT = "edit_text_value";

    public static void saveEditTextValue(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EDIT_TEXT, value);
        editor.apply();
    }

    public static String getEditTextValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EDIT_TEXT, "");
    }
}