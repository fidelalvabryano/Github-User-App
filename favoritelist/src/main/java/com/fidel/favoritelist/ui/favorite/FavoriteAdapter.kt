package com.fidel.favoritelist.ui.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fidel.favoritelist.R
import com.fidel.favoritelist.databinding.UserItemBinding
import com.fidel.favoritelist.model.Favorite
import com.fidel.favoritelist.ui.detail.DetailActivity
import com.fidel.favoritelist.util.CustomOnItemClickListener
import java.util.*

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    fun addItem(favorite: Favorite) {
        this.listFavorite.add(favorite)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun updateItem(position: Int, favorite: Favorite) {
        this.listFavorite[position] = favorite
        notifyItemChanged(position, favorite)
    }

    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemBinding.bind(itemView)
        fun bind(favorite: Favorite) {
            Glide.with(itemView.context)
                .load(favorite.avatar)
                .apply(RequestOptions().override(250, 250))
                .into(binding.imgAvatar)
            binding.txtUsername.text = favorite.username
            binding.txtName.text = favorite.name
            binding.txtCompany.text = favorite.company
            binding.txtLocation.text = favorite.location
            itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                            intent.putExtra(DetailActivity.EXTRA_FAVORITE_LIST, favorite)
                            activity.startActivity(intent)
                        }
                    }
                )
            )
        }
    }
}