package com.fidel.githubuserappfinal.ui.search

import android.annotation.SuppressLint
import android.content.Context
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
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("StaticFieldLeak")
lateinit var mContext: Context

class UserAdapter(private var userListData: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val listViewHolder = ListViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false))
        mContext = viewGroup.context
        return listViewHolder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userList = userListData[position]
        Glide.with(holder.itemView.context)
            .load(userList.avatar)
            .apply(RequestOptions().override(250, 250))
            .into(holder.imgAvatar)
        holder.txtUsername.text = userList.username
        holder.txtName.text = userList.name
        holder.txtCompany.text = userList.company
        holder.txtLocation.text = userList.location
        holder.itemView.setOnClickListener {
            val dataUser = UserData(
                userList.username,
                userList.name,
                userList.avatar,
                userList.company,
                userList.location,
                userList.repository,
                userList.followers,
                userList.following,
                userList.isFav
            )
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
            intent.putExtra(DetailActivity.EXTRA_FAVORITE_USER, dataUser)
            mContext.startActivity(intent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: UserData)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemCount(): Int = userListData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemBinding.bind(itemView)
        var imgAvatar: CircleImageView = binding.imgAvatar
        var txtUsername: TextView = binding.txtUsername
        var txtName: TextView = binding.txtName
        var txtCompany: TextView = binding.txtCompany
        var txtLocation: TextView = binding.txtLocation
    }
}