package com.tencent.joox.sdk.business.library.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.joox.sdklibrary.kernel.dataModel.UserInfo
import com.tencent.joox.sdk.databinding.LibraryHeaderLayoutBinding


class LibraryHeaderAdapter(val data: UserInfo?) : BaseSingleItemAdapter<UserInfo, LibraryHeaderAdapter.ItemVH>(data) {

    class ItemVH(val viewBinding: LibraryHeaderLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding = LibraryHeaderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, item: UserInfo?) {
        item?.let {
            holder.viewBinding.userName.text = item.nickname
            holder.viewBinding.userDes.text = item.descrip
            holder.viewBinding.userVipLabel.visibility = if (item.isVVip() || item.isVip) View.VISIBLE else View.GONE
            Glide.with(context).load(item.headImgUrl).into(holder.viewBinding.userCoverImg)
        }
    }

    override fun isFullSpanItem(itemType: Int): Boolean {
        return true
    }

}
