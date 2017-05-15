package com.bignerdranch.android.photogallery.utils.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bignerdranch.android.photogallery.utils.persistent.QueryPerferences;

/**
 * Created by TINH HUYNH on 5/15/2017.
 */

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());

        boolean isOn = QueryPerferences.isAlarmOn(context);
        PollService.setServiceAlarm(context, isOn);
    }
}
