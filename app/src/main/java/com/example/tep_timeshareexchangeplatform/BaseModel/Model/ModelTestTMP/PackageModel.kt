package com.example.tep_timeshareexchangeplatform.BaseModel.Model.ModelTestTMP

import android.os.Parcel
import android.os.Parcelable

data class PackageModel(
    val id: Int,
    val name: String,
    val price: Long,
    val description: String,
    val duration: Int,
    val type: String,
    val listBenefit: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.createStringArrayList() ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeLong(price)
        parcel.writeString(description)
        parcel.writeInt(duration)
        parcel.writeString(type)
        parcel.writeStringList(listBenefit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PackageModel> {
        override fun createFromParcel(parcel: Parcel): PackageModel {
            return PackageModel(parcel)
        }

        override fun newArray(size: Int): Array<PackageModel?> {
            return arrayOfNulls(size)
        }
    }

}