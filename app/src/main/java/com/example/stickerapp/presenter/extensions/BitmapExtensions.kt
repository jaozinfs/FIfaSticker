package com.example.stickerapp.presenter.extensions

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.FileOutputStream
import java.io.InputStream


@RequiresApi(Build.VERSION_CODES.O)
fun Uri.toBitmap(context: Context) : Bitmap {
    val picture = context.createFileFromContentUri(this)
    val options = BitmapFactory.Options()
    options.inSampleSize = 2
    options.inScaled = true
    return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picture.absolutePath, options), 100, 150, true)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createFileFromContentUri(fileUri : Uri) : File{
    var fileName : String = ""

    fileUri.let { returnUri ->
       contentResolver.query(returnUri,null,null,null)
    }?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }

    //  For extract file mimeType
    val fileType: String? = fileUri.let { returnUri ->
       contentResolver.getType(returnUri)
    }

    val iStream : InputStream = contentResolver.openInputStream(fileUri)!!
    val outputDir : File = cacheDir!!
    val outputFile : File = File(outputDir,fileName)
    copyStreamToFile(iStream, outputFile)
    iStream.close()
    return  outputFile
}

fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(1 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}