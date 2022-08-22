package com.kudu.tagboard.model

data class Group(
//    var title: String,
//    val id: String,
//    var hashtags: ArrayList<String> = ArrayList()
    var tag1: String? = null,
    var tag2: String? = null,
    var tag3: String? = null,
    var tag4: String? = null,
    var tag5: String? = null,
    var tag6: String? = null,
    var tag7: String? = null,
    var tag8: String? = null,
    var tag9: String? = null,
    var tag10: String? = null,
    var tag11: String? = null,
    var tag12: String? = null,
    var tag13: String? = null,
    var tag14: String? = null,
    var tag15: String? = null,
    var tag16: String? = null,
    var tag17: String? = null,
    var tag18: String? = null,
    var tag19: String? = null,
    var tag20: String? = null,
    var tag21: String? = null,
    var tag22: String? = null,
    var tag23: String? = null,
    var tag24: String? = null,
    var tag25: String? = null,
    var tag26: String? = null,
    var tag27: String? = null,
    var tag28: String? = null,
    var tag29: String? = null,
    var tag30: String? = null,
)

class GroupList {
    lateinit var name: String
    lateinit var groupList: ArrayList<Group>
}

class ButtonGroupList {
    var ref: ArrayList<GroupList> = ArrayList()
}
