package net.fkm.cloudmusictest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.fkm.cloudmusictest.R;
import net.fkm.cloudmusictest.adapter.MusicAdapter;
import net.fkm.cloudmusictest.database.DatabaseHelper;
import net.fkm.cloudmusictest.model.MusicModel;
import net.fkm.cloudmusictest.utils.CheckNetwork;
import net.fkm.cloudmusictest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyActivity extends AppCompatActivity {

    @BindView(R.id.rv_favorite_music)
    RecyclerView rvFavoriteMusic;

    private MusicAdapter adapter;
    private List<MusicModel> mList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        // 当主页被选中时，切换到MainActivity
                        Intent intentMain = new Intent(MyActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        return true;
                    case R.id.action_my:
                        return true;
                }
                return false;
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper dbHelper = new DatabaseHelper(MyActivity.this);
                try {
                    List<MusicModel> songInfos = dbHelper.getAllMusic();
                    if (songInfos != null && !songInfos.isEmpty()) {
                        mList.clear();
                        mList.addAll(songInfos);
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

        rvFavoriteMusic.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new MusicAdapter(this, mList);
        adapter.setActivityType(MyActivity.this);
        rvFavoriteMusic.setAdapter(adapter);

        adapter.setItemClikListener(new MusicAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                if (!CheckNetwork.isNetworkConnected(MyActivity.this)) {
                    ToastUtil.showToastLong("当前网络不可用，请检查您的网络设置");
                    return;
                }
                Intent intent = new Intent(MyActivity.this, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.NAME, mList.get(position).getName());
                intent.putExtra(PlayMusicActivity.POSTER, mList.get(position).getPoster());
                intent.putExtra(PlayMusicActivity.PATH, mList.get(position).getPath());
                intent.putExtra(PlayMusicActivity.AUTHOR, mList.get(position).getAuthor());
                intent.putExtra("music_list", (ArrayList<MusicModel>) mList);
                startActivity(intent);
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });
    }
}
