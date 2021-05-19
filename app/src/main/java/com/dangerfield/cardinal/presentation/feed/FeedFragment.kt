package com.dangerfield.cardinal.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.FragmentFeedBinding
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.domain.util.GenericError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val viewModel : FeedViewModel by viewModels()

    private lateinit var binding : FragmentFeedBinding
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.feed.observe(viewLifecycleOwner, {
            updateFeed(it)
        })

        viewModel.feedLoading.observe(viewLifecycleOwner, {
            showFeedLoading(it)
        })

        viewModel.feedError.observe(viewLifecycleOwner, {
            handleError(it)
        })
    }

    private fun handleError(it: GenericError) {
        Toast.makeText(context, "Got an error", Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {
        setupRecyclerView()
        setupRefresher()
    }

    private fun setupRecyclerView() {
        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(view?.context)
        binding.articlesRecyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            val bundle = Bundle()
            (item as? FeedArticleItemSmall)?.let {
               Toast.makeText(context, "CLICK",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRefresher() {
        binding.swipeRefreshLayout.setColorSchemeResources( R.color.black, android.R.color.holo_blue_light
            , android.R.color.holo_blue_dark)

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.getFeed(forceRefresh = true) }
    }

    private fun updateFeed(articles: List<Article>) {
        val views = articles.map {
           if((0..10).random() > 6) {
               FeedArticleItemLarge(it)
           }else {
               FeedArticleItemSmall(it)
           }
        }
        adapter.update(views)
    }

    private fun showFeedLoading(loading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = loading
    }

}