package com.tencent.joox.sdk.songlisttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.tencent.joox.sdk.R;
import com.tencent.joox.sdk.RequestParamBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ArtistsActivity extends AppCompatActivity {

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

        String path = String.format("v1/tag/%s/artists?", id);

        String paramString = new RequestParamBuilder().setCommonParam()
                .setConnector().setCountryParam("hk")
                .setConnector().setLanguageParam("en").getResultString();
        SDKInstance.getmInstance().doJooxRequest(path, paramString, new SceneBase.OnSceneBack() {
            @Override
            public void onSuccess(int responseCode, String JsonResult) {
                String statusCode = null;
                try {
                    JSONObject jsonObject = new JSONObject(JsonResult);
                    statusCode = jsonObject.optString("error_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(statusCode)) {
                    Artists artists = gson.fromJson(JsonResult, Artists.class);
                    if (artists.getArtists() != null) {
                        categoryAdapter.setCategories(artists.getArtists().getItems());
                        categoryAdapter.notifyDataSetChanged();
                    }
                    Log.d("test", JsonResult);
                } else {
                    Toast.makeText(ArtistsActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int errCode) {
                Toast.makeText(ArtistsActivity.this, "歌手请求失败， 错误码："+ errCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private List<Artists.ArtistsBean.ItemsBean> categories;

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
            Artists.ArtistsBean.ItemsBean categoriesBean = categories.get(i);
            categoryViewHolder.textView.setText(categoriesBean.getName());
            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AppPlayManager.getInstance().setPlayList(categories);
//                    AppPlayManager.getInstance().playIndex(i);
                    Intent intent = new Intent(v.getContext(), ArtistsSongsActivity.class);
                    intent.putExtra("id", categoriesBean.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories != null ? categories.size() : 0;
        }

        public void setCategories(List<Artists.ArtistsBean.ItemsBean> categories) {
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
