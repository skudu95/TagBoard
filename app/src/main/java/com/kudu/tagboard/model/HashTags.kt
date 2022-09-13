package com.kudu.tagboard.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HashTags(
    var buttonId: String? = null,
    var tagName: String? = null,
) : Parcelable
