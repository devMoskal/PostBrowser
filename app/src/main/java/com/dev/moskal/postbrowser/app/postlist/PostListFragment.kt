package com.dev.moskal.postbrowser.app.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.app.MainViewModel
import com.dev.moskal.postbrowser.databinding.PostListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PostListFragment : Fragment() {

    @Inject
    lateinit var postAdapter: PostListAdapter

    private val viewModel: PostListViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private lateinit var binding: PostListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = PostListFragmentBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        handleLoadingErrors()
        observeViewModel()
    }

    private var searchJob: Job? = null

    private fun observeViewModel() {
        viewModel.actions.observe(viewLifecycleOwner) {
            when (it) {
                PostListViewAction.FailedToDeletePost -> showSnackbar(R.string.unable_to_delete_post)
                is PostListViewAction.OnPostSelected -> navigateToPostDetail(it.postId)
                PostListViewAction.FailedToLoadMorePosts -> showSnackbar(R.string.unable_to_load_more_post)
            }
        }

        lifecycleScope.launch {
            viewModel.items.collect {
                searchJob = async {
                    Timber.i("__ as")
                    postAdapter.submitData(it)
                }
            }
        }

    }

    private fun navigateToPostDetail(postId: Int) {
        sharedViewModel.selectPost(postId)
    }

    private fun showSnackbar(@StringRes stringRes: Int) {
        activity?.apply {
            Snackbar.make(
                findViewById(android.R.id.content),
                getString(stringRes),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleLoadingErrors() {
        // refactor: move to viewModel and send Action back to view
        binding.errorView.setOnClickListener { postAdapter.retry() }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = postAdapter
        }
        postAdapter.addLoadStateListener(viewModel::onPagingStateChanged)
    }
}