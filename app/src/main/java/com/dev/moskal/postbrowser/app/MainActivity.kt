package com.dev.moskal.postbrowser.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.app.postlist.PostListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostListFragment.newInstance())
                .commitNow()
        }
    }
}