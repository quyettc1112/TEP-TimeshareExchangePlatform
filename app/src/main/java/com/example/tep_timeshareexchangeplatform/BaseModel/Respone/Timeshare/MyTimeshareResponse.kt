package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Timeshare


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

    companion object CREATOR : Parcelable.Creator<MyTimeshareResponse> {
        override fun createFromParcel(parcel: Parcel): MyTimeshareResponse {
            return MyTimeshareResponse(parcel)
        }

        override fun newArray(size: Int): Array<MyTimeshareResponse?> {
            return arrayOfNulls(size)
        }
    }


}