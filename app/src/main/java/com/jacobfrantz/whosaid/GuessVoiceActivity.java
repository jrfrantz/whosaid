package com.jacobfrantz.whosaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseObject;

import network.UserInfo;

public class GuessVoiceActivity extends Activity {
    private ImageView face;

    private Button audioButtonLeft;
    private Button audioButtonRight;

    private MediaController audioControllerLeft;
    private MediaController audioControllerRight;

    private MediaPlayer audioPlayerLeft;
    private MediaPlayer audioPlayerRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseObject.registerSubclass(UserInfo.class);
        Parse.initialize(this, "RRSzNkADoF991MKjZBv6P4umaCcFpAfcGsNwgrZQ", "1pSC1xgubgF846ZJH3SdM4aFZrS60UcDaDKODmnE");
        // this sets ParseUser.getCurrentUser() to an anonymous user for us so async call is not req
        ParseUser.enableAutomaticUser();

        if (needsNux()) {
            Log.d("","nux");
            Intent nux = new Intent(this, NewUserActivity.class);
            // TODO(jacob): figure out how to make this transition robust to pressing back button
            startActivity(nux);
        } else {
            Log.d("", "olduser");

            setContentView(R.layout.activity_guess_voice);

            face = (ImageView) findViewById(R.id.personface);
            audioButtonLeft = (Button) findViewById(R.id.mediacontroller_left);
            audioButtonRight = (Button) findViewById(R.id.mediacontroller_right);
            audioButtonLeft.setOnClickListener(new AudioOnClickListener());
            audioButtonLeft.setOnClickListener(new AudioOnClickListener());

            audioPlayerLeft = MediaPlayer.create(this, R.raw.testaudio);
            audioPlayerRight = new MediaPlayer();

            face.setImageResource(R.drawable.testimg);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("", "olduser");

        setContentView(R.layout.activity_guess_voice);

        face = (ImageView) findViewById(R.id.personface);
        audioButtonLeft = (Button) findViewById(R.id.mediacontroller_left);
        audioButtonRight = (Button) findViewById(R.id.mediacontroller_right);
        audioButtonLeft.setOnClickListener(new AudioOnClickListener());
        audioButtonLeft.setOnClickListener(new AudioOnClickListener());

        audioPlayerLeft = MediaPlayer.create(this, R.raw.testaudio);
        audioPlayerRight = new MediaPlayer();

        face.setImageResource(R.drawable.testimg);
    }

    public boolean needsNux() {
        Log.d("","hello");
        SharedPreferences sharedPref = this.getSharedPreferences("fname", Context.MODE_PRIVATE);
        Log.d("","second");
        String res = sharedPref.getString("com.example.whosaid.pref", ""); // true is fallback
        System.out.println(res + "was read");
        if (res == "") {
            res = "yes";
        }
        //return ("yes").equals(res);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guess_voice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AudioOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            MediaPlayer player = null;
            if (view.getTag().equals("left")) {
                player = audioPlayerLeft;
                Log.d("clicklistener", "left click audio");
            } else if (view.getTag().equals("right")) {
                player = audioPlayerRight;
            } else {
                Log.e("clicklistener", "button was neither left nor right");
            }

            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }
    }
}
