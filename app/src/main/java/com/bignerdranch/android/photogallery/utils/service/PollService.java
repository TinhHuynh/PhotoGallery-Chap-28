package com.bignerdranch.android.photogallery.utils.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bignerdranch.android.photogallery.R;
import com.bignerdranch.android.photogallery.controller.activity.PhotoGalleryActivity;
import com.bignerdranch.android.photogallery.model.GalleryItem;
import com.bignerdranch.android.photogallery.utils.download.FlickrFetchr;
import com.bignerdranch.android.photogallery.utils.persistent.QueryPerferences;

import java.util.List;

/**
 * Created by TINH HUYNH on 5/13/2017.
 */

public class PollService extends IntentService {

    private static final String TAG = "PollService";

    private static final long POLL_INTERVAL = 60 * 1000;

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.bignerdranch.android.photogallery.utils.SHOW_NOTIFICATION";
    public static final String PERM_STRING_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        QueryPerferences.setAlarmOn(context, isOn);

    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        String query = QueryPerferences.getStoredQuery(this);
        String lastResultId = QueryPerferences.getLastResultId(this);
        List<GalleryItem> items;

        if (query == null) {
            items = new FlickrFetchr().fetchRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }

        if (items.isEmpty()) {
            return;
        }

        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        } else {
            Log.i(TAG, "Got a new result: " + resultId);

            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);

        }

        QueryPerferences.setLastResultId(this, resultId);
    }

    private void showBackgroundNotification(int requestcode, Notification notification){
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestcode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_STRING_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetWorkAvaiable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetWorkAvaiable && connectivityManager.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }


}
