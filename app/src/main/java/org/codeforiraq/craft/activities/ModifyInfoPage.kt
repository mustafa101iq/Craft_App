package org.codeforiraq.craft.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_modify_info_page.*
import org.codeforiraq.craft.R
import org.codeforiraq.craft.modules.ListSkill
import java.util.*

class ModifyInfoPage : AppCompatActivity() {
 lateinit var db:FirebaseDatabase
    var your_skill_list = ArrayList<ListSkill>()
    lateinit var user_name: String
    lateinit var phone_number: String
    lateinit var province: String
    lateinit var user_skill: String
    lateinit var price: String
    lateinit var more: String
    private var mAuth: FirebaseAuth? = null
    lateinit var currentUser: FirebaseUser
    var birth_day=""
    lateinit var db_ref:DatabaseReference
    var mDateListener: DatePickerDialog.OnDateSetListener?=null
    var isFromAdminPanel=0
    lateinit var show:String

    lateinit var txt_province: TextView
    lateinit var spinProvince: Spinner
    lateinit var id:String
    lateinit var email:String
    lateinit var skill:String
    lateinit var details:String
    lateinit var db_auth:FirebaseAuth
    lateinit var old_name:String
    lateinit var old_phone:String
    lateinit var old_skill:String
    lateinit var old_details:String
    lateinit var old_province:String
    lateinit var old_birth_day:String
    lateinit var old_price:String
    lateinit var old_show:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_info_page)

        id=intent.extras.getString("id")
        user_name=intent.extras.getString("user_name")
        email=intent.extras.getString("email")
        province=intent.extras.getString("province")
        skill=intent.extras.getString("skill")
        price=intent.extras.getString("price")
        details=intent.extras.getString("details")
        phone_number=intent.extras.getString("phone_number")
        birth_day=intent.extras.getString("birth_day")
        isFromAdminPanel=intent.extras.getInt("isFromAdminPanel")
        show=intent.extras.getString("show")

        old_name=user_name
        old_birth_day=birth_day
        old_details=details
        old_phone=phone_number
        old_price=price
        old_province=province
        old_skill=skill
        old_show=show

        if(isFromAdminPanel==1){
            edt_show.visibility=View.VISIBLE
            edt_show.setText(old_show)
        }else{
            edt_show.visibility=View.GONE
        }

        edt_full_nameM.setText(old_name)
        edt_more_userM.setText(old_details)
        edt_phone_numM.setText(old_phone)
        edt_priceM.setText(old_price)
        txt_birth_dayM.setText(old_birth_day)
        edt_skillM.setText(old_skill)

        db_auth= FirebaseAuth.getInstance()
       currentUser=db_auth.currentUser!!

        mDateListener=DatePickerDialog.OnDateSetListener { datePicker, year, month,day ->
            birth_day=" $day / ${month+1} / $year"
            txt_birth_dayM.text = birth_day
        }
        txt_birth_dayM.setOnClickListener {
            show_picker_dialog()
        }


        btn_modify.setOnClickListener {

            user_name = edt_full_nameM.text.toString()
            phone_number = edt_phone_numM.text.toString()
            user_skill = edt_skillM.text.toString()
            price = edt_priceM.text.toString()
            more = edt_more_userM.text.toString()
            province = spinProvinceM.selectedItem.toString()
            show = edt_show.text.toString()
            var photo_url = ""
            if (currentUser.photoUrl == null) {
                photo_url = "http://i.imgur.com/DvpvklR.png"
            } else {
                photo_url = currentUser.photoUrl!!.toString()
            }
            db = FirebaseDatabase.getInstance()
            db_ref = db.reference
            if (user_name!=old_name || phone_number!=old_phone
                    || price!=old_price || more!=old_details || birth_day!=old_birth_day
                    ||province!=old_province||skill!=old_skill||show!=old_show) {
                val map_data = HashMap<String, String>()
                map_data["user_name"] = user_name
                map_data["phone_number"] = phone_number
                map_data["province"] = province
                map_data["user_skill"] = user_skill
                map_data["price"] = price
                map_data["details"] = more
                map_data["photo_url"] = photo_url
                map_data["uid"] = currentUser.uid.toString()
                map_data["email"] = currentUser.email!!.toString()
                map_data["birth_day"] = birth_day
                map_data["id"] = id
                map_data["show"] = show
                db_ref.child("skill_users").child(id).setValue(map_data)
                Toast.makeText(this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show()
                if(isFromAdminPanel==1){
                    val i=Intent(this,AdminPanelPage::class.java)
                    startActivity(i)
                   finish()
                }else{
                    val i=Intent(this,MainActivity::class.java)
                    i.putExtra("from_add",true)
                    startActivity(i)
                    finish()
                }

            } else {
                Toast.makeText(this, "يرجى تعديل احد البيانات اولا", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun show_picker_dialog() {
        val cal= Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)
        val month=cal.get(Calendar.MONTH)
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val dailog=DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,year,month,day)
        dailog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dailog.show()
    }


}
