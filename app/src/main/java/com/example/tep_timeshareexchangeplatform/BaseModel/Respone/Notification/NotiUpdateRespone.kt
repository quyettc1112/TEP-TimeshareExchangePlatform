package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Notification


import com.google.gson.annotations.SerializedName

/**
{
  "id": 0,
  "title": "string",
  "content": "string",
  "createdAt": "2024-12-08T15:38:36.383Z",
  "isRead": true,
  "userId": 0,
  "type": "string",
  "role": "string",
  "entityId": 0
}
*/
data class NotiUpdateRespone(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("userId") val userId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("role") val role: String,
    @SerializedName("entityId") val entityId: Int
)