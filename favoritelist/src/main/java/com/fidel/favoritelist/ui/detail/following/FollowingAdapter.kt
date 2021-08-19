package com.fidel.favoritelist.ui.detail.following

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fidel.favoritelist.R
import com.fidel.favoritelist.databinding.UserItemBinding
import com.fidel.favoritelist.model.UserData
import com.fidel.favoritelist.ui.detail.DetailActivity
import com.fidel.favoritelist.ui.search.mContext
import de.hdodenhof.circleimageview.CircleImageView

var followingList = ArrayList<UserData>()

class FollowingAdapter(listUser: ArrayList<UserData>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    init {
        followingList = listUser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val listViewHolder = ListViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false))
        mContext = viewGroup.context
        return listViewHolder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val followingList = followingList[position]
        Glide.with(holder.itemView.context)
            .load(followingList.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.imgAvatar)
        holder.txtUsername.text = followingList.username
        holder.txtName.text = followingList.name
        holder.txtCompany.text = followingList.company
        holder.txtLocation.text = followingList.location
        holder.itemView.setOnClickListener {
            val dataUser = UserData(
                followingList.username,
                followingList.name,
                followingList.avatar,
                followingList.company,
                followingList.location,
                followingList.repository,
                followingList.followers,
                followingList.following,
                followingList.isFav
            )
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
            intent.putExtra(DetailActivity.EXTRA_FAVORITE_USER, dataUser)
            mContext.startActivity(intent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(UserData: UserData)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemCount(): Int = followingList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemBinding.bind(itemView)
        var imgAvatar: CircleImageView = binding.imgAvatar
        var txtUsername: TextView = binding.txtUsername
        var txtName: TextView = binding.txtName
        var txtCompany: TextView = binding.txtCompany
        var txtLocation: TextView = binding.txtLocation
    }

}