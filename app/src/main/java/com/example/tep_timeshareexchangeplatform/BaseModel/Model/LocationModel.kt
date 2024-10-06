package com.example.tep_timeshareexchangeplatform.BaseModel.Model

import android.os.Parcel
import android.os.Parcelable

data class LocationModel(
    val id : Int,
    val name: String,
    val location: String,
    val image: Int,
    val type: Int,
): Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(location)
        dest.writeInt(image)
        dest.writeInt(type)
    }
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    companion object CREATOR : Parcelable.Creator<LocationModel> {
        override fun createFromParcel(parcel: Parcel): LocationModel {
            return LocationModel(parcel)
        }

        override fun newArray(size: Int): Array<LocationModel?> {
            return arrayOfNulls(size)
        }
    }


}
