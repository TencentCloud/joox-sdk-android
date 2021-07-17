package com.tencent.joox.sdk

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.joox.sdklibrary.AuthState
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.SDKListener
import com.tencent.joox.sdk.songlisttest.CategoryActivity
import com.tencent.joox.sdk.songlisttest.InputSongListIdActivity
import kotlinx.android.synthetic.main.activity_navigate.*
import kotlin.system.exitProcess

class NavigateActivity : AppCompatActivity() , SDKListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)
        SDKInstance.getmInstance().registerListener(this)
        debug_api_tx.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, SecondActivity::class.java)
            startActivity(intent)
        }
        refresh_tx.setOnClickListener {
            SDKInstance.getmInstance().refreshUserInfo()
        }
        player_tx.setOnClickListener{
            val intent = Intent()
            intent.setClass(this, PlayerActivity::class.java)
            startActivity(intent)
        }

        category.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        input_song_id.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, InputSongListIdActivity::class.java)
            startActivity(intent)
        }

        scope_token_invalid.setOnCheckedChangeListener { buttonView, isChecked ->
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
        scope_token_invalid.isChecked = isChecked

        token_invalid.setOnClickListener{
            val tokenInfo = SDKInstance.getmInstance().authManager.tokenInfo
            tokenInfo.token = "aaaa"
            Toast.makeText(this, "token已修改为无效的token。", Toast.LENGTH_SHORT).show()
        }

        token_empty.setOnClickListener{
            val tokenInfo = SDKInstance.getmInstance().authManager.tokenInfo
            tokenInfo.token = ""
            Toast.makeText(this, "token已设置为空。", Toast.LENGTH_SHORT).show()
        }

        token_expired_time.setOnClickListener{
                    modifyTokenExpired()
                    Toast.makeText(this, "token已修改为过期。", Toast.LENGTH_SHORT).show()
                }

        tx_id3_test.setOnClickListener {
            startActivity(Intent(this, MatchSongActivity::class.java))
        }
        log_out.setOnClickListener{
            SDKInstance.getmInstance().logout()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (SDKInstance.getmInstance().authManager.authState == AuthState.INITED) {
            token_expired_time.isChecked = false
            token_invalid.isChecked = false
            token_empty.isChecked = false

            val isChecked = !SDKApplication.instance.getScopeList().contains("public")
            scope_token_invalid.isChecked = isChecked

            SDKInstance.getmInstance().auth()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Activity","Navigate Activity onPause called");

    }

    override fun onDestroy() {
        Log.d("Activity","Navigate Activity onDestroy called");
        SDKInstance.getmInstance().unregisterListener(this)
        super.onDestroy()

    }

    override fun updateCurrentPlayState(playState: Int) {
    }

    override fun currentAuthState(authState: Int) {
//        if(authState == AuthState.INITED){
//            val intent = Intent()
//            intent.setClass(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
//            startActivity(intent)
//        }
    }

    private fun scopeChangeExitDialog(isChecked: Boolean, scope: String) {
        AlertDialog.Builder(this).setMessage("scope权限信息已更改，需退出再次登陆才能生效，是否退出？")
                .setNegativeButton("退出") { _, _ ->
                    SDKApplication.instance.saveScopeList(scope)
                    SDKInstance.getmInstance().logout()
                    exitProcess(0)

                }.setPositiveButton("取消") {  _, _ ->
                    scope_token_invalid.isChecked = !isChecked
                }.show()
    }

    private fun modifyTokenExpired() {
        val tokenInfo = SDKInstance.getmInstance().authManager.tokenInfo
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

