package org.codeforiraq.craft.fragments


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

import org.codeforiraq.craft.R
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class FeedbackFragment : Fragment() {
lateinit var name:String
    lateinit var comment:String
    lateinit var edt_name:EditText
    lateinit var edt_comment:EditText
    lateinit var btn_sent:RelativeLayout
    lateinit var ctx:Context
    lateinit var db: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_feedback, container, false)
        btn_sent=view.findViewById(R.id.btn_send_coment)
        ctx=container!!.context
        edt_comment=view.findViewById(R.id.edt_comment)
        edt_name=view.findViewById<EditText>(R.id.edt_nameF)


        db= FirebaseDatabase.getInstance()

        btn_sent.setOnClickListener {
            name=view.findViewById<EditText>(R.id.edt_nameF).text.toString()
            comment=view.findViewById<EditText>(R.id.edt_comment).text.toString()

            if(name.isNotEmpty()&&comment.isNotEmpty()) {
                if (isNetworkConnected) {
                    val myRef = db.reference
                    val key = myRef!!.push().key.toString()
                    val map_data = HashMap<String, String>()
                    map_data["name"] = name
                    map_data["comment"] = comment
                    myRef.child("users_comments").child(key).setValue(map_data)
                    Toast.makeText(ctx, "تم الارسال بنجاح", Toast.LENGTH_SHORT).show()
                    edt_comment.setText("")
                    edt_name.setText("")
                } else {
                    Toast.makeText(ctx, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(ctx, "يرجى التأكد من ادخال جميع البيانات", Toast.LENGTH_SHORT).show()

            }
        }

        return view
    }
    private val isNetworkConnected: Boolean
        get() {

            val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni == null) {
                return false
            }
            return true
        }

}
