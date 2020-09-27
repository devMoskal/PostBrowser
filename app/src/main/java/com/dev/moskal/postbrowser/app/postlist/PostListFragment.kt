package com.dev.moskal.postbrowser.app.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.moskal.postbrowser.databinding.PostListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostListFragment : Fragment() {

    companion object {
        fun newInstance() = PostListFragment()
    }

    @Inject
    lateinit var postAdapter: PostListAdapter

    private val viewModel: PostListViewModel by viewModels()

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
        binding.viewState = viewModel.viewState
        binding.lifecycleOwner = viewLifecycleOwner
    }


    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = postAdapter
        }
    }
}