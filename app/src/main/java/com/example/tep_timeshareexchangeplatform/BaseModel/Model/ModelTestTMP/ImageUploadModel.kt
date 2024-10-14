package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import android.net.Uri

data class ImageUploadModel(
    val id: Int,
    val uri: Uri
) {
    companion object {
        private var idCounter = 0
        fun create(uri: Uri): ImageUploadModel {
            return ImageUploadModel(idCounter++, uri)
        }
    }
}