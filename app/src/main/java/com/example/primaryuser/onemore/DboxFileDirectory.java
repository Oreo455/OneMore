package com.example.primaryuser.onemore;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.util.ArrayList;

public class DboxFileDirectory extends ActionBarActivity{
    final static private String APP_KEY = "zjb1tlu4ygric2b";
    final static private String APP_SECRET = "5bctcf8et7as1ig";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    AndroidAuthSession session;
    private String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
    protected ArrayList backgroundProcessing(View v){
        ArrayList allFiles = new ArrayList<>();
        try
        {
            DropboxAPI.Entry directory = mDBApi.metadata(path,1000,null,true,null);
            for (DropboxAPI.Entry entry : directory.contents)
            {
                allFiles.add(entry.fileName());
            }
        }
        catch(DropboxException ex)
        {
            ex.printStackTrace();
        }
        return allFiles;
    }

    public void showFiles (View v){
        backgroundProcessing(v);
    }

}

