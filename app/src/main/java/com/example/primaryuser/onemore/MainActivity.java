package com.example.primaryuser.onemore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    final static private String APP_KEY = "zjb1tlu4ygric2b";
    final static private String APP_SECRET = "5bctcf8et7as1ig";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    Button btnTakePhoto;
    ImageView imgTakenPhoto;
    private static final int Cam_request = 1313;
    Bitmap thumbnail;
    Button directoryButton;

    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);

        btnTakePhoto = (Button) findViewById(R.id.button1);
        btnTakePhoto.setOnClickListener(new btntakePhotoClicker());
        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);
    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    //Camera Pic
    public class btntakePhotoClicker implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, Cam_request);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Cam_request) {
            try {
                thumbnail = (Bitmap) data.getExtras().get("data");
                imgTakenPhoto.setImageBitmap(thumbnail);
            } catch (Exception e) {

            }
        }
    }

    public class listAllFiles implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            String hash = "";
            try {
                Intent listingFiles = new Intent(String.valueOf(mDBApi.metadata(filepath, 4, null, false, null)));
            } catch (DropboxException e) {
                e.printStackTrace();
            }
        }

/*
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }
*/

        public class uploadingTODP extends AsyncTask<Void, Void, Boolean> {

            private DropboxAPI<?> dropboxAPI;
            private String filePath;
            private Context context;

            public uploadingTODP(Context context, DropboxAPI<?> dropbox,
                                 String path) {
                this.context = context.getApplicationContext();
                this.dropboxAPI = dropbox;
                this.filePath = path;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                final File tempDir = context.getCacheDir();
                File tempFile = new File(tempDir + "Picture" + ".jpg");
                //File tempFile = thumbnail;
                FileWriter fr;
                try {
                    tempFile = File.createTempFile("file", ".txt", tempDir);
                    //tempFile = Bitmap.createBitmap(tempFile);
                    fr = new FileWriter(String.valueOf(tempFile));
                    fr.write(String.valueOf(tempFile));
                    fr.close();

                    FileInputStream fileInputStream = new FileInputStream(String.valueOf(tempFile));
                    dropboxAPI.putFile(filePath + "photo.txt", fileInputStream,
                            tempFile.length(), null, null);
                    tempFile.deleteOnExit();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DropboxException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(context, "File Uploaded Sucesfully!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }


    }
}
