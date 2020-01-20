package com.example.musictest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] allSongNames;
    ListView songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList = findViewById(R.id.song_list);

//        Dexter.withActivity(this)
//                .withPermission(permission)
//                .withListener(listener)
//                .check();

        externalStoragePermission();
    }

    public void externalStoragePermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        displayAudioSongs();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }

                }).check();
    }

    public ArrayList<File> mySongs(File file){
        ArrayList<File> musicList = new ArrayList<>();

        File[] allFiles = file.listFiles();

        for (File individualFile: allFiles){

            if (individualFile.isDirectory() && !individualFile.isHidden()){
                musicList.addAll(mySongs(individualFile));
            }else if (individualFile.getName().endsWith(".mp3")
                    || individualFile.getName().endsWith(".wav")
                    || individualFile.getName().endsWith(".aac")
                    || individualFile.getName().endsWith(".wma")){

                musicList.add(individualFile);


            }
        }

        return musicList;
    }

    public void displayAudioSongs(){

        ArrayList<File> songs  = mySongs(Environment.getExternalStorageDirectory());

        allSongNames = new String[songs.size()];

        for (int songCounter = 0; songCounter < songs.size(); songCounter++){
            allSongNames[songCounter] = songs.get(songCounter).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_expandable_list_item_1, allSongNames);

        songList.setAdapter(adapter);
    }
}
