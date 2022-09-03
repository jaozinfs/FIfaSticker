package com.example.stickerapp.presenter.extensions

import android.content.ContentValues
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


fun FragmentActivity.addPictureToGallery(uri: Uri?) {
    MediaScannerConnection.scanFile(
        this, arrayOf(uri?.path),
        null, null
    )
}

fun createFile(imagesDir: File, fileName: String) = File(imagesDir, fileName)

fun getExternalStorageImagesDir(): File =
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

@RequiresApi(Build.VERSION_CODES.Q)
fun createImageContentValues(fileName: String) = ContentValues().apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
}

internal fun FragmentActivity.saveImage(
    fileUri: Uri,
    name: String,
    onSuccess: (Uri?) -> Unit = {}
): Uri? = try {
    val fileName = "$name.png"
    var outputStream: OutputStream? = null
    var uri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentResolver?.also { resolver ->
            resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                createImageContentValues(fileName)
            ).also {
                uri = it
                outputStream = it?.let { resolver.openOutputStream(it) }
            }
        }
    } else {
        val imagesDir = getExternalStorageImagesDir()

        createFile(imagesDir, fileName).also {
            uri = it.toUri().fromFileProvider(this)
            outputStream = FileOutputStream(it)
        }
        addPictureToGallery(uri)
    }
    val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
    val drawable = Drawable.createFromStream(inputStream, fileUri.toString())
    outputStream.compressJpgBitmap(drawable.toBitmap())
    onSuccess.invoke(uri)
    uri
} catch (error: Exception) {
    null
}