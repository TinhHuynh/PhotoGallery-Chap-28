package com.bignerdranch.android.photogallery.utils.persistent;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by TINH HUYNH on 5/12/2017.
 */

public class QueryPerferences {
    private static final String PREP_SEARCH_QUERY = "searchQuery";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREP_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREP_SEARCH_QUERY, query)
                .apply();
    }
}
