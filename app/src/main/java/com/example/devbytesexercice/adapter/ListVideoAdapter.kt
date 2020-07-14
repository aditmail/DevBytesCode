package com.example.devbytesexercice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.devbytesexercice.R
import com.example.devbytesexercice.databinding.ListVideoItemBinding
import com.example.devbytesexercice.models.Video

class ListVideoAdapter(private val clickListener: OnClickListener) :
    ListAdapter<Video, ListVideoAdapter.ListVideoViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class ListVideoViewHolder(val binding: ListVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onClickListener: OnClickListener, video: Video) {
            binding.videos = video
            binding.executePendingBindings()

            binding.onClickListener = onClickListener
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVideoViewHolder {
        //Error Disini Sebelumnya...
        val binding: ListVideoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_video_item,
            parent, false
        )

        return ListVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListVideoViewHolder, position: Int) {
        val videos = getItem(position)
        holder.bind(clickListener, videos)
    }
}

class OnClickListener(val clickListener: (video: Video) -> Unit) {
    fun onClick(video: Video) = clickListener(video)
}