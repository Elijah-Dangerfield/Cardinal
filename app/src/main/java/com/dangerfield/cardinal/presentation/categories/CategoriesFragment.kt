package com.dangerfield.cardinal.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.FragmentCategoriesBinding
import com.dangerfield.cardinal.databinding.FragmentFeedBinding
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.presentation.feed.FeedArticleItemSmall
import com.dangerfield.cardinal.presentation.util.OscillatingScrollListener
import com.dangerfield.cardinal.presentation.util.smoothScrollToPositionWithSpeed
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class CategoriesFragment : Fragment() {

    private val viewModel: CategoriesViewModel by viewModels()
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.rvCategories.apply {
            setHasFixedSize(true)
            adapter = groupAdapter
            smoothScrollToPositionWithSpeed(viewModel.categories.size)
            addOnScrollListener(
                OscillatingScrollListener(resources.getDimensionPixelSize(R.dimen.grid_2))
            )
        }
        groupAdapter.setOnItemClickListener { item, view ->
            val bundle = Bundle()
            (item as? CategoryItem)?.let {
                Toast.makeText(context, "CLICK", Toast.LENGTH_SHORT).show()
            }
        }
        groupAdapter.update(viewModel.categories.map { CategoryItem(it) }.reversed())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
    }
}