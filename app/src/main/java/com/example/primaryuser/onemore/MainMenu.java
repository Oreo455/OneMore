package com.example.primaryuser.onemore;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;

public class MainMenu extends ActionBarActivity {
    final static private String APP_KEY = "zjb1tlu4ygric2b";
    final static private String APP_SECRET = "5bctcf8et7as1ig";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    AndroidAuthSession session;
    private File imageFile;

    @Override
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

    public void pictureTaker(View v) {
        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Challenge.JPG");
        Uri tempLocation = Uri.fromFile(imageFile);

        intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, tempLocation);
        intentPicture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        startActivityForResult(intentPicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (imageFile.exists()) {
                        Toast.makeText(this, "The File Was Saved at" + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error Saving File", Toast.LENGTH_LONG).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

}



