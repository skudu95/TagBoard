package com.kudu.tagboard.model

data class Group(
//    var title: String,
//    val id: String,
    var hashtags: ArrayList<String> = ArrayList()
)

class GroupList {
    lateinit var name: String
    lateinit var groupList: ArrayList<Group>
}

class ButtonGroupList {
    var ref: ArrayList<GroupList> = ArrayList()
}
