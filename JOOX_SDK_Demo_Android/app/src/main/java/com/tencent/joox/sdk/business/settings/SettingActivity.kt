package com.tencent.joox.sdk.business.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.DebugActivity
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.databinding.SettingPageBinding
import com.tencent.joox.sdk.tools.SingInActivity

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: SettingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarSetting.ivToolbarBack.visibility = View.VISIBLE
        binding.toolbarSetting.ivToolbarBack.setOnClickListener(this)
        binding.toolbarSetting.ivToolbarMenu.visibility = View.GONE
        binding.toolbarSetting.tvToolbarTitle.text = "Setting"
        binding.debugItem.setOnClickListener(this)
        binding.logOutItem.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.debug_item -> {
                val it = Intent(this, DebugActivity::class.java)
                startActivity(it)
            }
            R.id.log_out_item -> {
                logOut()
            }
            R.id.iv_toolbar_back -> {
                finish()
            }
        }
    }

    private fun logOut(){
        SDKInstance.getIns().logout()
        finish()
    }

}