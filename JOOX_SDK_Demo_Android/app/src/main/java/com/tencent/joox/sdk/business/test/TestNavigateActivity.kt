package com.tencent.joox.sdk.business.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.kernel.network.BaseRequestComposer
import com.joox.sdklibrary.kernel.network.NetConstants
import com.joox.sdklibrary.kernel.network.SceneBase.OnSceneBack
import com.joox.sdklibrary.kernel.network.SdkTokenRequest
import com.joox.sdklibrary.kernel.network.impl.UserInfoScene
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.SDKApplication
import com.tencent.joox.sdk.business.matchsong.MatchSongActivity
import com.tencent.joox.sdk.business.player.PlayerActivity
import com.tencent.joox.sdk.databinding.ActivityNavigateBinding
import com.tencent.joox.sdk.tools.SecondActivity
import kotlin.system.exitProcess

class TestNavigateActivity : AppCompatActivity() {

    val TAG = "NavigateActivity"
    private lateinit var binder: ActivityNavigateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)
        binder = ActivityNavigateBinding.inflate(layoutInflater)
        binder.debugApiTx.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, SecondActivity::class.java)
            startActivity(intent)
        }
        binder.refreshTx.setOnClickListener {
            SDKInstance.getIns().refreshToken(null)
        }
        binder.playerTx.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, PlayerActivity::class.java)
            startActivity(intent)
        }

        binder.category.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        binder.inputSongId.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, InputSongListIdActivity::class.java)
            startActivity(intent)
        }

        binder.scopeTokenInvalid.setOnCheckedChangeListener { buttonView, isChecked ->
            val scope = SDKApplication.instance.getScopeList()
            if (isChecked && scope.contains("public")) {
                val tempScope = "user_profile+playmusic+search"
                scopeChangeExitDialog(isChecked, tempScope)
            }

            if (!isChecked && !scope.contains("public")) {
                val tempScope = "public+user_profile+playmusic+search"
                scopeChangeExitDialog(isChecked, tempScope)
            }
        }

        val isChecked = !SDKApplication.instance.getScopeList().contains("public")
        binder.scopeTokenInvalid.isChecked = isChecked

        binder.tokenInvalid.setOnClickListener{
            val tokenInfo = SDKInstance.getIns().authManager.tokenInfo
            tokenInfo.token = "aaaa"
            Toast.makeText(this, "token已修改为无效的token。", Toast.LENGTH_SHORT).show()
        }

        binder.tokenEmpty.setOnClickListener{
            val tokenInfo = SDKInstance.getIns().authManager.tokenInfo
            tokenInfo.token = ""
            Toast.makeText(this, "token已设置为空。", Toast.LENGTH_SHORT).show()
        }

        binder.tokenExpiredTime.setOnClickListener{
                    modifyTokenExpired()
                    Toast.makeText(this, "token已修改为过期。", Toast.LENGTH_SHORT).show()
                }

        binder.txId3Test.setOnClickListener {
            startActivity(Intent(this, MatchSongActivity::class.java))
        }

        binder.testTimeout.setOnClickListener{
            val context = this
            //http://203.0.113.1 non routable address will cause connect timeout
            //https://httpbin.org/delay/20 20-second response time will cause read timeout
            val composer = BaseRequestComposer("http://203.0.113.1")
            val request = SdkTokenRequest(composer.requestUrl)
            val scene = UserInfoScene(request, object : OnSceneBack {
                override fun onSuccess(responseCode: Int, JsonResult: String) {
                    try {
                        Log.d(TAG,"test timeout response success");
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFail(errCode: Int) {
                    Log.d(TAG, "errcode: $errCode")
                    if (errCode == NetConstants.ErrType.ET_NETWORK_TIMEOUT) {
                        Toast.makeText(context, "ET_NETWORK_TIMEOUT!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            //GetTaskImpl.getmInstance().addTask(scene)
            Toast.makeText(this, "connecting", Toast.LENGTH_SHORT).show()
        }

        binder.logOut.setOnClickListener{
            SDKInstance.getIns().logout()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Activity","Navigate Activity onPause called");

    }

    override fun onDestroy() {
        Log.d("Activity","Navigate Activity onDestroy called");
        super.onDestroy()

    }

    private fun scopeChangeExitDialog(isChecked: Boolean, scope: String) {
        AlertDialog.Builder(this).setMessage("scope权限信息已更改，需退出再次登陆才能生效，是否退出？")
                .setNegativeButton("退出") { _, _ ->
                    SDKApplication.instance.saveScopeList(scope)
                    SDKInstance.getIns().logout()
                    exitProcess(0)

                }.setPositiveButton("取消") {  _, _ ->
                    binder.scopeTokenInvalid.isChecked = !isChecked
                }.show()
    }

    private fun modifyTokenExpired() {
        val tokenInfo = SDKInstance.getIns().authManager.tokenInfo
        tokenInfo.token = "Evrwl0eKXfAIWzXJIwzo2HHtL4cPOiM7hfvO6phpZ1DB5fsNQNOi9Sc_" +
                "eIs0Q5FSEnbkzIZr5uZb6OyxfIFWNU1E8QU-vM_WkL65botgQ" +
                "yhe60JqT7qhySDpgAkR-2t9CxFDwgRjHKHlJ84wlMRvRQ9QN00" +
                "mXtlPG5DU6pkLa05A5_NfmnCP3QSmNx4hljnKFDQBUxDaccUqIp-z" +
                "d1KEjBZ6-gqLyixrFkx-_Fy9eMebtIB_bwurd_MuQzV04ldPGXtZO" +
                "5uigZ3FZUWr_A5edCSAzHAf8d2LYsHfdSoSfVpht8D0NGcns8-MO2dx-" +
                "C95NqNZZFxQ1zzPB6KtF2k2T7WeBAIs2HymSQ5xa0YKDU4l9eNpNGlwkR21Y" +
                "pL4OHVeL8acj7U-8dLLMO3C33MCzeIAS4qTa3EMmHkiUyfD1xhCXP-oQQ2AxC8" +
                "jbBjIRpiiUXOVdbK_v27MsI33gpWDcPnDeeQCk3hzLWgtROiMTY--qXPgKnZir3gD" +
                "O29xTBpsuboYfP2HpKDiAZsazNSN9TKs6lQ7Om7_MP9jkUbuJaabs9FI8rSGkQgbNi" +
                "HjegQGmTwmu2NH9PvPDzG9pMZaapIhmFQTuuLKfgAvlym6OofqU9MIXatEb83iVFf3" +
                "xkySJCQ80cjPcAaerbfvszK4_vt9fpUd4icS90XH78j-gqvFc6QOuHSt9YRUOo-uIk" +
                "HnIzokdccxMwNV_exgtqZumvxxOFGobbIq1dSixN65pOggpXirjj8B4bLmO8mToUFBrZA" +
                "H2wDrwA3TmJEGhYxqaPo7J3K2G391awJ0zpXC6EZG47O-7Kh2oNlE88n5b41Js6Kqmp-9" +
                "zdDcH4zhERK82jJm2I6yqfr2HgX69CdL6tXADZYoc_mP832NR7c4n8gpv9kSGxZBlBuiCzHok" +
                "kenjE37E9FlwBQ_cmHk-AB4_ZYk-CDl5E5el2-e6eZYNI9400_I_6uuky4UT5RXJFfwP8GcEp" +
                "L1Me5GlM_zglkjeQQ%3D"
    }

}

