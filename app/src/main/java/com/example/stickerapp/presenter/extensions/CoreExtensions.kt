package com.example.stickerapp.presenter.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.OutputStream

fun Uri.fromFileProvider(context: Context): Uri? = FileProvider.getUriForFile(
    context,
    context.applicationContext.packageName + ".provider",
    File(path)
)

fun OutputStream?.compressJpgBitmap(bitmap: Bitmap) = use {
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
}