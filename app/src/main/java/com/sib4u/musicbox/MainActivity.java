package com.sib4u.musicbox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.Listener {
boolean serviceBound=false ;
private MediaPlayer mediaPlayer;
ArrayList<Audio> audioList;
ImageButton imageButton;

    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

private RecyclerView recyclerView;
private  String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        recyclerView=findViewById ( R.id.MusicList );
        audioList=new ArrayList<> (  );
        imageButton=findViewById ( R.id.imageView );
        mediaPlayer=new MediaPlayer ();










        if( checkSelfPermission ( Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_DENIED ) {
            requestPermissions ( permissions, 1 );
        }
        else {
           // SongManager songManager=new SongManager ();
            //songsList=songManager.getPlayList ();
            loadAudio ();

        }
       CustomAdapter adapter=new CustomAdapter ( this,audioList,this);
        recyclerView.setAdapter ( adapter );
        //playAudio ( audioList.get ( 0 ).getData () );

        imageButton.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying ())
                {
                    mediaPlayer.stop ();
                    imageButton.setImageResource ( R.drawable.ic_baseline_play_arrow_24);
                }
                else
                {
                    try {
                        mediaPlayer.reset ();
                        playAudio ( 0 );
                        imageButton.setImageResource ( R.drawable.ic_round_pause_24 );
                    } catch (IOException e) {
                        e.printStackTrace ( );
                    }
                }
            }
        } );

    }
    private void playAudio(int audioIndex) throws IOException {
         mediaPlayer.setDataSource ( audioList.get ( audioIndex ) .getData ());
         mediaPlayer.prepare ();
         mediaPlayer.start ();
    }


    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null,selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();
    }


    @Override
    public void clickListener(int position) throws IOException {
        mediaPlayer.reset ();
        imageButton.setImageResource ( R.drawable.ic_round_pause_24 );
        playAudio ( position );
    }
}