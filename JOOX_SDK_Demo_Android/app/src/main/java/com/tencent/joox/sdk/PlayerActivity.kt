package com.tencent.joox.sdk


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.joox.sdklibrary.PlayErrState
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.down.DownTask
import com.joox.sdklibrary.down.keep.Callback
import com.joox.sdklibrary.down.keep.Progress
import com.joox.sdklibrary.kernel.dataModel.BaseAdInfo
import com.joox.sdklibrary.kernel.dataModel.BaseSongInfo
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.SDKApplication.Companion.instance
import com.tencent.joox.sdk.songlisttest.ArtistsSongsActivity
import com.tencent.joox.sdk.songlisttest.CategoryActivity
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity(), AppPlayManager.PlayerListener {


    private var handler: Handler = Handler()

    private lateinit var adapter: ArtistsSongsActivity.CategoryAdapter

    private var isSeeking: Boolean = false

    private var downTask: DownTask? = null

    private lateinit var downDb: DownDB;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        downDb = DownDB(this)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            var runnable: Runnable = Runnable {
                seek()
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (fromUser) {
//                    handler.removeCallbacks(runnable)
//                    handler.postDelayed(runnable, 200)
//                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                seek()
            }

        })

        start.setOnClickListener {
            if (AppPlayManager.getInstance().isPreparing) {
                Toast.makeText(PlayerActivity@this, "正在准备，稍后操作", Toast.LENGTH_SHORT).show()
            } else if (!AppPlayManager.getInstance().isPrepared) {
                AppPlayManager.getInstance().play()
            } else if(!AppPlayManager.getInstance().isPlaying) {
                AppPlayManager.getInstance().resumePlay()
                start.setText("暂停")
            } else if (AppPlayManager.getInstance().isPlaying) {
                AppPlayManager.getInstance().pausePlay()
                start.setText("播放")
            }
        }

        if (AppPlayManager.getInstance().isPlaying) {
            start.setText("暂停")
        } else {
            start.setText("播放")
        }

        down.setOnClickListener {

            val trackItem = AppPlayManager.getInstance().currentTrackItem

            if(trackItem!=null){
                downTask?.cancel()

                down(trackItem)
            }


        }

        canceldown.setOnClickListener {
            downTask?.cancel()
        }

        play_down.setOnClickListener {
            val trackItems = downDb.allDownSongs
            if (trackItems != null) {
                AppPlayManager.getInstance().setPlayList(trackItems)
                AppPlayManager.getInstance().playIndex(0)
            } else {
                Toast.makeText(applicationContext, "没有下载歌曲", Toast.LENGTH_SHORT).show()
            }
        }

        del_download.setOnClickListener{
            val trackItem = AppPlayManager.getInstance().currentTrackItem;
            if(trackItem != null){
                SDKInstance.getmInstance().stopPlay()
                SDKInstance.getmInstance().deleteDownFile(trackItem.id,trackItem.album_id);
                downDb.deleteDownSong(trackItem)
                val trackItems = downDb.allDownSongs
                AppPlayManager.getInstance().setPlayList(trackItems)
                if (trackItems != null && trackItems.size > 0) {
                    AppPlayManager.getInstance().playIndex(0)
                }else{
                    Toast.makeText(instance, "no more songs to play!", Toast.LENGTH_SHORT).show()
                    SDKInstance.getmInstance().stopPlay()
                }
            }
        }

        ad_btn.setOnClickListener{
            SDKInstance.getmInstance().getAudioAd(object: SceneBase.OnSceneBack{
                override fun onSuccess(p0: Int, p1: String?) {
                    Log.d("PlayerActivity","request result is "+p1);
                    var gson = GsonBuilder().create()
                    val adInfo : AdInfo = gson.fromJson<AdInfo>(p1, AdInfo::class.java)
                    val songInfo = BaseSongInfo(BaseSongInfo.TYPE_AD)
                    val adToPlay = BaseAdInfo()
                    adToPlay.adId  = adInfo.result.ad_infos.get(0).ad_id
                    adToPlay.audioUrl = adInfo.result.ad_infos.get(0).adcreative_elements.audio_url
                    AppPlayManager.getInstance().playAd(adToPlay)
                }

                override fun onFail(errCode: Int) {
                    Log.d("PlayerActivity","request failed errCode is  " + errCode);
                    Toast.makeText(applicationContext, "广告暂时不可用， 错误码：$errCode", Toast.LENGTH_SHORT).show()

                }
            })
        }

        pre.setOnClickListener {
            AppPlayManager.getInstance().pre();
        }

        next.setOnClickListener {
            AppPlayManager.getInstance().next();
        }

        AppPlayManager.getInstance().addPlayerListeners(this)

        onLoading(AppPlayManager.getInstance().isLoading)

        adapter = ArtistsSongsActivity.CategoryAdapter()
        adapter.setCategories(AppPlayManager.getInstance().items)
        rv_song_list.layoutManager = LinearLayoutManager(this)
        rv_song_list.adapter = adapter


        if (AppPlayManager.getInstance().items != null) {
            tv_name.setText(AppPlayManager.getInstance(). currentTrackItem ?. name)
        }

        song_list.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, CategoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
        }

        play_stop.setOnClickListener {
            AppPlayManager.getInstance().stop()
        }

        refreshQuality()

        tv_quality.setOnClickListener {
            val items = arrayOf<String>("低","标准", "中");
            val dialog = AlertDialog.Builder(this).setItems(items, object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if(which == 0) {
                        AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_48K
                    } else if (which == 1) {
                        AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_96K
                    } else {
                        AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_192K
                    }

                    refreshQuality()
                }

            })
            dialog.show()
        }

    }

    fun refreshQuality() {
        val quality = AppPlayManager.getInstance().quality;
        if(quality == BaseSongInfo.TYPE_48K) {
            tv_quality.setText("低")
        } else if (quality == BaseSongInfo.TYPE_96K) {
            tv_quality.setText("标准")
        } else {
            tv_quality.setText("中")
        }
    }

    fun down(trackItem: TrackItem) {
        if(SDKInstance.getmInstance().isDownFileExist(trackItem.id, AppPlayManager.getInstance().quality)) {
            Toast.makeText(applicationContext, "已经下载，不需要重新下载", Toast.LENGTH_SHORT).show()
            downDb.insertDownSong(trackItem)
            return
        }
        Toast.makeText(applicationContext, "开始下载", Toast.LENGTH_SHORT).show()
        downTask = SDKInstance.getmInstance().down(trackItem.id, AppPlayManager.getInstance().quality, object : Callback {
            override fun onComplete(outFilePath: String?) {
                Log.e("test", "down finish")
                Toast.makeText(applicationContext, "下载完成", Toast.LENGTH_SHORT).show()
                down.setText("下载")
                downDb.insertDownSong(trackItem)
                downTask = null
            }

            override fun onProgress(progress: Progress) {
                Log.e("test", progress.read.toString() + " " + progress.total.toString())
                down.setText((progress.read * 1f / progress.total * 100).toInt().toString() + "%")
            }

            override fun onError(code: Int) {
                down.setText("下载")
                if(code == PlayErrState.LIMIT_ERROR) {
                    Toast.makeText(applicationContext, "没有权限下载", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(applicationContext, "下载错误", Toast.LENGTH_SHORT).show()
                }
                downTask = null
            }

        })
    }

    private fun seek() {
        Log.d("test", " seek " + seekBar.progress.toString())
        SDKInstance.getmInstance().seekTo(seekBar.progress.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        AppPlayManager.getInstance().unInit()
    }



    fun formatSeconds(mins: Long): String {
        val seconds = (mins / 1000).toInt()
        val builder = StringBuilder()
        var temp = seconds / 60
        if (temp == 0 || temp < 10) {
            builder.append("0").append(temp)
        } else {
            builder.append(temp)
        }
        builder.append(":")
        temp = seconds % 60
        if (temp == 0 || temp < 10) {
            builder.append("0").append(temp)
        } else {
            builder.append(temp)
        }
        return builder.toString()
    }

    override fun onLoading(isLoading: Boolean) {
        if(isLoading) {
            pb.visibility = View.VISIBLE
        } else {
            pb.visibility = View.GONE
        }
    }


    override fun onPlayProgress(progress: Long, during: Long) {
        if (!isSeeking) {
            seekBar.progress = progress.toInt()
            seekBar.max = during.toInt()
            tv_time.setText(formatSeconds(progress))
            tv_during.setText(formatSeconds(during))
        }
    }

    override fun onSongListChange() {
        adapter.setCategories(AppPlayManager.getInstance().items)
        adapter.notifyDataSetChanged()
    }

    override fun onError(code: Int) {

    }

    override fun onPlaySongChange() {
        tv_name.setText(AppPlayManager.getInstance().currentTrackItem.name)
    }

    override fun onIdle() {
        tv_name.setText("")
        tv_time.setText("")
        tv_during.setText("")
    }

}