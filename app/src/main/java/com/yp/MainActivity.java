package com.yp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOKEN = "BQAMEytKG6myjoCsZgcuIfxFo7Hr9_sIw6rOUj4N0C-ER-v2J8e871RJ83skurP7KbH5xb8EOTiJ5ivnPsdu6nCq2jt_S_-1I28Smu2KZ-oGyhWbfxMzMTEG5Ykv8oybBGNUx-l08ilCE94dag66xS3U4Cq2hjS5WILj6IADfQ";
    private ListView listView;
    private EditText editText;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView error;

    private List<Songer> songerList = new ArrayList<>();
    private String[] songerNames = {"邓紫棋","薛之谦","张杰","林俊杰","许嵩","陈奕迅","谢霆锋","周杰伦","蔡依林","华晨宇","胡歌"};
//    private String[] songerNames = {"刘德华"};
    private SongerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//哈哈，我测试一下啦
        listView = (ListView) findViewById(R.id.list_view);
        editText = (EditText) findViewById(R.id.edit_query);
        btnSearch = (Button) findViewById(R.id.btn_search);
        progressBar = (ProgressBar) findViewById(R.id.songer_progress_bar);
//        error = (TextView) findViewById(R.id.error);
        btnSearch.setOnClickListener(this);
        adapter = new SongerAdapter(this,R.layout.songer_list,songerList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Songer songer = songerList.get(position);

                Intent intent = new Intent(MainActivity.this,SongListActivity.class);
                intent.putExtra("spotifyId",songer.getSpotifyId());
                startActivity(intent);
            }
        });

        new SongerTask().execute(songerNames);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String searchStr = editText.getText().toString();
                if (!TextUtils.isEmpty(searchStr)) {
                    boolean hasStr = false;
                    for (int i = 0; i < songerNames.length; i++) {
                        if (searchStr.equals(songerNames[i])) {
                            hasStr = true;
                            Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                            intent.putExtra("spotifyId", songerList.get(i).getSpotifyId());
                            startActivity(intent);
                        }
                    }
                    if (!hasStr) {
                        Toast.makeText(this, "不存在名字为" + searchStr + "的歌手", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
        }
    }



    class SongerTask extends AsyncTask<String,Void,List<Songer>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Songer> doInBackground(String... strings) {

            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(TOKEN);

            SpotifyService spotify = api.getService();

            Songer songer = null;

                for(int i=0;i<strings.length;i++) {
                    ArtistsPager artist = spotify.searchArtists(strings[i]);
                    songer = new Songer();
//                List<Artist> artistList = artist.artists.items;
//
//                if (artistList.size() >= 2) {
//                    songer.setName(artistList.get(1).name);
//                    songer.setSpotifyId(artistList.get(1).id);
//                    songer.setImageUrl(artistList.get(1).images.get(0).url);
//                    songerList.add(songer);
//                }
//
                    songer.setName(artist.artists.items.get(0).name);
                    songer.setSpotifyId(artist.artists.items.get(0).id);
                    songer.setImageUrl(artist.artists.items.get(0).images.get(0).url);
                    songerList.add(songer);
                }
                return songerList;
        }

        @Override
        protected void onPostExecute(List<Songer> songers) {

            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.songer_refresh,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh_menu){
            songerList.clear();
            new SongerTask().execute(songerNames);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
