package com.tencent.joox.sdk.business.search.widget

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tencent.joox.sdk.business.search.SearchAllFragment
import com.tencent.joox.sdk.business.search.SearchTabFragment
import com.tencent.joox.sdk.business.search.entity.SearchConstant
import com.tencent.joox.sdk.business.search.entity.SearchTab

class SearchFragmentStateAdapter(private val searchKey: String = "", fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val tabs = arrayOf(
        SearchTab.ALL,
        SearchTab.SONG,
        SearchTab.ARTIST,
        SearchTab.ALBUM,
        SearchTab.PLAYLIST
    )

    override fun createFragment(position: Int): Fragment {
        var searchTabFragment: Fragment?
        if (getItemViewType(position) == SearchTab.ALL.type){
            searchTabFragment = SearchAllFragment()
        }else{
            searchTabFragment = SearchTabFragment()
        }
        searchTabFragment.arguments = Bundle().apply {
            putString(SearchConstant.KEY_SEARCH, searchKey)
            putSerializable(SearchConstant.KEY_SEARCH_TYPE, tabs[position])
        }
        return searchTabFragment
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun getItemViewType(position: Int): Int {
        return tabs[position].type
    }
}