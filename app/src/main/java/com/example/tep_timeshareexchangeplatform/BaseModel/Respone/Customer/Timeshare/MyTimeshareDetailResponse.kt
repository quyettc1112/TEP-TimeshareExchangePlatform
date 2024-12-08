package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Timeshare


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MyTimeshareDetailResponse(
    @SerializedName("timeShareId") val timeShareId: Int,
    @SerializedName("resortName") val resortName: String,
    @SerializedName("roomCode") val roomCode: String,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("roomId") val roomId: Int,
    @SerializedName("startYear") val startYear: Int,
    @SerializedName("endYear") val endYear: Int,
    @SerializedName("resortId") val resortId: Int,
    @SerializedName("resortImage") val resortImage: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("unitType") val unitType: UnitType,
    @SerializedName("location") val location: Location,
    @SerializedName("roomAmenities") val roomAmenities: List<RoomAmenity>,
    @SerializedName("verify") val verify: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("unitType"),
        TODO("roomAmenities"),
        TODO("location"),
        parcel.readByte() != 0.toByte()
    ) {
    }

    data class Location(
        @SerializedName("name") val name: String,
        @SerializedName("displayName") val displayName: Any?,
        @SerializedName("latitude") val latitude: String,
        @SerializedName("longitude") val longitude: String,
        @SerializedName("country") val country: String,
        @SerializedName("placeId") val placeId: Any?
    )

    data class UnitType(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("area") val area: String,
        @SerializedName("bathrooms") val bathrooms: Int,
        @SerializedName("bedrooms") val bedrooms: Int,
        @SerializedName("bedsFull") val bedsFull: Int,
        @SerializedName("bedsKing") val bedsKing: Int,
        @SerializedName("bedsSofa") val bedsSofa: Int,
        @SerializedName("bedsMurphy") val bedsMurphy: Int,
        @SerializedName("bedsQueen") val bedsQueen: Int,
        @SerializedName("bedsTwin") val bedsTwin: Int,
        @SerializedName("buildingsOption") val buildingsOption: Any?,
        @SerializedName("description") val description: String,
        @SerializedName("kitchen") val kitchen: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("sleeps") val sleeps: Int,
        @SerializedName("view") val view: String
    )

    data class RoomAmenity(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(timeShareId)
        parcel.writeString(resortName)
        parcel.writeString(roomCode)
        parcel.writeString(roomName)
        parcel.writeInt(roomId)
        parcel.writeInt(startYear)
        parcel.writeInt(endYear)
        parcel.writeInt(resortId)
        parcel.writeString(resortImage)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeByte(if (verify) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyTimeshareDetailResponse> {
        override fun createFromParcel(parcel: Parcel): MyTimeshareDetailResponse {
            return MyTimeshareDetailResponse(parcel)
        }

        override fun newArray(size: Int): Array<MyTimeshareDetailResponse?> {
            return arrayOfNulls(size)
        }
    }
}

