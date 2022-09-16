package com.kudu.tagboard.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonGroup(
    @Exclude @set:Exclude @get:Exclude var id: String? = null,
    var buttonName: String? = null,
    var tagItemsNumber: Int = 0,
    var creationTimeButton: Long = 0,
    var tagsList: ArrayList<HashTags> = ArrayList(),
) : Parcelable
