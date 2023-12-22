package net.fkm.cloudmusictest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.fkm.cloudmusictest.R;
import net.fkm.cloudmusictest.adapter.MusicAdapter;
import net.fkm.cloudmusictest.model.MusicModel;
import net.fkm.cloudmusictest.utils.CheckNetwork;
import net.fkm.cloudmusictest.utils.ToastUtil;
import net.fkm.cloudmusictest.utils.SongInfoFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_music)
    RecyclerView rvMusic;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private MusicAdapter adapter;
    private List<MusicModel> mList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        // 当主页被选中时，切换到MainActivity
                        return true;
                    case R.id.action_my:
                        // 当"我的"被选中时，切换到MyActivity
                        Intent intentMy = new Intent(MainActivity.this, MyActivity.class);
                        startActivity(intentMy);
                        return true;
                }
                return false;
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);

        rvMusic.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new MusicAdapter(this, mList);
        adapter.setActivityType(MainActivity.this);
        rvMusic.setAdapter(adapter);

        adapter.setItemClikListener(new MusicAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                if (!CheckNetwork.isNetworkConnected(MainActivity.this)) {
                    ToastUtil.showToastLong("当前网络不可用，请检查您的网络设置");
                    return;
                }
                Intent intent = new Intent(MainActivity.this, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.NAME, mList.get(position).getName());
                intent.putExtra(PlayMusicActivity.POSTER, mList.get(position).getPoster());
                intent.putExtra(PlayMusicActivity.PATH, mList.get(position).getPath());
                intent.putExtra(PlayMusicActivity.AUTHOR, mList.get(position).getAuthor());
                startActivity(intent);
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMusic(etSearch.getText().toString());
            }
        });
    }

    private void searchMusic(String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SongInfoFetcher fetcher = new SongInfoFetcher();
                try {
                    List<Map<String, String>> songInfos = fetcher.fetchSongInfo(title);
                    if (songInfos != null && !songInfos.isEmpty()) {
                        mList.clear();
                        for (Map<String, String> songInfo : songInfos) {
                            MusicModel musicModel = new MusicModel();
                            musicModel.setMusicId(songInfo.get("id"));
                            musicModel.setName(songInfo.get("name"));
                            musicModel.setAuthor(songInfo.get("singername"));
                            musicModel.setPoster(songInfo.get("coverImgUrl"));
                            musicModel.setPath(songInfo.get("songUrl"));
                            mList.add(musicModel);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}


