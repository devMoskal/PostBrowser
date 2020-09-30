package com.dev.moskal.postbrowser.app.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dev.moskal.postbrowser.app.MainViewModel
import com.dev.moskal.postbrowser.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: DetailsFragmentBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewState = viewModel.viewState
        binding.lifecycleOwner = viewLifecycleOwner
        sharedViewModel.selectedPost.observe(viewLifecycleOwner) {
            viewModel.selectPost(it)
        }
    }
}
