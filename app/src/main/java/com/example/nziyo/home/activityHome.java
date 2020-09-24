package com.example.nziyo.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.nziyo.R;
import com.example.nziyo.utils.shared;

import java.util.ArrayList;

public class activityHome extends AppCompatActivity {

    //references
    GridView tracksGrid ;
    RelativeLayout playerLayout ;
    TextView playingTrackName ;

    ImageView trackIcon;
    TextView noTrackFoundText;

    //Global Variables
    private static final String TAG = "activityHome";
    String[] APP_PERMISSIONS = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ArrayList<String> MUSIC_PATHS = new ArrayList<>();

    Boolean isPlaying = false ;
    MediaPlayer player ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        //variables
        player = new MediaPlayer();

        //references
        tracksGrid = findViewById(R.id.musicGrid);
        playerLayout = findViewById(R.id.playerLayout);
        playingTrackName = findViewById(R.id.trackNameAndArtist);

        trackIcon = findViewById(R.id.trackIcon);
        noTrackFoundText = findViewById(R.id.noTrackFoundText);

        //translate bottom navigation
        playerLayout.setTranslationY(400);

        //get all music in phone
        checkPermissions();
    }

    private void checkPermissions() {
        if (permissionsGranted()){
            //do something
            getMusic();
        }
        else {
            //request permissions and recurse
            ActivityCompat.requestPermissions(activityHome.this,APP_PERMISSIONS,123);
            checkPermissions();
        }

    }

    private void getMusic() {
        //get paths
        MUSIC_PATHS = ((shared) getApplication()).getMusicPaths();

        //If array is empty show that no tracks have been found
        if (!MUSIC_PATHS.isEmpty()){
            noTrackFoundText.setVisibility(View.GONE);
        }

        //set tracks grid adapter
        homeMusicGridAdapter adapter = new homeMusicGridAdapter(this,MUSIC_PATHS);
        tracksGrid.setAdapter(adapter);

        //track on click
        tracksGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.reset();
                try {
                    player.setDataSource(MUSIC_PATHS.get(position));
                    player.prepare();
                    player.start();
                }
                catch (Exception ex){

                };

                //show track selected animation
                int firstPosition = tracksGrid.getFirstVisiblePosition();
                int childPosition = position - firstPosition;

                View viewItem = tracksGrid.getChildAt(childPosition);
                if (viewItem != null){
                    //change background color for track
                    final RelativeLayout layout = viewItem.findViewById(R.id.trackBackground);
                    layout.setBackgroundResource(R.color.colorPrimaryDark);

                    //restore background color
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.setBackgroundResource(R.color.colorPrimary);
                        }
                    },1000);
                }

                //set items for the current playing track
                TextView trackNameChild = viewItem.findViewById(R.id.trackNameAndArtistz);
                ImageView trackImageChild = viewItem.findViewById(R.id.trackImage);

                //set player items
                playingTrackName.setText(trackNameChild.getText().toString()); //track name

                BitmapDrawable drawable = (BitmapDrawable) trackImageChild.getDrawable(); //track image
                Bitmap image = drawable.getBitmap();
                trackIcon.setImageBitmap(image);

                //show player
                if (!isPlaying){
                    isPlaying = true ;
                    playerLayout.animate().translationY(0).setDuration(500);
                }


            }
        });


        playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean permissionsGranted() {
        for (String permission : APP_PERMISSIONS){
            if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false ;
            }
        }
        return  true ;
    }
}
