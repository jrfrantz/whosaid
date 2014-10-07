package com.jacobfrantz.whosaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import network.*;


import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.LogInCallback;

/**
 * Created by Jacob on 10/5/14.
 */
public class NewUserActivity extends Activity {
    Button recordButton;
    Button continueButton;
    Button selfieButton;
    MediaRecorder mRecorder;
    String mFileName;
    boolean isPlaying;
    boolean isRecorded;
    private String mCurrentPhotoPath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_user);

        recordButton = (Button) findViewById(R.id.record_audio);
        recordButton.setOnClickListener(new RecordOnClickListener());

        continueButton = (Button) findViewById(R.id.proceed);
        continueButton.setOnClickListener(new ContinueOnClickListener());

        selfieButton = (Button) findViewById(R.id.take_selfie);
        selfieButton.setOnClickListener(new View.OnClickListener() {
            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                return image;
            }
            @Override
            public void onClick(View view) {
                int REQUEST_IMAGE_CAPTURE = 1;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }


        });


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("nux", "prepare() failed");
        }

        isPlaying = false;
        isRecorded = false;
    }

    private class RecordOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!isPlaying) {
                if (mRecorder == null)
                    return;
                mRecorder.start();
                isPlaying = true;
                recordButton.setText("Stop Recording");
            } else {
                mRecorder.stop();
                mRecorder.release();
                isPlaying = false;
                isRecorded = true;
                recordButton.setText("Start Recording");
            }
        }
    }

    private class ContinueOnClickListener implements View.OnClickListener {
        // TODO(bob): this is obviously extremely hacky. anonymous users are disposable
        // fix this shit
        public void onClick(View v) {
            // send to Parse
            SharedPreferences sharedPref = NewUserActivity.this.getSharedPreferences("fname", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("com.example.whosaid.pref", "no");
            editor.commit();

            Log.d("nux", "continue was clicked");

            // send it off
            Backend.saveFiles(Backend.getCurrentUser(), new File(mCurrentPhotoPath), new File(mFileName));

            Intent i = new Intent(NewUserActivity.this, GuessVoiceActivity.class);
            startActivity(i);
            finish();
            Log.d("nux", "finished");
        }
    }
    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

}
