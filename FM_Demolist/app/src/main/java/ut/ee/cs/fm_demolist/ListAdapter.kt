package ut.ee.cs.fm_demolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.contact_data.view.*
import java.text.FieldPosition

class ListAdapter(val context: Context,val list: ArrayList<Contact>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view:View =LayoutInflater.from(context).inflate(R.layout.contact_data,parent,false)
        view.contact_name.text=list[position].name
        view.contact_phone_number.text=list[position].phoneNumber
        view.contact_email.text=list[position].email
        view.contact_photo.setImageBitmap(list[position].photo)
        return  view
            }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
      return  position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}