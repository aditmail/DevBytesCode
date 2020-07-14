package com.example.devbytesexercice.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.devbytesexercice.R
import com.example.devbytesexercice.database.DatabaseProfile
import com.example.devbytesexercice.databinding.ListProfileItemBinding

class ListProfileAdapter(private val clickProfile: OnClickProfile) :
    ListAdapter<DatabaseProfile, ListProfileAdapter.ProfileViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<DatabaseProfile>() {
        override fun areItemsTheSame(oldItem: DatabaseProfile, newItem: DatabaseProfile): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DatabaseProfile,
            newItem: DatabaseProfile
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ProfileViewHolder(val binding: ListProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickProfile: OnClickProfile, profile: DatabaseProfile) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding: ListProfileItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_profile_item,
            parent, false
        )

        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profiles = getItem(position)
        holder.bind(clickProfile, profiles)
    }
}

class OnClickProfile(val clickListener: (profile: DatabaseProfile) -> Unit) {
    fun onClick(profile: DatabaseProfile) = clickListener(profile)
}