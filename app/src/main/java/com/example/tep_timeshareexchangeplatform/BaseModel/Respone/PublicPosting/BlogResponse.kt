package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting


import com.google.gson.annotations.SerializedName

/**
{
  "totalPages": 0,
  "totalElements": 0,
  "size": 0,
  "content": [
    {
      "id": 0,
      "title": "string",
      "image": "string",
      "createdAt": "2024-11-17T15:16:55.831Z",
      "updatedAt": "2024-11-17T15:16:55.831Z",
      "isActive": true
    }
  ],
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "pageable": {
    "offset": 0,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0,
    "unpaged": true
  },
  "numberOfElements": 0,
  "first": true,
  "last": true,
  "empty": true
}
*/
data class BlogResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("number") val number: Int,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("size") val size: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("totalPages") val totalPages: Int
) {
    data class Content(
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("id") val id: Int,
        @SerializedName("image") val image: String,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("title") val title: String,
        @SerializedName("updatedAt") val updatedAt: String
    )

    data class Pageable(
        @SerializedName("offset") val offset: Int,
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("paged") val paged: Boolean,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("unpaged") val unpaged: Boolean
    ) {
        data class Sort(
            @SerializedName("empty") val empty: Boolean,
            @SerializedName("sorted") val sorted: Boolean,
            @SerializedName("unsorted") val unsorted: Boolean
        )
    }

    data class Sort(
        @SerializedName("empty") val empty: Boolean,
        @SerializedName("sorted") val sorted: Boolean,
        @SerializedName("unsorted") val unsorted: Boolean
    )
}