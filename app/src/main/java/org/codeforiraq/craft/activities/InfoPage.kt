package org.codeforiraq.craft.activities

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_info_page.*
import org.codeforiraq.craft.R
import java.util.*

class InfoPage : AppCompatActivity() {

    lateinit var user_name: String
    lateinit var email: String
    lateinit var province: String
    lateinit var skill: String
    lateinit var price: String
    lateinit var details: String
    lateinit var phone_number: String
    lateinit var birth_day: String
    lateinit var shared: SharedPreferences
    var like = 0
    var dislike = 0
    var is_like_or_dislike = false
    lateinit var db: FirebaseDatabase
    lateinit var db_ref: DatabaseReference
    lateinit var id_skill: String
    lateinit var id_skill_rate:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_page)

        //get data from home page
        user_name = intent.extras.getString("user_name")
        email = intent.extras.getString("email")
        province = intent.extras.getString("province")
        skill = intent.extras.getString("skill")
        price = intent.extras.getString("price")
        details = intent.extras.getString("details")
        phone_number = intent.extras.getString("phone_number")
        birth_day = intent.extras.getString("birth_day")
        id_skill = intent.extras.getString("id")

        db = FirebaseDatabase.getInstance()
        db_ref = db.reference

        //prepare shard prefrence
        shared = getSharedPreferences("save", Context.MODE_PRIVATE)
        //lod -> like_or_dislike
        is_like_or_dislike = shared.getBoolean(id_skill, false)

        set_info()

        btn_user_profile.setOnClickListener {
            val i = Intent(this, UserProfilePage::class.java)
            i.putExtra("user_name", user_name)
            i.putExtra("province", province)
            i.putExtra("skill", skill)
            i.putExtra("price", price)
            i.putExtra("details", details)
            i.putExtra("phone_number", phone_number)
            i.putExtra("birth_day", birth_day)
            startActivity(i)
        }

        btn_copy_email.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("تم النسخ", email)
            clipboard.primaryClip = clip
            Toast.makeText(this, "تم النسخ الى الحافظة", Toast.LENGTH_SHORT).show()
        }
        btn_email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.")

            startActivity(Intent.createChooser(intent, "Send Email"))
        }
        btn_like.setOnClickListener {
            fun_like()
        }

        btn_dislike.setOnClickListener {
            fun_dislike()
        }

        load_rate_info()
        if (is_like_or_dislike) {
            btn_like.isEnabled = false
            btn_dislike.isEnabled = false
        }


    }

    private fun load_rate_info() {
        val dba = FirebaseDatabase.getInstance().reference.child("rate")
        dba.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                add_child(p0)
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                add_child(p0)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })

    }

    private fun add_child(p0: DataSnapshot?) {

        val i = p0!!.children.iterator()
        while (i.hasNext()) {
            val dl = i.next().value!!.toString().toInt()
            id_skill_rate = i.next().value!!.toString()
            val l = i.next().value!!.toString().toInt()

            if(id_skill_rate==id_skill){
                like=l
                dislike=dl
            }
        }
        txt_like.setText(like.toString()+" من الاشخاص معجبون بهذا")
        txt_dislike.setText(dislike.toString()+" من الاشخاص لم يعجبهم هذا")
    }

    private fun fun_dislike() {
        if (is_like_or_dislike == false) {
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putBoolean(id_skill, true)
            editor.apply()
            dislike++
            val map_data = HashMap<String, String>()
            val key = db_ref.push().key.toString()

            map_data["like"] = like.toString()
            map_data["dislike"] = dislike.toString()
            map_data["id_skill"] = id_skill
            db_ref.child("rate").child(key).setValue(map_data)
            txt_dislike.setText(dislike.toString())
            btn_like.isEnabled = false
            btn_dislike.isEnabled = false
            Toast.makeText(this, "تم التقيم ", Toast.LENGTH_SHORT).show()


        }

    }

    private fun fun_like() {
        if (is_like_or_dislike == false) {
            val editor: SharedPreferences.Editor = shared.edit()
            val key = db_ref.push().key.toString()
            editor.putBoolean(id_skill, true)
            editor.apply()
            like++
            val map_data = HashMap<String, String>()
            map_data["like"] = like.toString()
            map_data["dislike"] = dislike.toString()
            map_data["id_skill"] = id_skill
            db_ref.child("rate").child(key).setValue(map_data)
            Toast.makeText(this, "تم التقيم ", Toast.LENGTH_SHORT).show()
            txt_like.setText(like.toString())
            btn_dislike.isEnabled = false
            btn_like.isEnabled = false

        }

    }

    @SuppressLint("SetTextI18n")
    private fun set_info() {
        txt_name_personI.text = " الأسم : $user_name "
        txt_emailI.text = "$email  : الأيميل "
        txt_detailsI.text = details
        txt_priceI.text = " السعر : $price دينار "
        txt_provinceI.text = "  المحافظة : $province   "
        txt_skillI.text = " المهارة : $skill  "
    }


}
