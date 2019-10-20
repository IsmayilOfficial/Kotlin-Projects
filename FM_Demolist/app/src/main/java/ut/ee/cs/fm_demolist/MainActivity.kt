package ut.ee.cs.fm_demolist

import android.Manifest
import androidx.appcompat.app.AppCompatActivity


import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri

import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity() {

    val REQUEST_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_PERMISSION)
        }
        else{
            getContacts()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==REQUEST_PERMISSION)getContacts()
    }
    private fun getContacts(){
        val adapter =ListAdapter(this,getContactsData())
        contact_list.adapter = adapter
    }

    private fun getContactsData(): ArrayList<Contact> {
        val contactList=ArrayList<Contact>()
        val contactsCursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        if ((contactsCursor?.count ?:0)>0){
            while (contactsCursor !=null && contactsCursor.moveToNext()){
                val rowID=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber=""
                if (contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    val phoneNumberCursor=contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        arrayOf<String>(rowID),
                        null

                    )
                    while (phoneNumberCursor!!.moveToNext()){
                        phoneNumber +=phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )+"\n"
                    }
                    phoneNumberCursor.close()
                }
                var email =""
                val emailCursor=contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",
                    arrayOf<String>(rowID),
                    null

                )
                while (emailCursor!!.moveToNext()){
                    email+=emailCursor.getString(
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)

                    )+"\n"
                }
                emailCursor.close()
                val contactPhotoUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,rowID)
                val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,contactPhotoUri)
                val buffer  = BufferedInputStream(photoStream)
                val contactPhoto = BitmapFactory.decodeStream(buffer)


                contactList.add(Contact(name,phoneNumber,email,contactPhoto))
            }

        }
        contactsCursor!!.close()

        return contactList

    }
}

