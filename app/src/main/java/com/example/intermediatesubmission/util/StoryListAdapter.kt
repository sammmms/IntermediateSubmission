package com.example.intermediatesubmission.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intermediatesubmission.R
import com.example.intermediatesubmission.data.response.ListStoryItem
import com.example.intermediatesubmission.databinding.StoryCardBinding
import com.example.intermediatesubmission.ui.detail_story.DetailStoryActivity
import com.example.intermediatesubmission.ui.home.HomeActivity


class StoryListAdapter :
    ListAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var _stories: List<ListStoryItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StoryCardBinding.inflate(layoutInflater, parent, false)

        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(_stories[position])
    }

    override fun getItemCount(): Int {
        return _stories.size
    }

    fun setStories(stories: List<ListStoryItem>) {
        _stories = stories
        val diffCallback = StoryDiffCallback(_stories, stories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: StoryCardBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                tvItemName.text = story.name
                tvItemDescription.text = story.description
                Glide.with(context).load(story.photoUrl).error(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.baseline_broken_image_24
                    )
                ).into(ivItemPhoto)

                cvStory.setOnClickListener {
                    val moveIntent = Intent(context, DetailStoryActivity::class.java)
                    moveIntent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                    startActivity(
                        context,
                        moveIntent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(context as HomeActivity)
                            .toBundle()
                    )
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}