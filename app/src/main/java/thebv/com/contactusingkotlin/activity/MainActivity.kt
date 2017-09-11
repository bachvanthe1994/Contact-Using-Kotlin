package thebv.com.contactusingkotlin.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import thebv.com.contactusingkotlin.R
import thebv.com.contactusingkotlin.adapter.ContactAdapter
import thebv.com.contactusingkotlin.common.ContactSharedPreferences
import thebv.com.contactusingkotlin.entity.Contact
import java.io.File
import android.widget.RelativeLayout
import android.R.attr.button
import android.os.SystemClock


class MainActivity : AppCompatActivity() {

    private var lvContact: ListView? = null
    private var tvEmpty: TextView? = null

    private var arrContact: ArrayList<Contact> = ArrayList()
    private var contactAdapter: ContactAdapter? = null

    private val REQUEST_ADD_CONTACT: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListener()
    }

    private fun initView() {
        lvContact = findViewById(R.id.lvContact) as ListView
        tvEmpty = findViewById(R.id.tvEmpty) as TextView

        loadOrReloadContact()
    }

    private fun initListener() {
        findViewById(R.id.btAddContact).setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_CONTACT)
        }

        lvContact!!.setOnItemClickListener { adapterView, view, position, l -> openContact(arrContact[position]) }
    }

    fun loadOrReloadContact() {
        arrContact.clear()
        arrContact.addAll(ContactSharedPreferences(this).getAll())

        if (arrContact.size == 0) {
            tvEmpty!!.visibility = View.VISIBLE
            lvContact!!.visibility = View.GONE
        } else {
            tvEmpty!!.visibility = View.GONE
            lvContact!!.visibility = View.VISIBLE
        }

        if (contactAdapter == null) {
            contactAdapter = ContactAdapter(this, arrContact)
            lvContact!!.adapter = contactAdapter
        } else {
            contactAdapter!!.notifyDataSetChanged()
        }
    }

    fun openContact(contact: Contact) {
        var dialog = Dialog(this)

        var rootView = RelativeLayout(this)

        var ivAvatar = ImageView(this)
        ivAvatar.id = SystemClock.currentThreadTimeMillis().toInt()
        ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
        ivAvatar.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimen(200f))

        if (contact.avatar != null && File(contact.avatar).exists()) {
            ivAvatar.setImageBitmap(BitmapFactory.decodeFile(contact.avatar))
        }

        rootView.addView(ivAvatar)

        var content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.setBackgroundColor(Color.parseColor("#80000000"))
        content.setPadding(getDimen(10f), getDimen(10f), getDimen(10f), getDimen(10f))

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.ALIGN_BOTTOM, ivAvatar.id)
        content.layoutParams = params

        var tvName = TextView(this)
        tvName.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20f, resources.getDisplayMetrics()).toFloat()
        tvName.text = contact.name
        tvName.setTextColor(Color.WHITE)


        var tvPhone = TextView(this)
        tvPhone.text = contact.phone
        tvPhone.setTextColor(Color.WHITE)

        content.addView(tvName)
        content.addView(tvPhone)

        rootView.addView(content)

        dialog.setContentView(rootView)
        dialog.show()
    }

    fun getDimen(value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.getDisplayMetrics()).toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                loadOrReloadContact()
            }
        }
    }
}
