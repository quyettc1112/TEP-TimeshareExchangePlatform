package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Sample


import com.google.gson.annotations.SerializedName

/**
[
  {
    "name": "Zachary Nitzsche DDS",
    "phone": "216-273-4084 x146",
    "id": "1"
  },
  {
    "name": "David O'Hara",
    "phone": "558.559.1626 x43427",
    "id": "2"
  },
  {
    "name": "Mrs. Ignacio Hickle",
    "phone": "214.544.7055 x234",
    "id": "3"
  },
  {
    "name": "Toni Farrell",
    "phone": "(428) 663-5766 x9977",
    "id": "4"
  },
  {
    "name": "Mabel Turcotte",
    "phone": "(554) 545-2854 x84409",
    "id": "5"
  }
]
*/
class UserSampleModel : ArrayList<UserSampleModel.UserSampleModelItem>(){
    data class UserSampleModelItem(
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("id") val id: String
    )
}