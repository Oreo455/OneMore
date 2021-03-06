package com.example.primaryuser.onemore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;


public class MainActivity extends ActionBarActivity {
    final static private String APP_KEY = "zjb1tlu4ygric2b";
    final static private String APP_SECRET = "5bctcf8et7as1ig";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    AndroidAuthSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);
    }

    protected void onResume() {
        super.onResume();
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                AccessTokenPair accessToken = session.getAccessTokenPair();
                mainMenuFetcher();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public void loginView(View v) {
        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
    }

    private void mainMenuFetcher() {
        Intent getMainMenu = new Intent(this, MainMenu.class);
        startActivity(getMainMenu);
    }
}



