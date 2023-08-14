package com.release.spaceguide.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.release.spaceguide.R
import com.release.spaceguide.adapters.downloadFromUrl
import com.release.spaceguide.adapters.placeHolderProgressBar

import com.release.spaceguide.databinding.CarouselItemLayoutBinding


class PopularImagesCarouselAdapter(private val list: ArrayList<String>): RecyclerView.Adapter<PopularImagesCarouselAdapter.ViewHolder>() {

    class ViewHolder(val binding: CarouselItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CarouselItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.carouselImageView.downloadFromUrl(list[position], placeHolderProgressBar(holder.binding.root.context))

        holder.binding.carouselImageView.setOnClickListener {
            val motionLayout = holder.binding.expandImageAnim
            if (motionLayout.currentState == R.id.start) {
                motionLayout.transitionToEnd()
            } else {
                motionLayout.transitionToStart()
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size

    }


    fun updateList(l: List<String>) {
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }
}