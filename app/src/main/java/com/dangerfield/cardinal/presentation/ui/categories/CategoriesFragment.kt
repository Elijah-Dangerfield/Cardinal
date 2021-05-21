package com.dangerfield.cardinal.presentation.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.FragmentCategoriesBinding
import com.dangerfield.cardinal.presentation.util.OscillatingScrollListener
import com.dangerfield.cardinal.presentation.util.smoothScrollToPositionWithSpeed
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupView() {
        binding.rvCategories.apply {
            setHasFixedSize(true)
            adapter = groupAdapter
            itemAnimator = null // fixes items shifting around
            smoothScrollToPositionWithSpeed(viewModel.categories.size)
            addOnScrollListener(
                OscillatingScrollListener(resources.getDimensionPixelSize(R.dimen.grid_2))
            )
        }
        groupAdapter.setOnItemClickListener { item, view ->
            (item as? CategoryItem)?.data?.apply {
                toggleSelected()
                if(isSelected) {
                   viewModel.addSelectedCategory(this)
                } else {
                    viewModel.removeSelectedCategory(this)
                }
            }
            item.notifyChanged()
        }

        groupAdapter.update(viewModel.categories.map { CategoryItem(it) }.reversed())

        binding.btnNext.setOnClickListener {
            viewModel.saveCurrentUserCategories()
            findNavController().navigate(R.id.action_categoriesFragment_to_feedFragment)
        }
    }

    private fun setupObservers() {
        viewModel.buttonState.observe(viewLifecycleOwner) { state ->
            binding.btnNext.text = when(state) {
                CategoriesViewModel.ButtonState.Next -> getString(R.string.next)
                CategoriesViewModel.ButtonState.Skip,
                null -> getString(R.string.skip)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}