package com.tencent.joox.sdk.business.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tencent.joox.sdk.business.search.entity.SearchConstant.KEY_SEARCH
import com.tencent.joox.sdk.business.search.widget.SearchFragmentStateAdapter
import com.tencent.joox.sdk.databinding.SearchTabListLayoutBinding

class SearchTabListFragment: Fragment() {

    companion object{
        private val TITLE_ARRAY = arrayOf("All", "Song","Artist","Album","PlayList")
    }

    private lateinit var binding: SearchTabListLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SearchTabListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi(){
        val word = arguments?.getString(KEY_SEARCH) ?:""
        binding.searchTabContainer.adapter = SearchFragmentStateAdapter(word, requireActivity())
        TabLayoutMediator(binding.searchTabBar, binding.searchTabContainer) { tab, position ->
            tab.text = TITLE_ARRAY[position]
        }.attach()
    }


}