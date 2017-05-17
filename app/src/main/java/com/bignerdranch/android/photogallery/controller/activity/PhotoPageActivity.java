package com.bignerdranch.android.photogallery.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.bignerdranch.android.photogallery.R;
import com.bignerdranch.android.photogallery.controller.fragment.PhotoPageFragment;

/**
 * Created by TINH HUYNH on 5/16/2017.
 */

public class PhotoPageActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return PhotoPageFragment.newInstance(getIntent().getData());
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PhotoPageFragment photoPageFragment =
                (PhotoPageFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (photoPageFragment != null) {
            WebView webView = photoPageFragment.getWebView();
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
