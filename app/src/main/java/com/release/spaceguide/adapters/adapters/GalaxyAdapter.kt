package com.release.spaceguide.adapters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.release.spaceguide.adapters.downloadFromUrl
import com.release.spaceguide.adapters.placeHolderProgressBar
import com.release.spaceguide.databinding.FlipCardItemLayoutBinding


class GalaxyAdapter(private val links: ArrayList<String>, private val titles: ArrayList<String>,private val description: ArrayList<String>): RecyclerView.Adapter<GalaxyAdapter.ViewHolder>() {

    class ViewHolder(val binding: FlipCardItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FlipCardItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.image.downloadFromUrl(links[position], placeHolderProgressBar(holder.binding.root.context))
        holder.binding.centerText.text = titles[position]
        holder.binding.titleText.text = description[position]

    }


    override fun getItemCount(): Int {
        return links.size

    }


    fun updateGalaxiesList(l: List<String>) {
        links.clear()
        links.addAll(l)
        notifyDataSetChanged()
    }

    fun updateGalaxiesTitles(t: List<String>) {
        titles.clear()
        titles.addAll(t)

        notifyDataSetChanged()
    }

    fun updateGalaxiesDescriptions(d: List<String>) {
        description.clear()
        description.addAll(d)

        notifyDataSetChanged()
    }
}
