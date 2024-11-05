package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
        "timeShareId": 16,
        "resortName": "Khách sạn Cương Quyết Ngầu Nhất An Giang",
        "roomName": "string",
        "bathRoom": 0,
        "bedRooms": 0,
        "startDate": "10-06-2024",
        "endDate": "16-06-2024"
    }
*/
data class MyTimeshareResponse(
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
        @SerializedName("timeShareId") val timeShareId: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("roomName") val roomName: String,
        @SerializedName("bathRoom") val bathRoom: Int,
        @SerializedName("bedRooms") val bedRooms: Int,
        @SerializedName("startDate") val startDate: String,
        @SerializedName("endDate") val endDate: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(timeShareId)
            parcel.writeString(resortName)
            parcel.writeString(roomName)
            parcel.writeInt(bathRoom)
            parcel.writeInt(bedRooms)
            parcel.writeString(startDate)
            parcel.writeString(endDate)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Content> {
            override fun createFromParcel(parcel: Parcel): Content {
                return Content(parcel)
            }

            override fun newArray(size: Int): Array<Content?> {
                return arrayOfNulls(size)
            }
        }

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