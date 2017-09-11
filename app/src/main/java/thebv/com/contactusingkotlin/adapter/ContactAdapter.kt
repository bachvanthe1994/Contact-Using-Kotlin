package thebv.com.contactusingkotlin.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import thebv.com.contactusingkotlin.R
import thebv.com.contactusingkotlin.entity.Contact
import java.io.File

/**
 * Created by thebv on 09/11/2017.
 */

class ContactAdapter(activity: Activity?, arr: ArrayList<Contact>?) : BaseAdapter() {

    private var activity: Activity

    private var arr: ArrayList<Contact>

    init {
        this.activity = activity!!
        this.arr = arr!!
    }

    override fun getCount(): Int {
        return if (arr == null) 0 else arr.size
    }


    override fun getItem(position: Int): Contact {
        return arr[position]
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup?): View? {

        val view: View?

        if (contentView != null) {
            view = contentView
        } else {
            view = activity.layoutInflater.inflate(R.layout.item_contact, null)
        }

        if (getItem(position).avatar != null && File(getItem(position).avatar).exists()) {
            view!!.findViewById<ImageView>(R.id.ivAvatar).setImageBitmap(BitmapFactory.decodeFile(getItem(position).avatar))
        }

        view!!.findViewById<TextView>(R.id.tvName).text = getItem(position).name
        view!!.findViewById<TextView>(R.id.tvPhone).text = getItem(position).phone

        return view
    }


}