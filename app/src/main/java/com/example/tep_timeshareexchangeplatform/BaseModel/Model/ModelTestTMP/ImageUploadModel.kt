package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import android.net.Uri
import android.provider.MediaStore
import com.example.tep_timeshareexchangeplatform.AppConfig.Application.TEP_TimeshareExhangPlatform
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

data class ImageUploadModel(
    val id: Int,
    val uri: Uri,
    val part: MultipartBody.Part
) {
    companion object {
        private var idCounter = 0
        fun create(uri: Uri): ImageUploadModel {
            val file = File(getPathFromUri(uri)!!)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", file.name, requestFile)
            return ImageUploadModel(idCounter++, uri, part)
        }

        private fun getPathFromUri(uri: Uri): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = TEP_TimeshareExhangPlatform.getContext()
                .contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                    val path = cursor.getString(columnIndex)
                    it.close()
                    return path
                }
            }
            return null
        }
    }
}