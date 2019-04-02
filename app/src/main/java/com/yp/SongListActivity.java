package com.yp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class SongListActivity extends AppCompatActivity {
    private static final String TOKEN = "BQBxXhtgHKcKvG-9S0FCScBicuP3fFogjAgHPImZxL1vwcCn3DbB6QuOHiwDSlKYUkTm5scdSVXE7sh3L12dE1hFlFSo5Lku-5ueFedvhO-hkeRwviOFAwGzA8ugrNPbD-d-u1tZ8pxteKYUc_LTNyelq9272YVIQcl1j1hdYw";

    private List<Song> songList = new ArrayList<>();
    private ListView listView;
    private ProgressBar progressBar;
    private static String artistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        listView = (ListView) findViewById(R.id.list_song);
        progressBar = (ProgressBar) findViewById(R.id.song_progress_bar);

        SongAdapter adapter = new SongAdapter(this,R.layout.song_list,songList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SongListActivity.this,PalyActivity.class);
                i.putExtra("position",position);
                i.putExtra("songList", (Serializable) songList);
                startActivity(i);

            }
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        artistId = intent.getStringExtra("spotifyId");

        new SongTask().execute(artistId);
    }

    class SongTask extends AsyncTask<String,Void,List<Song>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Song> doInBackground(String... strings) {

            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(TOKEN);

            SpotifyService spotifyService = api.getService();

            Tracks tracks = spotifyService.getArtistTopTrack(strings[0], "US");

            List<Track> trackList = tracks.tracks;
            if(trackList.size() > 10){
                for(int i=0;i<10;i++){
                    String songName = trackList.get(i).name;
                    String songerName = trackList.get(i).artists.get(0).name;
                    String imageUrl = trackList.get(i).album.images.get(0).url;
                    String preview_url = trackList.get(i).preview_url;
                    int duration_ms = (int) trackList.get(i).duration_ms;
                    songList.add(createSong(songName,songerName,imageUrl,preview_url,duration_ms));
                }
            }else {
                for (int i=0;i<trackList.size();i++){
                    String songName = trackList.get(i).name;
                    String songerName = trackList.get(i).artists.get(0).name;
                    String imageUrl = trackList.get(i).album.images.get(0).url;
                    String preview_url = trackList.get(i).preview_url;
                    int duration_ms = (int) trackList.get(i).duration_ms;
                    songList.add(createSong(songName,songerName,imageUrl,preview_url,duration_ms));
                }
            }

            return songList;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private Song createSong(String songName,String songerName,String imageUrl,String preview_url,int duration_ms){

        Song song = new Song();
        song.setSongName(songName);
        song.setSongerName(songerName);
        song.setImageUrl(imageUrl);
        song.setPreview_url(preview_url);
        song.setDuration_ms(duration_ms);

        return song;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_refresh,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }else if(id == R.id.refresh_menu){
            songList.clear();
            new SongTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }
}
