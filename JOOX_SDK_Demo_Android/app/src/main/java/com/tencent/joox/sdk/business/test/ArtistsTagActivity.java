package com.tencent.joox.sdk.business.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joox.sdklibrary.SDKInstance;
import com.joox.sdklibrary.kernel.network.SceneBase;
import com.tencent.joox.sdk.R;
import com.tencent.joox.sdk.data.net.RequestParamBuilder;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class ArtistsTagActivity extends AppCompatActivity {

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
        SDKInstance.getIns().doJooxRequest(path, paramString, new SceneBase.OnSceneBack() {
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
                    Toast.makeText(ArtistsTagActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int errCode) {
                Toast.makeText(ArtistsTagActivity.this, "歌手请求失败， 错误码："+ errCode, Toast.LENGTH_SHORT).show();
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
