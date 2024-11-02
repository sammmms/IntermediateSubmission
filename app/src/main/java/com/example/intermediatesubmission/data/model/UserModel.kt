package com.example.intermediatesubmission.data.model

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    val name: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "UserModel(name='$name', email='$email', password='$password')"
    }

    fun isValid(): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }
}