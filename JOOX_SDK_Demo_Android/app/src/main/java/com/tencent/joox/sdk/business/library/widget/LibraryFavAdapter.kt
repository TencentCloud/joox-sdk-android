package com.tencent.joox.sdk.business.library.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.joox.sdklibrary.kernel.dataModel.UserInfo
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.MusicPlayListViewModel
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListEntity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.databinding.LibraryFavLayoutBinding
import com.tencent.joox.sdk.databinding.LibraryHeaderLayoutBinding


class LibraryFavAdapter(data: Int) : BaseSingleItemAdapter<Int, LibraryFavAdapter.ItemVH>(data) {


    class ItemVH(val viewBinding: LibraryFavLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding = LibraryFavLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, data: Int?) {

        holder.viewBinding.favView.setOnClickListener {
            MusicPlayListActivity.toSongList(context, MusicPlayListType.USER_FAV_PLAYLIST.type, "201")
        }
    }

    override fun isFullSpanItem(itemType: Int): Boolean {
        return true
    }

}
