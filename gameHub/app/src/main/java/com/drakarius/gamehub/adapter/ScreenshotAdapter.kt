package com.drakarius.gamehub.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.drakarius.gamehub.R
import com.drakarius.gamehub.databinding.ItemScreenshotBinding
import com.drakarius.gamehub.model.Screenshot

class ScreenshotAdapter(private val screenshots: List<Screenshot>) : RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder>() {
    inner class ScreenshotViewHolder(val binding: ItemScreenshotBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        val binding = ItemScreenshotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScreenshotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        val screenshot = screenshots[position]
        holder.binding.ivScreenshot.load(screenshot.image) {
            placeholder(android.R.drawable.ic_menu_gallery)
            crossfade(true)
            transformations(RoundedCornersTransformation(12f))
        }
        holder.binding.ivScreenshot.setOnClickListener {
            val dialog = Dialog(holder.binding.root.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.setContentView(R.layout.dialog_zoom_image)
            val imageView = dialog.findViewById<ImageView>(R.id.ivZoomedImage)
            imageView.load(screenshot.image) {
                placeholder(android.R.drawable.ic_menu_gallery)
                crossfade(true)
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int = screenshots.size
}
