package com.fidel.githubuserappfinal.ui.detail.followers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fidel.githubuserappfinal.R
import com.fidel.githubuserappfinal.databinding.UserItemBinding
import com.fidel.githubuserappfinal.model.UserData
import com.fidel.githubuserappfinal.ui.detail.DetailActivity
import com.fidel.githubuserappfinal.ui.search.mContext
import de.hdodenhof.circleimageview.CircleImageView

var followersList = ArrayList<UserData>()

class FollowersAdapter(listUser: ArrayList<UserData>) : RecyclerView.Adapter<FollowersAdapter.ListViewHolder>() {

    init {
        followersList = listUser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val listViewHolder = ListViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false))
        mContext = viewGroup.context
        return listViewHolder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val followersList = followersList[position]
        Glide.with(holder.itemView.context)
            .load(followersList.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.imgAvatar)
        holder.txtUsername.text = followersList.username
        holder.txtName.text = followersList.name
        holder.txtCompany.text = followersList.company
        holder.txtLocation.text = followersList.location
        holder.itemView.setOnClickListener {
            val dataUser = UserData(
                followersList.username,
                followersList.name,
                followersList.avatar,
                followersList.company,
                followersList.location,
                followersList.repository,
                followersList.followers,
                followersList.following,
                followersList.isFav
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

    override fun getItemCount(): Int = followersList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemBinding.bind(itemView)
        var imgAvatar: CircleImageView = binding.imgAvatar
        var txtUsername: TextView = binding.txtUsername
        var txtName: TextView = binding.txtName
        var txtCompany: TextView = binding.txtCompany
        var txtLocation: TextView = binding.txtLocation
    }

}