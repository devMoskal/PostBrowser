package com.dev.moskal.postbrowser.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.app.postlist.PostListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        title = resources.getString(R.string.post_list_label)
        if (savedInstanceState == null) {
            viewModel //invoke viewModel lazy initialization
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostListFragment.newInstance())
                .commitNow()
        }
    }
}