package com.tencent.joox.sdk

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.kernel.dataModel.MetaData
import com.joox.sdklibrary.localsong.SongMatchManager
import com.joox.sdklibrary.localsong.id3.ID3ParserUtil
import com.joox.sdklibrary.report.ReportManager
import com.joox.sdklibrary.report_external.ExternalMLRecommendBuilder
import com.tencent.ibg.fingerprint.biz.AudioFingerprintService
import com.tencent.ibg.fingerprint.rule.FingerprintPoint
import com.tencent.ibg.fingerprintdemo.AudioReaderJNI
import com.tencent.mars.xlog.Log
import kotlinx.android.synthetic.main.activity_id3.*
import java.io.File


@SuppressLint("SetTextI18n")
class MatchSongActivity : AppCompatActivity() {
    private val NO_SONG = "sdcard/joox_for_third/localTest/ 路径下没有歌曲"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id3)
        Log.d(TAG, Environment.getExternalStorageDirectory().path)
        val mList = getAllSongs()
        var metaData: MetaData? = null
        var pcm: ByteArray? = null
        var fingerprint: List<FingerprintPoint>? = null
        AudioFingerprintService.getInstance().init()
        //获取pcm
        if (mList != null && mList.isNotEmpty()) {
            metaData = getMetaData(mList[0])
            tx_id3.text = "${metaData.songName}\nalbum: ${metaData.albumName}\nsinger: ${metaData.artistName}"
            pcm = AudioReaderJNI.getTestBytes(mList[0])
            Log.d("MatchSongActivity", pcm.size.toString())
        } else {
            tx_id3.text = NO_SONG
        }

//        获取音频指纹
        btn_fingerprint.setOnClickListener {
            if (mList != null && mList.isNotEmpty() && pcm != null) {
                AudioFingerprintService.getInstance().getFingerprints(pcm, pcm.size) {
                    fingerprint = it
                    runOnUiThread {
                        tx_id3.text = "success !! fingerprint size = & ${fingerprint!!.size}"
                    }
                }
            } else {
                tx_id3.text = NO_SONG
            }
        }

//        匹配songId
        btn_net.setOnClickListener {
            if (fingerprint == null || metaData == null) {
                tx_id3.text = "指纹数据为空，请先获取指纹"
                return@setOnClickListener
            }
            if (mList != null && mList.isNotEmpty()) {
                netSceneTest(fingerprint!!, metaData!!, 20000, 25000)
            } else {
                tx_id3.text = NO_SONG
            }
        }

        setReportBtn()

    }

    private fun setReportBtn() {
        btn_report_test.setOnClickListener {
            val builder = ExternalMLRecommendBuilder(1, edit_json.text.toString())
            ReportManager.reportMLRecommend(builder)
            edit_json.text.clear()
        }
    }

    private fun netSceneTest(fingerprintPoint: List<FingerprintPoint>, metaData: MetaData, startTime: Int, endTime: Int) {
        SDKInstance.getmInstance().matchLocalSong(fingerprintPoint, metaData, startTime, endTime, 240,
                object : SongMatchManager.MatchSongCallBack {
                    override fun onSuccess(songId: String, expiredTime: Int, metaData: MetaData,
                                           fingerprints: List<FingerprintPoint>,
                                           fingerprintVersion: String, score: Int) {
                tx_id3.text = "songId: $songId   expiredTime: $expiredTime   " +
                        " metaData: $metaData   fingerprints sizFe: ${fingerprints.size} " +
                        " fingerprintVersion: $fingerprintVersion  score: $score"

                btn_copy_songid.setOnClickListener {
                    val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val mClipData = ClipData.newPlainText("Label", songId)
                    cm.primaryClip = mClipData
                    Toast.makeText(this@MatchSongActivity, "$songId 已复制到剪切板", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(errCode: Int, errorMsg: String) {
                tx_id3.text = "errCode: $errCode   errorMsg: $errorMsg"
            }

        })
    }

    private fun getMetaData(path: String): MetaData {
        val metaData = ID3ParserUtil.getID3(path)
        return MetaData(metaData.title, metaData.artist, metaData.album)
    }

    private fun getAllSongs(): List<String>? {
        val file = File("${Environment.getExternalStorageDirectory().path}/joox_for_third/localTest")
        if (!file.exists()) {
            file.mkdirs()
        }
        val files: Array<File>? = file.listFiles()
        Log.d(TAG, files?.size.toString())
        val songs: MutableList<String> = ArrayList()
        if (files != null) {
            for (i in files.indices) {
                songs.add(files[i].absolutePath)
            }
        }
        return songs
    }

    companion object {
        const val TAG = "Id3Activity";
    }
}