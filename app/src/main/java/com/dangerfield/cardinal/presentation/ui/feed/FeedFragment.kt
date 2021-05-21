package com.dangerfield.cardinal.presentation.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.FragmentFeedBinding
import com.dangerfield.cardinal.domain.util.GenericError
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity
import com.dangerfield.cardinal.presentation.model.DisplaySize
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.feed.observe(viewLifecycleOwner, {
            updateFeed(it.first, it.second)
        })
        viewModel.feedLoading.observe(viewLifecycleOwner, this::showFeedLoading)
        viewModel.feedError.observe(viewLifecycleOwner, this::handleError)
    }

    private fun handleError(it: GenericError) {
        Toast.makeText(context, "Got an error", Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {
        setupRecyclerView()
        setupRefresher()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.icSearch.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_searchFragment)
        }
        binding.icSettings.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_settingsFragment)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            val bundle = Bundle()
            (item as? FeedArticleItem)?.let {
                findNavController().navigate(R.id.action_feedFragment_to_detailsFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.articlesRecyclerView.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = groupAdapter
        }
    }

    private fun setupRefresher() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.black, android.R.color.holo_blue_light, android.R.color.holo_blue_dark
        )

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getFeed(forceRefresh = true) }
    }

    private fun updateFeed(articles: List<ArticlePresentationEntity>, shouldAnimateChange: Boolean) {
        val views = articles.map {
            when(it.displaySize) {
                DisplaySize.Large -> FeedArticleItemLarge(it)
                DisplaySize.Small -> FeedArticleItemSmall(it)
            }
        }
        groupAdapter.update(views)
        if(shouldAnimateChange) {
            binding.articlesRecyclerView.scheduleLayoutAnimation()
        }
    }

    private fun showFeedLoading(loading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}