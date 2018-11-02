package org.codeforiraq.craft.activities

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_info_page.*
import kotlinx.android.synthetic.main.activity_info_page_your_skill.*
import org.codeforiraq.craft.R
import java.util.HashMap

class InfoPageYourSkill : AppCompatActivity() {

    lateinit var user_name: String
    lateinit var email: String
    lateinit var province: String
    lateinit var skill: String
    lateinit var price: String
    lateinit var details: String
    lateinit var phone_number: String
    lateinit var birth_day: String
    lateinit var id: String
    lateinit var db: FirebaseDatabase
    lateinit var db_ref: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_page_your_skill)

        //get data from home page

        user_name = intent.extras.getString("user_name")
        email = intent.extras.getString("email")
        province = intent.extras.getString("province")
        skill = intent.extras.getString("skill")
        price = intent.extras.getString("price")
        details = intent.extras.getString("details")
        phone_number = intent.extras.getString("phone_number")
        birth_day = intent.extras.getString("birth_day")
        id = intent.extras.getString("id")

        db = FirebaseDatabase.getInstance()
        db_ref = db.reference



        btn_edit.setOnClickListener {
            val i = Intent(this, ModifyInfoPage::class.java)
            i.putExtra("user_name", user_name)
            i.putExtra("email", email)
            i.putExtra("province", province)
            i.putExtra("skill", skill)
            i.putExtra("price", price)
            i.putExtra("details", details)
            i.putExtra("phone_number", phone_number)
            i.putExtra("birth_day", birth_day)
            i.putExtra("id", id)
            startActivity(i)
        }
        btn_delete.setOnClickListener {
            val dialog:AlertDialog.Builder=AlertDialog.Builder(this)
            dialog.setMessage("هل تريد حذف المهارة بالفعل !")
            dialog.setPositiveButton("نعم") { dialogInterface, i ->
                db_ref.child("skill_users").child(id).removeValue()
                Toast.makeText(this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("from_add", true)
                startActivity(intent)
            }
            dialog.setNegativeButton("لا") { dialogInterface, i ->

            }
            dialog.show()

        }
        set_info()

        btn_user_profileys.setOnClickListener {
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

        btn_copy_emailys.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("تم النسخ", email)
            clipboard.primaryClip = clip
            Toast.makeText(this, "تم النسخ الى الحافظة", Toast.LENGTH_SHORT).show()
        }
        btn_emailys.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.")

            startActivity(Intent.createChooser(intent, "Send Email"))
        }


    }



    @SuppressLint("SetTextI18n")
    private fun set_info() {
        try {
            txt_name_personIys.text = " الأسم : $user_name "
            txt_emailIys.text = "$email  : الأيميل "
            txt_detailsIys.text = details
            txt_priceIys.text =  " السعر : $price دينار "
            txt_provinceIys.text = "  المحافظة : $province   "
            txt_skillIys.text = " المهارة : $skill  "
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
