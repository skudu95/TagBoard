package com.kudu.tagboard.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonGroup(
    @Exclude @set:Exclude @get:Exclude var id: String? = null,
    var buttonName: String? = null,
//    var tags: List<String>? = null,
    var tagsList: ArrayList<HashTags> = ArrayList(),
): Parcelable
