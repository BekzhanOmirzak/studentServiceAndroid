package com.example.studentservice.util

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path


class ProgressRequestBody(
    val file: File,
    val content_type: String,
    val listener: UploadCallBack

) : RequestBody() {

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 1048;
    }

    interface UploadCallBack {
        fun onProgressUpdate(percentage: Int)
    }

    inner class ProgressUpdate(
        private val uploaded: Long,
        private val total: Long
    ) : Runnable {

        override fun run() {
            listener.onProgressUpdate((uploaded * 100 / total).toInt());
        }
    }


    override fun contentType() = MediaType.parse("$content_type/*");


    override fun writeTo(sink: BufferedSink) {

        val fileLength = file.length();
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE);
        val fileInputStream = FileInputStream(file);
        var uploaded = 0L;

        fileInputStream.use { inputStream ->
            var read: Int;
            val handler = Handler(Looper.getMainLooper());

            while (inputStream.read(buffer).also { read = it } != -1) {
                handler.post(ProgressUpdate(uploaded, fileLength))
                uploaded += read;
                sink.write(buffer, 0, read);
            }

        }

    }


    override fun contentLength(): Long {
        return file.length();
    }


}