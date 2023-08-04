package com.tencent.joox.sdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.kernel.network.SceneBase
import kotlinx.android.synthetic.main.activity_second.et_method
import kotlinx.android.synthetic.main.activity_second.et_param
import kotlinx.android.synthetic.main.activity_second.login_text
import kotlinx.android.synthetic.main.activity_second.request_btn
import kotlinx.android.synthetic.main.activity_second.result_tx

class SecondActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        login_text.setText("login success!")
        et_method.setText("v1/discover")
        et_param.setText("country=my&lang=en")
        request_btn.setOnClickListener{
            var methodString = et_method.text.toString()
            var paramString = et_param.text.toString()
            SDKInstance.getmInstance().doJooxRequest(methodString, paramString, object : SceneBase.OnSceneBack {
                override fun onSuccess(responseCode: Int, JsonResult: String) {
                    this@SecondActivity.runOnUiThread{
                        result_tx.setText(JsonResult)
                    }
                }

                override fun onFail(errCode: Int) {

                }
            })
        }

    }


}