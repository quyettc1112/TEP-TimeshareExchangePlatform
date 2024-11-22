package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "title": "string",
  "image": "string",
  "content": "string",
  "createdAt": "2024-11-17T15:18:02.730Z",
  "updatedAt": "2024-11-17T15:18:02.730Z",
  "isActive": true
}
*/
data class BlogDetailResponse(
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String
)