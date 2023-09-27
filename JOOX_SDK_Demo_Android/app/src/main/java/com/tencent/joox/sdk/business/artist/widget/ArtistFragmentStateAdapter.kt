package com.tencent.joox.sdk.business.artist.widget

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tencent.joox.sdk.business.artist.ArtistAlbumFragment
import com.tencent.joox.sdk.business.artist.ArtistSongFragment
import com.tencent.joox.sdk.business.artist.entity.ArtistHomeConstant

class ArtistFragmentStateAdapter(private val id: String = "", fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object{
        private const val SONG = 1
        private const val ALBUM = 2
    }

    private val tabs = arrayOf(
        SONG,
        ALBUM
    )

    override fun createFragment(position: Int): Fragment {
        var tabFragment = if (getItemViewType(position) == SONG){
            ArtistSongFragment()
        }else{
            ArtistAlbumFragment()
        }
        tabFragment.arguments = Bundle().apply {
            putString(ArtistHomeConstant.KEY_ARTIST_ID, id)
        }
        return tabFragment
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun getItemViewType(position: Int): Int {
        return tabs[position]
    }
}