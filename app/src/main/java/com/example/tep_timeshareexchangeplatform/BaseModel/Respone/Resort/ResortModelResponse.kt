package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Resort


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class ResortModelResponse(
    @SerializedName("content") val content: List<Content>,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("last") val last: Boolean,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("first") val first: Boolean,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("empty") val empty: Boolean
) {
    data class Content(
        @SerializedName("id") val id: Int,
        @SerializedName("resortName") val resortName: String,
        @SerializedName("resortLocationName") val resortLocationName: String?,
        @SerializedName("resortLocationDisplayName") val resortLocationDisplayName: String?,
        @SerializedName("resortLocationLatitude") val resortLocationLatitude: String?,
        @SerializedName("resortLocationLongitude") val resortLocationLongitude: String?,
        @SerializedName("logo") val logo: String,
        @SerializedName("minPrice") val minPrice: Long,
        @SerializedName("maxPrice") val maxPrice: Long,
        @SerializedName("status") val status: String,
        @SerializedName("address") val address: String,
        @SerializedName("timeshareCompanyId") val timeshareCompanyId: Int,
        @SerializedName("isActive") val isActive: Boolean,
        @SerializedName("averageRating") val averageRating: Float,
        @SerializedName("totalRating") val totalRating: Int,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readFloat(),
            parcel.readInt()
        ) {
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeString(resortName)
            dest.writeString(resortLocationName)
            dest.writeString(resortLocationDisplayName)
            dest.writeString(resortLocationLatitude)
            dest.writeString(resortLocationLongitude)
            dest.writeString(logo)
            dest.writeLong(minPrice)
            dest.writeLong(maxPrice)
            dest.writeString(status)
            dest.writeString(address)
            dest.writeInt(timeshareCompanyId)
            dest.writeByte(if (isActive) 1 else 0)
            dest.writeFloat(averageRating)
            dest.writeInt(totalRating)
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

