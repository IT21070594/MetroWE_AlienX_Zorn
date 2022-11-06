package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class ViewSong extends AppCompatActivity
{
    ImageView image;
    TextView name, langGenre, artist, currentTime, endTime;
    SeekBar seekBar;
    ImageButton pauseBtn, nextBtn, previousBtn, backBtn, editBtn, deleteBtn;

    int id;
    String lang;
    String genre;
    String Name;
    String artistName;
    Songs songData;

    private byte[] imageInBytes;
    private byte[] audioInBytes;

    // Initializing media player
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    DatabaseHandler objectDatabaseHandler;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Uploads Selected
        bottomNavigationView.setSelectedItemId(R.id.uploads);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        intent.putExtra("user",artistName);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        Intent intent1 = new Intent(getApplicationContext(),Library.class);
                        intent1.putExtra("user",artistName);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:
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

            backBtn = findViewById(R.id.backBtn);
            editBtn = findViewById(R.id.editBtn);
            deleteBtn = findViewById(R.id.deleteBtn);

            image = findViewById(R.id.playMusicImg);
            name = findViewById(R.id.songname);
            langGenre = findViewById(R.id.langGenre);
            artist = findViewById(R.id.artistName);

            currentTime = findViewById(R.id.currentTime);
            endTime = findViewById(R.id.endTime);
            seekBar = findViewById(R.id.seekbar);

            pauseBtn = findViewById(R.id.pauseButton);
            nextBtn = findViewById(R.id.skipNxtBtn);
            previousBtn = findViewById(R.id.skipPrevBtn);

            objectDatabaseHandler = new DatabaseHandler(this);

            if(getIntent().getBundleExtra("songData") != null)
            {
                Bundle bundle = getIntent().getBundleExtra("songData");
                id = bundle.getInt("song_id");
                artistName=bundle.getString("user");
            }
            setResourceWithMusic();

            ViewSong.this.runOnUiThread(new Runnable()
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
               finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("song_id", id);
                bundle.putString("song_name", Name);
                bundle.putString("song_lang", lang);
                bundle.putString("song_genre",genre);
                bundle.putByteArray("song_image", imageInBytes);
                bundle.putString("user",artistName);

                Intent intent = new Intent(ViewSong.this, UpdateSong.class);
                intent.putExtra("songData", bundle);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objectDatabaseHandler.deleteSong(id);

                mediaPlayer.reset();
                mediaPlayer.release();

//
                Intent intent = new Intent(ViewSong.this, MainActivity.class);
                intent.putExtra("user",artistName);
                startActivity(intent);
                overridePendingTransition(0,0);
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
        artistName = songData.getArtistName();

        audioInBytes = songData.getAudio();

        Bitmap bitmap = songData.getImage();;
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        name.setText(Name);
        langGenre.setText(lang + " " + genre);
        artist.setText(artistName);
        image.setImageBitmap(bitmap);
        endTime.setText(convertToMMSS(mediaPlayer.getDuration()+""));

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
        if(MyMediaPlayer.currentIndex == objectDatabaseHandler.getArtistSongsData(artistName).size()-1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        id += 1;
        setResourceWithMusic();
    }

    private void playPreviousSong()
    {
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
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
}


