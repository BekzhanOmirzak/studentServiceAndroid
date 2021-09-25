package com.example.studentservice.util

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar


object Utils {

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun permissionToCreateFile(activity : Activity) {
        val permission = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                1
            );
        }
    }


    fun getPath(uri: Uri,activity: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            activity.contentResolver.query(uri, projection, null, null, null)
                ?: return null
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s: String = cursor.getString(column_index)
        cursor.close()
        return s
    }


}


fun View.snackBar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss();
        }
    }.show();
}

fun ContentResolver.getFileName(uri: Uri): String {
    var name = "";
    val cursor = query(uri, null, null, null, null);
    cursor?.use {
        it.moveToFirst();
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }
    return name;
}