package org.codeforiraq.craft.activities

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_user_profile_page.*
import kotlinx.android.synthetic.main.row_skill.*
import org.codeforiraq.craft.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.widget.Toast


class UserProfilePage : AppCompatActivity() {
    lateinit var user_name:String
    lateinit var province:String
    lateinit var skill:String
    lateinit var price:String
    lateinit var details:String
    lateinit var phone_number:String
    lateinit var birth_day:String
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_page)

        //get data from info page
        user_name=intent.extras.getString("user_name")
        province=intent.extras.getString("province")
        skill=intent.extras.getString("skill")
        price=intent.extras.getString("price")
        details=intent.extras.getString("details")
        phone_number=intent.extras.getString("phone_number")
        birth_day=intent.extras.getString("birth_day")

        set_info()

        btn_call.setOnClickListener {
         permission_Check()
        }
        btn_copy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("تم النسخ",phone_number )
            clipboard.primaryClip = clip
            Toast.makeText(this, "تم النسخ الى الحافظة", Toast.LENGTH_SHORT).show()
        }

    }


    @SuppressLint("MissingPermission")
    private fun call_phone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone_number"))
        startActivity(intent)
    }

   fun permission_Check(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf<String>(android.Manifest.permission.CALL_PHONE), 100)
              return
            }

        }
        call_phone()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           call_phone()
        } else {
            permission_Check()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun set_info() {
        txt_full_name.text=" الأسم الكامل : $user_name "
        txt_phone_num.text=" رقم الهاتف : $phone_number   "
        txt_details_user.text=details
        txt_province_user.text="  المحافظة : $province   "
        txt_skill.text=" المهارة : $skill  "
        txt_birth_day_user.text=" تاريخ الميلاد : $birth_day  "
    }
}
