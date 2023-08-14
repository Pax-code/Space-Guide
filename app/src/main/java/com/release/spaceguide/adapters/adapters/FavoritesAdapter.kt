package com.release.spaceguide.adapters.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.release.spaceguide.adapters.data.local.DBModel
import com.release.spaceguide.adapters.downloadFromUrl
import com.release.spaceguide.adapters.placeHolderProgressBar
import com.release.spaceguide.adapters.viewmodel.FavoritesPageViewModel

import com.release.spaceguide.databinding.FavoritesItemLayoutBinding


class FavoritesAdapter(private val list: ArrayList<DBModel>, private val viewModel: FavoritesPageViewModel): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {


    class ViewHolder(val binding: FavoritesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoritesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.favoriteImages.downloadFromUrl(list[position].URL, placeHolderProgressBar(holder.binding.root.context))

        holder.binding.favoriteTitle.text = list[position].title

        holder.binding.favoriteDownloadIcon.setOnClickListener {
            viewModel.downloadImage(holder.binding.root.context, position)
        }

        holder.binding.deleteIcon.setOnClickListener {
            val alert = AlertDialog.Builder(holder.binding.root.context)
            alert.setTitle("Remove From Favorites")
            alert.setMessage("Are you sure about remove this image from favorites?")
            alert.setPositiveButton("Yes"){dialog, _ ->
                viewModel.delete(list[position])
                dialog.dismiss()
            }
            alert.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }
            alert.create().show()
        }

    }

    fun updateList(l: List<DBModel>){
        list.clear()
        list.addAll(l)
        notifyDataSetChanged()
    }
}