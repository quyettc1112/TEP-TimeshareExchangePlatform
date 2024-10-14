package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import android.os.Parcel
import android.os.Parcelable

data class MyTimeshareModel(
    val id: Int,
    val name: String,
    val roomName: String,
    val checkInDate: String,
    val checkOutDate: String,
    val numberOfNight: Int,
    val price: String,
    val image: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(roomName)
        parcel.writeString(checkInDate)
        parcel.writeString(checkOutDate)
        parcel.writeInt(numberOfNight)
        parcel.writeString(price)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyTimeshareModel> {
        override fun createFromParcel(parcel: Parcel): MyTimeshareModel {
            return MyTimeshareModel(parcel)
        }

        override fun newArray(size: Int): Array<MyTimeshareModel?> {
            return arrayOfNulls(size)
        }
    }


}
