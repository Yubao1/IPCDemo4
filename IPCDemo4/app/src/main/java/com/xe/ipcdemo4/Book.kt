package com.xe.ipcdemo4

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 86188 on 2021/6/23.
 */
class Book{
    var mId: Int? = null
    var mName: String? = null

    override fun toString(): String {
        return "mId = " + mId + ",mName = " + mName
    }
}