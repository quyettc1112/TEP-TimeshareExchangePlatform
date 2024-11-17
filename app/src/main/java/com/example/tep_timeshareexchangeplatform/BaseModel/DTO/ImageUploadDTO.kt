package com.example.tep_timeshareexchangeplatform.BaseModel.DTO

import android.net.Uri
import android.provider.MediaStore
import com.example.tep_timeshareexchangeplatform.AppConfig.Application.TEP_TimeshareExhangPlatform
import com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP.ImageUploadModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

data class ImageUploadDTO(val part: MultipartBody.Part) {
    companion object {
        fun create(uri: Uri): ImageUploadDTO {
            val file = File(getPathFromUri(uri)!!)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", file.name, requestFile)
            return ImageUploadDTO(part)
        }

        private fun getPathFromUri(uri: Uri): String? {
            // Thay đổi logic nếu bạn sử dụng ContentResolver hoặc khác phương pháp
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