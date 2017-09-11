package thebv.com.contactusingkotlin.entity

import java.io.Serializable

/**
 * Created by thebv on 09/11/2017.
 */

class Contact : Serializable {

    var avatar: String = ""
    var name: String = ""
    var phone: String = ""
    var email: String = ""

    constructor(avatar: String, name: String, phone: String, email: String) {
        this.avatar = avatar
        this.name = name
        this.phone = phone
        this.email = email
    }
}
