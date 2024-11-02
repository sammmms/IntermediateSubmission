package com.example.intermediatesubmission.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.intermediatesubmission.R
import com.example.intermediatesubmission.data.response.StoryResponse
import com.example.intermediatesubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()
    private val apiService = ApiConfig.getApiService(context)
    override fun onCreate() {
        // Do nothing
    }

    override fun onDataSetChanged() {
        Log.d("Widget", "onDataSetChanged")
        val client = apiService.getAllStory()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory
                    stories?.forEach {
                        Glide.with(context)
                            .asBitmap()
                            .load(it?.photoUrl)
                            .into(object :
                                com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                                ) {
                                    mWidgetItems.add(resource)
                                }

                                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                                    // Do nothing
                                }
                            })
                    }
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("Widget", "onFailure: ${t.message}")
            }
        })

    }

    override fun onDestroy() {
        // Do nothing
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews("com.example.intermediatesubmission", R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        return rv
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews("com.example.intermediatesubmission", R.layout.widget_item)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}