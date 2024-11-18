package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Feedback


import com.google.gson.annotations.SerializedName

/**
{
  "content": [
    {
      "id": 1,
      "ratingPoint": 2,
      "comment": "bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 22:28:05",
      "isActive": true
    },
    {
      "id": 10,
      "ratingPoint": 3,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:21",
      "isActive": true
    },
    {
      "id": 12,
      "ratingPoint": 3,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 13,
      "ratingPoint": 2,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 14,
      "ratingPoint": 1,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 15,
      "ratingPoint": 3,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 16,
      "ratingPoint": 3,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 17,
      "ratingPoint": 3,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "01-11-2024 16:38:24",
      "isActive": true
    },
    {
      "id": 18,
      "ratingPoint": 1,
      "comment": "so bad",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 1,
        "fullName": "thanh long",
        "avatar": "string"
      },
      "createdDate": "02-11-2024 12:21:14",
      "isActive": true
    },
    {
      "id": 19,
      "ratingPoint": 5,
      "comment": "Hay lắm 🤘",
      "resort": {
        "id": 1,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang"
      },
      "customer": {
        "id": 6,
        "fullName": "thanhlong",
        "avatar": null
      },
      "createdDate": "15-11-2024 08:29:18",
      "isActive": true
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 17,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
*/
data class FeedbacksResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("last") val last: Boolean,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("id") val id: Int,
        @SerializedName("ratingPoint") val ratingPoint: Int,
        @SerializedName("comment") val comment: String,
        @SerializedName("resort") val resort: Resort,
        @SerializedName("customer") val customer: Customer,
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("isActive") val isActive: Boolean
    ) {
        data class Resort(
            @SerializedName("id") val id: Int,
            @SerializedName("resortName") val resortName: String
        )

        data class Customer(
            @SerializedName("id") val id: Int,
            @SerializedName("fullName") val fullName: String,
            @SerializedName("avatar") val avatar: String?
        )
    }

    data class Pageable(
        @SerializedName("pageNumber") val pageNumber: Int,
        @SerializedName("pageSize") val pageSize: Int,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("offset") val offset: Int,
        @SerializedName("paged") val paged: Boolean,
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