package com.example.intermediatesubmission.util

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(imageUri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val fileName = "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
    val file = File(context.cacheDir, fileName)
    val inputStream = contentResolver.openInputStream(imageUri)
    file.outputStream().use { fileOutputStream ->
        inputStream?.copyTo(fileOutputStream)
    }
    inputStream?.close()
    return file
}