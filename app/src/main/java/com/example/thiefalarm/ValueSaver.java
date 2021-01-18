package com.example.thiefalarm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class ValueSaver {



    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public ValueSaver(Activity activity)
    {

         sharedPref = (SharedPreferences)activity.getPreferences(Context.MODE_PRIVATE);

         editor =  sharedPref.edit();


    }
    void setValues(int progressValue,float sensivityValue)
    {
        editor.putInt("progressValue",progressValue);
        editor.putFloat("sensivityValue",sensivityValue);

        editor.commit();
    }

    Map<String,?> getValues()
    {
       return sharedPref.getAll();
    }


}
