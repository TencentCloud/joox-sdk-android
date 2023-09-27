package com.tencent.joox.sdk.business.songlist.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListEntity
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.databinding.MusicPlayListListHeaderLayoutBinding


class MusicPlayListHeaderAdapter(data: MusicPlayListEntity? = null) : BaseSingleItemAdapter<MusicPlayListEntity, MusicPlayListHeaderAdapter.ItemVH>(data) {


    companion object {
        private const val HEAD_VIEWTYPE = 1
    }

    class ItemVH(val viewBinding: MusicPlayListListHeaderLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding = MusicPlayListListHeaderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, item: MusicPlayListEntity?) {
        item?.let {
            holder.viewBinding.songListTitle.text = item.name
            holder.viewBinding.songListSinger.text = generateArtistsName(item)
            holder.viewBinding.songVipLabel.visibility = if (item.vipFlag == 1) View.VISIBLE else View.GONE
            holder.viewBinding.songListPlayAll.visibility = View.VISIBLE
            Glide.with(context).load(item.coverList?.first()?.url).into(holder.viewBinding.songListCoverImg)
            refreshSubscribeStatus(holder, SubscribeManager.isMusicPlayListSubscribed(item.id ?:""))
            holder.viewBinding.songListLike.setOnClickListener {
                val isSubscribed = SubscribeManager.isMusicPlayListSubscribed(item.id ?:"")
                SubscribeManager.doLikeMusicPlayList(!isSubscribed,   item.subId ?:"", item.id ?:"",item.listType){success, status ->
                    if (success){
                        if (status == 1){
                            holder.viewBinding.songListLike.setImageResource(R.drawable.icon_like)
                        }else{
                            holder.viewBinding.songListLike.setImageResource(R.drawable.icon_like_idle)
                        }
                    }
                }
            }
        }
    }

    private fun refreshSubscribeStatus(holder: ItemVH, isSubscribed:Boolean){
        holder.viewBinding.songListLike.visibility = View.VISIBLE
        if (isSubscribed){
            holder.viewBinding.songListLike.setImageResource(R.drawable.icon_like)
        }else{
            holder.viewBinding.songListLike.setImageResource(R.drawable.icon_like_idle)
        }
    }

    private fun generateArtistsName(item: MusicPlayListEntity):String{
        val artistNames = arrayListOf<String>()
        item.artistList?.forEach {artistListBean ->
            artistNames.add(artistListBean.name)
        }
        return artistNames.joinToString(",") ?:""
    }


    override fun getItemViewType(position: Int, list: List<Any?>): Int {
        return HEAD_VIEWTYPE
    }
}
