package com.tencent.joox.sdk.songlisttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joox.sdklibrary.SDKInstance;
import com.joox.sdklibrary.kernel.network.SceneBase;
import com.tencent.joox.sdk.AppPlayManager;
import com.tencent.joox.sdk.R;
import com.tencent.joox.sdk.RequestParamBuilder;
import com.tencent.joox.sdk.TrackItem;

import java.util.List;

public class SongListActivity extends AppCompatActivity implements AppPlayManager.PlayerListener {

    Gson gson = new GsonBuilder().create();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CategoryAdapter categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        String id = getIntent().getStringExtra("id");

        String path = String.format("v1/playlist/%s/tracks", id);
        String paramString = new RequestParamBuilder().setCommonParam()
                .setConnector().setCountryParam("hk")
                .setConnector().setLanguageParam("en").getResultString();
        SDKInstance.getmInstance().doJooxRequest(path, paramString, new SceneBase.OnSceneBack() {
            @Override
            public void onSuccess(int responseCode, String JsonResult) {
                Log.d("DASF", JsonResult);
                ArtistsSong artistsSong = gson.fromJson(JsonResult, ArtistsSong.class);
                if (artistsSong.getTracks() != null) {
                    categoryAdapter.setCategories(artistsSong.getTracks().getItems());
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int errCode) {
                Toast.makeText(SongListActivity.this, "歌曲列表请求失败， 错误码："+ errCode, Toast.LENGTH_SHORT).show();
            }
        });

        AppPlayManager.getInstance().addPlayerListeners(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPlayManager.getInstance().removePlayerListeners(this);
    }

    @Override
    public void onPlayProgress(long progress, long during) {

    }

    @Override
    public void onError(int code) {

    }

    @Override
    public void onLoading(boolean isLoading) {
        if (isLoading) {
            findViewById(R.id.pb).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.pb).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSongListChange() {

    }

    @Override
    public void onPlaySongChange() {

    }

    @Override
    public void onIdle() {

    }

    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private List<TrackItem> categories;

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
            TrackItem categoriesBean = categories.get(i);
            categoryViewHolder.textView.setText(categoriesBean.getName() + ":" + categoriesBean.getTrack_label_flag());
            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppPlayManager.getInstance().setPlayList(categories);
                    AppPlayManager.getInstance().playIndex(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories != null ? categories.size() : 0;
        }

        public void setCategories(List<TrackItem> categories) {
            this.categories = categories;
        }
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        private TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_album);
            textView = itemView.findViewById(R.id.tv_album);
        }
    }
}
