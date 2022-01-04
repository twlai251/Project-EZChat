package com.example.ezchat.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val user_name:String, val user_email:String, val user_password: String, val roll_num:String, val studentID : String, val department: String):
    Parcelable {

    constructor() : this("","","", "","","","")
}
