package com.dev.moskal.postbrowser.app.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.moskal.postbrowser.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PostListFragment : Fragment() {

    companion object {
        fun newInstance() = PostListFragment()
    }

    private val viewModel: PostListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.postList.observe(viewLifecycleOwner) {
            Timber.i("### $it")
        }

        return inflater.inflate(R.layout.main_fragment, container, false)
    }
}