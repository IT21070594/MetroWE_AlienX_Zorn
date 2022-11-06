package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Database.DatabaseHandler;
import Database.Songs;

public class ViewSongHome extends AppCompatActivity
{
    ImageView image;
    TextView name, langGenre, artist, currentTime, endTime;
    SeekBar seekBar;
    ImageButton pauseBtn, nextBtn, previousBtn, backBtn;

    int id;
    String lang;
    String genre;
    String Name;
    Songs songData;
    String artistName;
    String username;
    private byte[] imageInBytes;
    private byte[] audioInBytes;

    // Initializing media player
    MediaPlayer mediaPlayer = MyMediaPlayer2.getInstance();
    DatabaseHandler objectDatabaseHandler;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.myLibrary:
                        //startActivity(new Intent(getApplicationContext(), Library.class));
                        Intent intent1 = new Intent(getApplicationContext(),Library.class);
                        intent1.putExtra("user",username);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:

                        uploadButtonGo();
                        return true;
                }
                return false;
            }
        });



        try
        {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 700*1024*1024); //200MB is the new size

            backBtn = findViewById(R.id.backBtnHome);

            image = findViewById(R.id.playMusicImgHome);
            name = findViewById(R.id.songnameHome);
            langGenre = findViewById(R.id.langGenreHome);
            artist = findViewById(R.id.artistNameHome);

            currentTime = findViewById(R.id.currentTimeHome);
            endTime = findViewById(R.id.endTimeHome);
            seekBar = findViewById(R.id.seekbarHome);

            pauseBtn = findViewById(R.id.pauseButtonHome);
            nextBtn = findViewById(R.id.skipNxtBtnHome);
            previousBtn = findViewById(R.id.skipPrevBtnHome);

            objectDatabaseHandler = new DatabaseHandler(this);

            if(getIntent().getBundleExtra("songData") != null)
            {
                Bundle bundle = getIntent().getBundleExtra("songData");
                id = bundle.getInt("song_id");
                username=bundle.getString("user");
            }
            setResourceWithMusic();

            ViewSongHome.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(mediaPlayer!=null)
                    {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                        if(mediaPlayer.isPlaying())
                        {
                            pauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_32);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    playNextSong();
                                }
                            });
                        }
                        else {
                            pauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_32);
                        }
                    }

                    //Each 100ms it will check the media player & update the seekbar and current time according to the current position.
                    new Handler().postDelayed(this, 100);
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(mediaPlayer!=null && fromUser)
                        mediaPlayer.seekTo(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        };

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSongHome.this, Home.class);
                intent.putExtra("user",username);
                startActivity(intent);
                overridePendingTransition(0,0);
//                finish();
            }
        });

    }

    void setResourceWithMusic()
    {
        songData = objectDatabaseHandler.getOneSongData(id);

        //To set the name, language and genre.
        Name = songData.getSongName();
        lang = songData.getLang();
        genre = songData.getGenre();
        audioInBytes = songData.getAudio();
        artistName=songData.getArtistName();

        Bitmap bitmap = songData.getImage();;
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        name.setText(Name);
        langGenre.setText(lang + " " + genre);
        image.setImageBitmap(bitmap);
        endTime.setText(convertToMMSS(mediaPlayer.getDuration()+""));
        artist.setText(artistName);

        pauseBtn.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();
    }

    private void playMusic()
    {
        try
        {
            //Creates temporary file
            File file = File.createTempFile("prefix","suffix", getCacheDir());
            file.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(file);

            // audioInBytes written in the temporary file
            fos.write(audioInBytes);
            fos.close();

            Uri uri = Uri.fromFile(file);

            // Resetting media player instance to evade problems
            mediaPlayer.reset();

            // To set the audio stream type for our media player.
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // To set the uri to our media player.
            mediaPlayer.setDataSource(this,uri);

//            FileInputStream fis = new FileInputStream(file);
//            mediaPlayer.setDataSource(fis.getFD());

            // To prepare and start our media player.
            mediaPlayer.prepare();
            endTime.setText(convertToMMSS(mediaPlayer.getDuration()+""));
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // To display a toast message.
        Toast.makeText(this, "Audio playing..", Toast.LENGTH_SHORT).show();

    }

    private void playNextSong()
    {
        if(MyMediaPlayer2.currentIndex == objectDatabaseHandler.getAllSongsData().size()-1)
            return;
        MyMediaPlayer2.currentIndex += 1;
        mediaPlayer.reset();
        id += 1;
        setResourceWithMusic();
    }

    private void playPreviousSong()
    {
        if(MyMediaPlayer2.currentIndex == 0)
            return;
        MyMediaPlayer2.currentIndex -= 1;
        mediaPlayer.reset();
        id -= 1;
        setResourceWithMusic();
    }

    private void pausePlay()
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public static String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    public void uploadButtonGo(){
//        System.out.println(receiveIntent3.getStringExtra("user"));
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(username);

//        if(res.getCount()==0){
//            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
//            return ;
//        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        res.close();
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

        if(c3.compareTo(accType)==0){
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }
    }
}


