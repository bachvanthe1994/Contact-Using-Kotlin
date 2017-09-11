package thebv.com.contactusingkotlin.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.widget.*
import thebv.com.contactusingkotlin.R
import thebv.com.contactusingkotlin.common.ContactSharedPreferences
import thebv.com.contactusingkotlin.common.Utils
import thebv.com.contactusingkotlin.entity.Contact
import java.io.File

class AddContactActivity : android.support.v7.app.AppCompatActivity() {

    private var rlAvatar: RelativeLayout? = null
    private var ivAvatar: ImageView? = null
    private var etName: EditText? = null
    private var etPhone: EditText? = null
    private var etEmail: EditText? = null

    private var btCancel: Button? = null
    private var btAdd: Button? = null

    private var avatarUri = ""

    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE: Int = 100
    private val REQUEST_CHOOSE_IMAGE_FOR_QR_CODE: Int = 101


    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        setTitle(getString(R.string.add_contact))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)

        initView()
        initListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun initView() {
        rlAvatar = findViewById(R.id.rlAvatar) as RelativeLayout
        ivAvatar = findViewById(R.id.ivAvatar) as ImageView
        etName = findViewById(R.id.etName) as EditText
        etPhone = findViewById(R.id.etPhone) as EditText
        etEmail = findViewById(R.id.etEmail) as EditText

        btCancel = findViewById(R.id.btCancel) as Button
        btAdd = findViewById(R.id.btAdd) as Button

    }

    private fun initListener() {

        rlAvatar!!.setOnClickListener {
            openImage()
        }

        btCancel!!.setOnClickListener { onBackPressed() }

        btAdd!!.setOnClickListener {

            if (validate()) {

                var contact = Contact(avatarUri,
                        etName!!.text.toString(),
                        etPhone!!.text.toString(),
                        etEmail!!.text.toString()
                )

                ContactSharedPreferences(this).put(contact)

                var dialog: AlertDialog? = null

                dialog = android.app.AlertDialog.Builder(this).setTitle(getString(R.string.notification))
                        .setMessage(getString(R.string.add_contact_success))
                        .setPositiveButton("Ok", { dialogInterface, i ->
                            dialog!!.dismiss()

                            setResult(Activity.RESULT_OK)
                            finish()
                        })
                        .setCancelable(false)
                        .create()
                dialog!!.show()
            }
        }
    }


    fun openImage() {
        if (!Utils.hasPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
        } else {
            try {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_photo)), REQUEST_CHOOSE_IMAGE_FOR_QR_CODE)
            } catch (e: Exception) {
                Log.wtf("thebv", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_IMAGE_FOR_QR_CODE) {
            try {
                val mCaptureUri = Utils.getRealPathFromURI(this, data.data)
                if (File(mCaptureUri).exists()) {
                    avatarUri = mCaptureUri;
                    val bitmap = BitmapFactory.decodeFile(avatarUri)
                    ivAvatar!!.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.wtf("thebv", e)
            }

        }
    }


    private fun validate(): Boolean {

        var validate = true

        if (etName!!.text.trim().length == 0) {
            etName!!.setError(getString(R.string.please_input_name))
            validate = false
        } else {
            etName!!.setError(null)
        }

        if (etPhone!!.text.trim().length == 0) {
            etPhone!!.setError(getString(R.string.please_input_phone))
            validate = false
        } else {
            etPhone!!.setError(null)
        }

        if (etEmail!!.text.trim().length == 0) {
            etEmail!!.setError(getString(R.string.please_input_email))
            validate = false
        } else {
            etEmail!!.setError(null)
        }


        return validate
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var countPermisstions = 0

        for (i in permissions.indices) {
            if (permissions[i].compareTo(android.Manifest.permission.READ_EXTERNAL_STORAGE) == 0 && grantResults[i] == 0)
                countPermisstions++
        }

        if (countPermisstions == 1) {
            openImage()
        }
    }
}
