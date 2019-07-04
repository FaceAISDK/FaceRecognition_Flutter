package com.zenglb.framework.module_note

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
class UserObjectTestBean {
    @Id
    var id: Long = 0
    var userName: String? = null
    var age: Int = 0


}