package com.release.spaceguide.adapters.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.release.spaceguide.adapters.adapters.FavoritesAdapter
import com.release.spaceguide.adapters.viewmodel.FavoritesPageViewModel
import com.release.spaceguide.databinding.FragmentFavoritesPageBinding


class FavoritesPage : Fragment() {

    private var _binding: FragmentFavoritesPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoritesPageViewModel
    private lateinit var favoritesRecyclerAdapter: FavoritesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FavoritesPageViewModel::class.java]
        favoritesRecyclerAdapter = FavoritesAdapter(arrayListOf(), viewModel)
        binding.favoritesRecyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        binding.favoritesRecyclerView.adapter = favoritesRecyclerAdapter
        viewModel.apods.observe(viewLifecycleOwner, Observer{
            favoritesRecyclerAdapter.updateList(it)
        })

        binding.exploreDirectionButton.setOnClickListener {
            val action = FavoritesPageDirections.actionFavoritesPageToExplorePage()
            Navigation.findNavController(it).navigate(action)
        }
    }



override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}