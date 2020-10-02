package com.dev.moskal.postbrowser.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.app.postdetail.DetailsFragment
import com.dev.moskal.postbrowser.app.postlist.PostListFragment
import com.dev.moskal.postbrowser.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var isInSinglePaneMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        isInSinglePaneMode = resources.getBoolean(R.bool.isInSinglePaneConfiguration)
        title = resources.getString(R.string.post_list_label)

        if (isInSinglePaneMode) {
            viewModel.isPostSelected.observe(this) {
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(it)
                    setDisplayShowHomeEnabled(it)
                }
            }
        }

        if (savedInstanceState == null) {
            PostListFragment() addTo R.id.master
            DetailsFragment() addTo R.id.detail
        }
    }

    override fun onBackPressed() {
        if (isInSinglePaneMode && viewModel.isPostSelected.value == true) {
            viewModel.unselectPost()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    private fun setupBinding() {
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).apply {
            isPostSelected = viewModel.isPostSelected
            lifecycleOwner = this@MainActivity
        }
    }

    private infix fun Fragment.addTo(@IdRes containerId: Int) {
        supportFragmentManager.beginTransaction()
            .add(containerId, this)
            .commit()
    }
}