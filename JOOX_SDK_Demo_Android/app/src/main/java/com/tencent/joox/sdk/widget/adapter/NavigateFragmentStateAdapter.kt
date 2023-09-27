package com.tencent.joox.sdk.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tencent.joox.sdk.business.discover.DiscoverFragment
import com.tencent.joox.sdk.business.library.LibraryFragment
import com.tencent.joox.sdk.business.search.SearchLandingFragment

class  NavigateFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val mFragments = arrayOf(
        DiscoverFragment(),
        SearchLandingFragment(),
        LibraryFragment()
    )

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }
}