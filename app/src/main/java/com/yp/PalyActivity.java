package com.yp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PalyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView songerNameText;
    private TextView trackNameText;
    private TextView startTime;
    private TextView endTime;
    private ImageView imageView;
    private SeekBar seekBar;
    private ImageButton btnPre,btnPlay,btnNext;
    private List<Song> songList;
    private int position;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        findView();

        Intent i = getIntent();
        position = i.getIntExtra("position",0);
        songList = (List<Song>) i.getSerializableExtra("songList");

        setValueToView(position);

//        Log.d("PalyActivity",songList.get(position).getPreview_url());
        initMediaPlayer(position);

        btnPlay.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void initMediaPlayer(int position){
        try{
            String path = songList.get(position).getPreview_url();
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void findView(){

        songerNameText = (TextView) findViewById(R.id.text_artist_name);
        trackNameText = (TextView) findViewById(R.id.text_trcak_name);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        imageView = (ImageView) findViewById(R.id.image_view);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        btnPre = (ImageButton) findViewById(R.id.btn_pre);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnPlay = (ImageButton) findViewById(R.id.btn_play);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                if(mediaPlayer != null){
                    int progress = sb.getProgress();
                    startTime.setText(progress/1000/60 + ":" + progress/1000%60);
                    mediaPlayer.seekTo(progress);
//                    mediaPlayer.start();
//                    btnPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });

    }

    private void setValueToView(int position){

        songerNameText.setText(songList.get(position).getSongerName());
        trackNameText.setText(songList.get(position).getSongName());
        Picasso.with(this).load(songList.get(position).getImageUrl()).resize(760,0).into(imageView);

        seekBar.setMax(songList.get(position).getDuration_ms());

        endTime.setText(songList.get(position).getDuration_ms()/1000/60 + ":" + songList.get(position).getDuration_ms()/1000%60);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pre:
                if(position > 0){
                    position--;
                    setValueToView(position);
                    initMediaPlayer(position);
                }
                break;
            case R.id.btn_play:
                if(mediaPlayer != null){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        btnPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }else if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    }
                }
                break;
            case R.id.btn_next:
                if(position < songList.size()){
                    position++;
                    setValueToView(position);
                    initMediaPlayer(position);
                }
                break;
                default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
