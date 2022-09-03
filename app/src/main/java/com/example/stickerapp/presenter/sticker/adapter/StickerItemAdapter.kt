package com.example.stickerapp.presenter.sticker.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fifastickerapp.R
import com.example.stickerapp.presenter.extensions.toBitmap


class StickerItemAdapter(
    private val prefix: String,
    private val onStickerSelected: (Pair<Int, Boolean>) -> Unit = {},
    private val addImageBackground: (Pair<Int, Boolean>) -> Unit = {}
) :
    ListAdapter<Pair<Pair<Int, Boolean>, String?>, StickerItemAdapter.StickerItemViewHolder>(
        StickerItemDiffUtils
    ) {
    val bitmaps: HashMap<Int, Bitmap> = HashMap(21)

    override fun onViewRecycled(holder: StickerItemViewHolder) {
        super.onViewRecycled(holder)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerItemViewHolder {
        return StickerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_sticker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StickerItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StickerItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val stickerItemText = view.findViewById<TextView>(R.id.sticker_item_text)
        private val stickerState = view.findViewById<CardView>(R.id.sticker_container)
        private val stickerImage = view.findViewById<ImageView>(R.id.sticker_image)
        private val addBackgroundFromGallery =
            view.findViewById<ImageView>(R.id.ic_gallery_image_bg)

        fun bind(sticker: Pair<Pair<Int, Boolean>, String?>) {
            updateData(sticker)
            setListeners(sticker)
            setBackground(sticker)
        }

        private fun updateData(sticker: Pair<Pair<Int, Boolean>, String?>) {
            addBackgroundFromGallery.isVisible = sticker.second == null
            with(stickerItemText) {
                text = "$prefix ${sticker.first.first}"
                setTextColor(
                    if (sticker.second != null)
                        Color.WHITE
                    else
                        Color.GRAY
                )
            }
        }

        private fun setListeners(sticker: Pair<Pair<Int, Boolean>, String?>) {
            stickerState.setOnClickListener { onStickerSelected.invoke(sticker.first) }
            addBackgroundFromGallery.setOnClickListener { addImageBackground.invoke(sticker.first) }
        }

        private fun setBackground(sticker: Pair<Pair<Int, Boolean>, String?>) {
            if (sticker.first.second) {
                when {
                    sticker.second != null -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(bitmaps[adapterPosition] == null) {
                            val bitmap = sticker.second?.toUri()?.toBitmap(view.context)
                            stickerImage.setImageBitmap(bitmap)
                            bitmaps[adapterPosition] =  bitmap!!
                        }else {
                            stickerImage.setImageBitmap(bitmaps[adapterPosition])
                        }
                    }
                    else -> stickerState.setCardBackgroundColor(Color.GREEN)
                }
            } else {
                stickerState.setCardBackgroundColor(Color.WHITE)
                stickerImage.setImageBitmap(null)
            }
        }
    }
}

private object StickerItemDiffUtils : DiffUtil.ItemCallback<Pair<Pair<Int, Boolean>, String?>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Pair<Int, Boolean>, String?>,
        newItem: Pair<Pair<Int, Boolean>, String?>
    ): Boolean = true

    override fun areContentsTheSame(
        oldItem: Pair<Pair<Int, Boolean>, String?>,
        newItem: Pair<Pair<Int, Boolean>, String?>
    ): Boolean = oldItem == newItem
}