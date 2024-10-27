package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
{
"id": 10,
"fullName": "Trần Cương Quyết",
"dob": "11-01-2024",
"address": "string",
"gender": "string",
"phone": "012414412",
"memberPurchaseDate": null,
"memberExpiryDate": null,
"membershipId": null,
"country": null,
"street": null,
"city": null,
"state": null,
"postalCode": null,
"note": null,
"user": {
"id": 33,
"userName": null,
"email": "quyet@gmail.com",
"isActive": true
},
"isActive": true,
"isMember": false
}
 */
data class CustomerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("memberPurchaseDate") val memberPurchaseDate: Any?,
    @SerializedName("memberExpiryDate") val memberExpiryDate: Any?,
    @SerializedName("membershipId") val membershipId: Any?,
    @SerializedName("country") val country: Any?,
    @SerializedName("street") val street: Any?,
    @SerializedName("city") val city: Any?,
    @SerializedName("state") val state: Any?,
    @SerializedName("postalCode") val postalCode: Any?,
    @SerializedName("note") val note: Any?,
    @SerializedName("user") val user: User,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isMember") val isMember: Boolean
) : Parcelable {

    data class User(
        @SerializedName("id") val id: Int,
        @SerializedName("userName") val userName: Any?,
        @SerializedName("email") val email: String,
        @SerializedName("isActive") val isActive: Boolean
    ) : Parcelable {
        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeValue(userName) // userName có thể là null, sử dụng writeValue()
            dest.writeString(email)
            dest.writeByte(if (isActive) 1 else 0) // Boolean được ghi dưới dạng byte
        }

        companion object CREATOR : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(
                    id = parcel.readInt(),
                    userName = parcel.readValue(Any::class.java.classLoader),
                    email = parcel.readString()!!,
                    isActive = parcel.readByte() != 0.toByte()
                )
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(fullName)
        dest.writeString(dob)
        dest.writeString(address)
        dest.writeString(gender)
        dest.writeString(phone)
        dest.writeValue(memberPurchaseDate) // Có thể null, sử dụng writeValue()
        dest.writeValue(memberExpiryDate)
        dest.writeValue(membershipId)
        dest.writeValue(country)
        dest.writeValue(street)
        dest.writeValue(city)
        dest.writeValue(state)
        dest.writeValue(postalCode)
        dest.writeValue(note)
        dest.writeParcelable(user, flags) // Ghi đối tượng User vào Parcel
        dest.writeByte(if (isActive) 1 else 0) // Boolean được ghi dưới dạng byte
        dest.writeByte(if (isMember) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<CustomerResponse> {
        override fun createFromParcel(parcel: Parcel): CustomerResponse {
            return CustomerResponse(
                id = parcel.readInt(),
                fullName = parcel.readString()!!,
                dob = parcel.readString()!!,
                address = parcel.readString()!!,
                gender = parcel.readString()!!,
                phone = parcel.readString()!!,
                memberPurchaseDate = parcel.readValue(Any::class.java.classLoader),
                memberExpiryDate = parcel.readValue(Any::class.java.classLoader),
                membershipId = parcel.readValue(Any::class.java.classLoader),
                country = parcel.readValue(Any::class.java.classLoader),
                street = parcel.readValue(Any::class.java.classLoader),
                city = parcel.readValue(Any::class.java.classLoader),
                state = parcel.readValue(Any::class.java.classLoader),
                postalCode = parcel.readValue(Any::class.java.classLoader),
                note = parcel.readValue(Any::class.java.classLoader),
                user = parcel.readParcelable(User::class.java.classLoader)!!,
                isActive = parcel.readByte() != 0.toByte(),
                isMember = parcel.readByte() != 0.toByte()
            )

        }

        override fun newArray(size: Int): Array<CustomerResponse?> {
            return arrayOfNulls(size)
        }

    }
}
