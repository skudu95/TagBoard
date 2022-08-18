package com.kudu.tagboard.model

data class Group(
    var hashtags: ArrayList<HashTags>,

    )

class GroupList {
    var groupList: ArrayList<Group> = ArrayList()
}
