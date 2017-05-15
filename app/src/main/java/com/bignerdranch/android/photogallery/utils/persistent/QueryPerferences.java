package com.bignerdranch.android.photogallery.utils.persistent;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by TINH HUYNH on 5/12/2017.
 */

public class QueryPerferences {
    private static final String PREP_SEARCH_QUERY = "searchQuery";
    private static final String PREP_LAST_RESULT_ID = "lastResultId";
    private static final String PREP_IS_ALARM_ON = "isAlarmOn";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREP_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREP_SEARCH_QUERY, query)
                .apply();
    }

    public static String getLastResultId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREP_LAST_RESULT_ID, null);
    }

    public static void setLastResultId(Context context, String lastResultId){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREP_LAST_RESULT_ID, lastResultId)
                .apply();
    }

    public static boolean isAlarmOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(PREP_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREP_IS_ALARM_ON, isOn)
                .apply();
    }

}
