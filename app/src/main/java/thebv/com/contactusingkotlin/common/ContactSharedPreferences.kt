package thebv.com.contactusingkotlin.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import thebv.com.contactusingkotlin.entity.Contact

/**
 * Created by thebv on 09/11/2017.
 */

class ContactSharedPreferences(context: Context) {

    private var context: Context? = null
    private var sharedPreferences: SharedPreferences? = null
    private var PREFERENCES_NAME = "CONTACT_SHARED_PREFERENCES"
    private var KEY = "CONTACT"

    init {
        this.context = context
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    fun put(contact: Contact) {
        var edit = sharedPreferences!!.edit()

        var arrContact: ArrayList<Contact> = ArrayList()

        if (sharedPreferences!!.contains(KEY)) {
            arrContact = Gson().fromJson(sharedPreferences!!.getString(KEY, "[]"), object : TypeToken<ArrayList<Contact>>() {}.type)
        }

        arrContact.add(0, contact)

        edit.putString(KEY, Gson().toJson(arrContact))

        edit.commit()
    }

    fun getAll(): ArrayList<Contact> {
        var arrContact: ArrayList<Contact>

        if (sharedPreferences!!.contains(KEY)) {
            arrContact = Gson().fromJson(sharedPreferences!!.getString(KEY, "[]"), object : TypeToken<ArrayList<Contact>>() {}.type)
        } else {
            arrContact = ArrayList()
        }

        return arrContact
    }
}
