package com.tencent.joox.sdk.business.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.databinding.SearchHintLayoutBinding
import com.tencent.joox.sdk.databinding.SearchTipLayoutBinding

class SearchTipFragment: Fragment() {

    private lateinit var binding: SearchTipLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SearchTipLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}