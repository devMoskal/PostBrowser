package com.dev.moskal.postbrowser.app.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.app.MainViewModel
import com.dev.moskal.postbrowser.databinding.PostListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
            clickListener = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        lifecycleScope.launch {
            viewModel.items.collect {
                postAdapter.submitData(it)
            }
        }
        observeActions()
        handleLoadingErrors()
    }

    private fun observeActions() {
        viewModel.actions.observe(viewLifecycleOwner) {
            when (it) {
                PostListViewAction.FailedToDeleteAction -> showSnackbar(R.string.unable_to_delete_post)
                is PostListViewAction.OnPostSelected -> navigateToPostDetail(it.postId)
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
        binding.apply {
            postAdapter.addLoadStateListener { loadState ->
                /*
                 * note: that is one of the reason I am not a fan of Paging library:
                 * loading state and errors are part of adapter
                 * making more and more code going int fragment,
                 * instead of using clear viewState pattern from viewModel as a single source of truth
                 */
                errorView.isVisible = loadState.source.refresh is LoadState.Error
                val uncriticalError = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                uncriticalError?.let {
                    showSnackbar(R.string.unable_to_load_more_post)
                }
            }
            errorView.setOnClickListener { postAdapter.retry() }
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = postAdapter
        }
    }
}