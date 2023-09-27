package com.tencent.joox.sdk.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.databinding.ActivityNavigateNewBinding
import com.tencent.joox.sdk.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        binding.loginText.setText("login success!")
        binding.etMethod.setText("v1/discover")
        binding.etParam.setText("country=my&lang=en")
        binding.requestBtn.setOnClickListener{
            var methodString = binding.etMethod.text.toString()
            var paramString = binding.etParam.text.toString()
            SDKInstance.getIns().doJooxRequest(methodString, paramString, object : SceneBase.OnSceneBack {
                override fun onSuccess(responseCode: Int, JsonResult: String) {
                    this@SecondActivity.runOnUiThread{
                        binding.resultTx.setText(JsonResult)
                    }
                }

                override fun onFail(errCode: Int) {

                }
            })
        }

    }


}