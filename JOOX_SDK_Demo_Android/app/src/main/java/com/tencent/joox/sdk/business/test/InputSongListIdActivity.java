package com.tencent.joox.sdk.business.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.joox.sdk.R;

public class InputSongListIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_songlistid);
        EditText editText = findViewById(R.id.et_param);

        findViewById(R.id.request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputSongListIdActivity.this, SongListActivity.class);
                intent.putExtra("id", editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
