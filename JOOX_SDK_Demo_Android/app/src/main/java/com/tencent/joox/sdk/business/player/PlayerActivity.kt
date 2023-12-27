package com.tencent.joox.sdk.business.player


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.gyf.immersionbar.ImmersionBar
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.kernel.dataModel.BaseSongInfo
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity(), AppPlayManager.PlayerListener, View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {


    companion object {
        fun startPlay(ctx: Context) {
            val intent = Intent(ctx, PlayerActivity::class.java)
            if (ctx !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            ctx.startActivity(intent)
        }
    }

    private var handler: Handler = Handler()
    private var isSeeking: Boolean = false
    private lateinit var binding: ActivityPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        fullScreen()
        binding.toolbarPlayer.ivToolbarBack.visibility = View.VISIBLE
        binding.toolbarPlayer.ivToolbarBack.setOnClickListener(this)

        binding.start.setOnClickListener(this)
        binding.seekBar.setOnSeekBarChangeListener(this)
        binding.pre.setOnClickListener(this)
        binding.next.setOnClickListener(this)
        binding.songQualityLabel.setOnClickListener(this)
        binding.playLike.setOnClickListener(this)
        AppPlayManager.getInstance().addPlayerListeners(this)
        refreshSongInfo()
    }

    private fun refreshSongInfo() {
        AppPlayManager.getInstance().currentTrackItem?.let {
            refreshVip(it)
            refreshCover(it)
            refreshSongIntroduction(it)
            refreshSubscribeStatus(it.id)
            refreshQuality()
            refreshPlayStatus()
            refreshPlayMode()
        }
    }

    private fun refreshVip(it: TrackItem) {
        binding.songVipLabel.visibility = if (it.vip_flag == 1) View.VISIBLE else View.GONE
    }

    private fun refreshPlayMode() {
        if (SDKInstance.getIns().userManager?.userInfo?.isVip == true) {
            binding.playMode.setOnClickListener(this)
            binding.playMode.alpha = 1.0f
        } else {
            binding.playMode.alpha = 0.6f
        }
        when (AppPlayManager.getInstance().playMode) {
            PlayMode.SHUFFLE -> {
                binding.playMode.setImageResource(R.drawable.icon_shuffle_play)
            }
            PlayMode.SINGLE -> {
                binding.playMode.setImageResource(R.drawable.icon_single_play)
            }
            PlayMode.LOOP -> {
                binding.playMode.setImageResource(R.drawable.icon_order_play)
            }
        }

    }


    private fun refreshPlayStatus() {
        if (AppPlayManager.getInstance().isPlaying) {
            binding.start.setImageResource(R.drawable.icon_pause)
        } else {
            binding.start.setImageResource(R.drawable.icon_play)
        }
    }

    private fun refreshSubscribeStatus(songId: String) {
        if (SubscribeManager.isSongSubscribed(songId)) {
            binding.playLike.setImageResource(R.drawable.icon_like)
        } else {
            binding.playLike.setImageResource(R.drawable.icon_like_idle)
        }
    }

    private fun refreshSongIntroduction(trackItem: TrackItem) {
        binding.songName.text = trackItem.name
        val artistNames = generateArtistsName(trackItem)
        binding.artistName.text = artistNames
    }

    private fun refreshCover(trackItem: TrackItem) {
        Glide.with(this).asBitmap().load(trackItem.images?.first()?.url).placeholder(R.drawable.bg_default_cover)
                .into(object : BitmapImageViewTarget(binding.songCoverImg) {
                    override fun setResource(resource: Bitmap?) {
                        super.setResource(resource)
                        resource?.let { cover ->
                            Palette.from(cover).maximumColorCount(10).generate { palette ->
                                palette?.vibrantSwatch?.let { vibrantSwatch ->
                                    binding.playerRoot.setBackgroundColor(vibrantSwatch.rgb)
                                }
                            }
                        }
                    }
                })
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }

    private fun refreshQuality() {
        when (SDKInstance.getIns()?.songInfo?.quality) {
            BaseSongInfo.TYPE_48K -> {
                binding.songQualityLabel.text = "Std"
            }
            BaseSongInfo.TYPE_96K -> {
                binding.songQualityLabel.text = "Mid"
            }
            BaseSongInfo.TYPE_320K -> {
                binding.songQualityLabel.text = "HQ"
            }
            BaseSongInfo.TYPE_FLACHIFI -> {
                binding.songQualityLabel.text = "Hi-Fi"
            }
            else -> {
                binding.songQualityLabel.text = "Std"
            }
        }
    }

    private fun seek() {
        Log.d("test", " seek " + binding.seekBar.progress.toString())
        SDKInstance.getIns().seekTo(binding.seekBar.progress.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        AppPlayManager.getInstance().unInit()
    }


    private fun formatSeconds(mins: Long): String {
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

    override fun onPlayLoading(isLoading: Boolean) {
    }


    override fun onPlayProgress(progress: Long, during: Long) {
        if (!isSeeking) {
            binding.seekBar.progress = progress.toInt()
            binding.seekBar.max = during.toInt()
            binding.tvTime.text = formatSeconds(progress)
            binding.tvDuring.text = formatSeconds(during)
        }
    }

    override fun onSongListChange() {

    }

    override fun onPlayStart() {
        super.onStart()
        binding.start.setImageResource(R.drawable.icon_pause)
        refreshQuality()
    }

    override fun onPlayPause() {
        super.onPause()
        binding.start.setImageResource(R.drawable.icon_play)
    }

    override fun onPlayStop() {
        super.onStop()
        binding.start.setImageResource(R.drawable.icon_play)

    }

    override fun onPlayError(code: Int) {
        binding.start.setImageResource(R.drawable.icon_play)

    }

    override fun onPlaySongChange() {
        refreshSongInfo()
    }

    override fun onIdle() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_toolbar_back -> {
                finish()
            }
            R.id.start -> {
                start()
            }
            R.id.pre -> {
                AppPlayManager.getInstance().pre()
            }
            R.id.next -> {
                AppPlayManager.getInstance().next()
            }
            R.id.song_quality_label -> {
                selectQuality()
            }
            R.id.play_like -> {
                doSubscribe()
            }
            R.id.play_mode -> {
                changePlayMode()
            }
        }
    }

    private fun changePlayMode() {
        when (AppPlayManager.getInstance().playMode) {
            PlayMode.SHUFFLE -> {
                AppPlayManager.getInstance().playMode = PlayMode.LOOP
            }
            PlayMode.SINGLE -> {
                AppPlayManager.getInstance().playMode = PlayMode.SHUFFLE
            }
            PlayMode.LOOP -> {
                AppPlayManager.getInstance().playMode = PlayMode.SINGLE
            }
        }
        refreshPlayMode()
    }

    private fun doSubscribe() {
        AppPlayManager.getInstance().currentTrackItem?.let {
            val isSubscribed = SubscribeManager.isSongSubscribed(it.id)
            SubscribeManager.doLikeSong(!isSubscribed, it.id) { success, status ->
                if (success) {
                    refreshSubscribeStatus(it.id)
                }
            }
        }
    }

    private fun selectQuality() {
        generateQualityList().let {
            val dialog = AlertDialog.Builder(this).setItems(
                it
            ) { _, which ->
                if (TextUtils.equals(it[which], "Std")) {
                    AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_48K
                } else if (TextUtils.equals(it[which], "Med")) {
                    AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_96K
                } else if (TextUtils.equals(it[which], "HQ")) {
                    AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_320K
                } else {
                    AppPlayManager.getInstance().quality = BaseSongInfo.TYPE_FLACHIFI

                }
                refreshQuality()
            }
            dialog.show()
        }

    }

    private fun generateQualityList(): Array<String> {
        var items: Array<String> = emptyArray()
        SDKInstance.getIns()?.songInfo?.qualityList?.let {
            it.forEach { quality ->
                if (quality == BaseSongInfo.TYPE_48K) {
                    items = items.plus("Std")
                }
                if (quality == BaseSongInfo.TYPE_96K) {
                    items = items.plus("Med")
                }
                if (quality == BaseSongInfo.TYPE_320K) {
                    items = items.plus("HQ")
                }
                if (quality == BaseSongInfo.TYPE_FLACHIFI) {
                    items = items.plus("Hi-Fi")
                }
            }
        }
        return items
    }

    private fun start() {
        if (AppPlayManager.getInstance().isPreparing) {
            Toast.makeText(PlayerActivity@ this, "正在准备，稍后操作", Toast.LENGTH_SHORT).show()
        } else if (!AppPlayManager.getInstance().isPrepared) {
            AppPlayManager.getInstance().play()
        } else if (!AppPlayManager.getInstance().isPlaying) {
            AppPlayManager.getInstance().resumePlay()
            binding.start.setImageResource(R.drawable.icon_pause)
        } else if (AppPlayManager.getInstance().isPlaying) {
            AppPlayManager.getInstance().pausePlay()
            binding.start.setImageResource(R.drawable.icon_play)
        }
    }

    private fun generateArtistsName(item: TrackItem): String {
        val artistNames = arrayListOf<String>()
        item.artist_list?.forEach { artistListBean ->
            artistNames.add(artistListBean.name)
        }
        return artistNames.joinToString(",")
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        isSeeking = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        isSeeking = false
        seek()
    }

}