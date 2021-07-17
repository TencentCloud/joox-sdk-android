package com.tencent.joox.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joox.sdklibrary.AuthState;
import com.joox.sdklibrary.AuthType;
import com.joox.sdklibrary.SDKInstance;
import com.joox.sdklibrary.SDKListener;

public class TestEnvActivity extends Activity implements View.OnClickListener,SDKListener {
    private TextView login_via_mobile;
    private TextView  login_via_qr;
    private TextView login_with_openId;
    private TextView login_via_ticket_token;
    private boolean commitResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testenv);
        initView();
        SDKInstance.getmInstance().registerListener(this);
    }

    private void initView(){
        login_via_mobile = findViewById(R.id.login_via_mobile);
        login_via_qr = findViewById(R.id.login_with_qr);
        login_with_openId = findViewById(R.id.login_with_openId);
        login_via_ticket_token = findViewById(R.id.login_with_ticket_token);
        login_via_mobile.setOnClickListener(this);
        login_via_qr.setOnClickListener(this);
        login_with_openId.setOnClickListener(this);
        login_via_ticket_token.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == login_via_mobile){
            SDKInstance.LOGIN_TYPE = AuthType.AUTH_WITH_MOBILE;
            commitResult = SharedPreferencesTool.getmInstance(getApplicationContext()).commitIntValue(SharedPreferencesTool.LOGIN_TYPE,AuthType.AUTH_WITH_MOBILE);

        }else if(v  == login_via_qr){
            SDKInstance.LOGIN_TYPE = AuthType.AUTH_WITH_QRCODE;
            commitResult = SharedPreferencesTool.getmInstance(getApplicationContext()).commitIntValue(SharedPreferencesTool.LOGIN_TYPE,AuthType.AUTH_WITH_QRCODE);

        }else if (v == login_with_openId){
            SDKInstance.LOGIN_TYPE = AuthType.AUTH_OPENID;
            commitResult = SharedPreferencesTool.getmInstance(getApplicationContext()).commitIntValue(SharedPreferencesTool.LOGIN_TYPE, AuthType.AUTH_OPENID);

        } else if (v == login_via_ticket_token) {
            SDKInstance.LOGIN_TYPE = AuthType.AUTH_TICKET_TOKEN;
            commitResult = SharedPreferencesTool.getmInstance(getApplicationContext()).commitIntValue(SharedPreferencesTool.LOGIN_TYPE, AuthType.AUTH_TICKET_TOKEN);
        }
        SDKInstance.getmInstance().exitJOOXSDK();
    }

    @Override
    public void currentAuthState(int authState) {
        if(authState == AuthState.INITED){
            try{
                if(commitResult){
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateCurrentPlayState(int playState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SDKInstance.getmInstance().unregisterListener(this);
    }
}
