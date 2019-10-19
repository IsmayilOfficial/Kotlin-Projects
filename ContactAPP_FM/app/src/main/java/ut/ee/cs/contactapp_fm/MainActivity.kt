package ut.ee.cs.contactapp_fm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var btn: Button? = null
    private var tvname: TextView? = null
    private var tvphone: TextView? = null
    private var tvmail: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.btn) as Button
        tvname = findViewById(R.id.tvname) as TextView
        tvphone = findViewById(R.id.tvphone) as TextView
        tvmail = findViewById(R.id.tvmail) as TextView


        btn!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 1)
        }

    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val contactData = data?.data
            val c = contentResolver.query(contactData!!, null, null, null, null)
            if (c!!.moveToFirst()) {

                var phoneNumber = ""
                var emailAddress = ""
                val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
               

                var hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone.equals("1", ignoreCase = true))
                    hasPhone = "true"
                else
                    hasPhone = "false"

                if (java.lang.Boolean.parseBoolean(hasPhone)) {
                    val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
                    while (phones!!.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
                }

                // Find Email Addresses
                val emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null)
                while (emails!!.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                }
                emails.close()

                //mainActivity.onBackPressed();
                // Toast.makeText(mainactivity, "go go go", Toast.LENGTH_SHORT).show();

                tvname!!.text = "Name: " + name
                tvphone!!.text = "Phone: " + phoneNumber
                tvmail!!.text = "Email: " + emailAddress
                Log.d("curs", "$name num$phoneNumber mail$emailAddress")
            }
            c.close()
        }
    }
}
