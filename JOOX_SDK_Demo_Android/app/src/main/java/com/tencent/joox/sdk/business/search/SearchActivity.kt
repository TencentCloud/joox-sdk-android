package com.tencent.joox.sdk.business.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.gyf.immersionbar.ImmersionBar
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.search.entity.SearchConstant
import com.tencent.joox.sdk.business.search.entity.SearchConstant.KEY_SEARCH
import com.tencent.joox.sdk.business.search.entity.SearchFrom
import com.tencent.joox.sdk.business.search.entity.SearchKey
import com.tencent.joox.sdk.databinding.ActivitySearchBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SearchActivity : FragmentActivity(), View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    companion object {

        private const val TAG = "SearchActivity"
        fun toSearch(ctx: Context, keyword: String? = null) {
            val it = Intent(ctx, SearchActivity::class.java)
            it.putExtra(SearchConstant.KEY_SEARCH, keyword)
            ctx.startActivity(it)
        }
    }

    private lateinit var binding: ActivitySearchBinding
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        initUi()

    }

    override fun onStart() {
        super.onStart()
        fetch()
    }

    private fun initUi() {
        fullScreen()
        binding.toolbarHome.ivToolbarBack.visibility = View.VISIBLE
        binding.toolbarHome.ivToolbarBack.setOnClickListener(this)
        binding.toolbarHome.tvToolbarTitle.text = "Search"
        binding.toolbarHome.ivToolbarMenu.visibility = View.GONE
        binding.searchBar.searchInput.setOnEditorActionListener(this)
        binding.searchBar.searchInput.setOnClickListener(this)
        binding.searchBar.iconDelete.setOnClickListener(this)
        binding.searchBar.searchInput.addTextChangedListener(this)

    }

    private fun fetch() {
        val searchKey = intent.getStringExtra(KEY_SEARCH)
        searchKey?.let {
            binding.searchBar.searchInput.setText(searchKey)
        }
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }

    @Subscribe
    fun onSearchChange(key: SearchKey){
        if (key.from == SearchFrom.HINT_PROMPT){
            binding.searchBar.searchInput.removeTextChangedListener(this)
            binding.searchBar.searchInput.setText(key.word)
            binding.searchBar.searchInput.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d(TAG, "beforeTextChanged:${s}")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //navController.currentDestination?.id
    }

    override fun afterTextChanged(s: Editable?) {
        val keyword = s?.toString() ?: ""
        if (R.id.searchHint != navController.currentDestination?.id) {
            showHint()
        } else {
            EventBus.getDefault().post(SearchKey(keyword, SearchFrom.USER_INPUT))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_toolbar_back -> {
                finish()
            }
            R.id.search_input -> {
                showHint()
            }
            R.id.icon_delete -> {
                binding.searchBar.searchInput.setText("")
                navController.navigate(R.id.searchTips)
            }
        }
    }

    private fun showHint(){
        val bundle = Bundle()
        bundle.putString("KEY_WORD", binding.searchBar.searchInput.text.toString())
        navController.navigate(R.id.searchHint, bundle)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
       when(actionId){
           EditorInfo.IME_ACTION_SEARCH ->{
               val bundle = Bundle().also {bundle ->
                   bundle.putString(SearchConstant.KEY_SEARCH, v?.text.toString())
               }
               navController.navigate(R.id.searchTabs, bundle)
               return true
           }
       }
        return false
    }
}