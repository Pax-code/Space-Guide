package com.release.spaceguide.adapters.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager
import com.release.spaceguide.adapters.PopularImagesCarouselAdapter
import com.release.spaceguide.adapters.adapters.GalaxyAdapter
import com.release.spaceguide.adapters.viewmodel.ExploreViewModel
import com.release.spaceguide.databinding.FragmentExplorePageBinding

class ExplorePage : Fragment() {

    private var _binding : FragmentExplorePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExploreViewModel
    private lateinit var carouselAdapter: PopularImagesCarouselAdapter
    private lateinit var galaxyAdapter: GalaxyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExplorePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ExploreViewModel::class.java]
        viewModel.fetchData()

        carouselAdapter = PopularImagesCarouselAdapter(arrayListOf())
        binding.carouselRecyclerView.layoutManager= CarouselLayoutManager()
        binding.carouselRecyclerView.adapter= carouselAdapter

        viewModel.apiData.observe(viewLifecycleOwner) { link ->
            carouselAdapter.updateList(link)
        }

        viewModel.fetchGalaxiesData()

        galaxyAdapter = GalaxyAdapter(arrayListOf(), arrayListOf(), arrayListOf())
        binding.galaxyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.galaxyRecyclerView.adapter = galaxyAdapter
        viewModel.galaxyURL.observe(viewLifecycleOwner){ galaxyLink ->
            galaxyAdapter.updateGalaxiesList(galaxyLink)
        }
        viewModel.titles.observe(viewLifecycleOwner){ titles ->
            galaxyAdapter.updateGalaxiesTitles(titles)
        }

        viewModel.descriptions.observe(viewLifecycleOwner){ description ->
            galaxyAdapter.updateGalaxiesDescriptions(description)
        }



        binding.favoritesDirectionButton.setOnClickListener {
            val action = ExplorePageDirections.actionExplorePageToFavoritesPage()
            Navigation.findNavController(it).navigate(action)
        }


        binding.exloreButton.setOnClickListener {
            binding.particleAnim.visibility = View.VISIBLE
            binding.particleAnim.playAnimation()

            viewModel.fetchGalaxiesData()

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
