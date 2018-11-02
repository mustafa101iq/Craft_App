package org.codeforiraq.craft.fragments


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_skill.*
import org.codeforiraq.craft.R
import org.codeforiraq.craft.activities.LogInPage
import org.codeforiraq.craft.activities.MainActivity
import org.codeforiraq.craft.activities.UserProfilePage
import org.codeforiraq.craft.modules.ListSkill
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class AddSkillFragment : Fragment() {
    var your_skill_list = ArrayList<ListSkill>()
    lateinit var db: FirebaseDatabase
    lateinit var user_name: String
    lateinit var phone_number: String
    lateinit var province: String
    lateinit var user_skill: String
    lateinit var price: String
    lateinit var more: String
    private var mAuth: FirebaseAuth? = null
    lateinit var currentUser: FirebaseUser
    lateinit var ctx: Context
    lateinit var btn_birth_day:TextView
    lateinit var btn_signOut:TextView
    var birth_day=""
    var mDateListener:DatePickerDialog.OnDateSetListener?=null
    lateinit var txt_province:TextView
    lateinit var spinProvince:Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_skill, container, false)
        val add_skill_btn = view.findViewById<RelativeLayout>(R.id.btn_add)
        btn_birth_day = view.findViewById<TextView>(R.id.txt_birth_day)
        ctx = container!!.context
        txt_province=view.findViewById(R.id.txt_province)
        spinProvince=view.findViewById(R.id.spinProvince)
        btn_signOut=view.findViewById(R.id.btn_signOut)
        val toolbar = view.findViewById<android.widget.Toolbar>(R.id.toolbarAddSkill)
        val btn_user_profile_addPage = view.findViewById<ImageView>(R.id.btn_user_profile_addPage)
        // setup fire base database
        db = FirebaseDatabase.getInstance()

        // setup fire base auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth!!.currentUser != null) {
            currentUser = mAuth!!.currentUser!!
        }


        //prepare toolbar
        activity!!.setActionBar(toolbar)
        setHasOptionsMenu(true)
        activity!!.invalidateOptionsMenu()

        mDateListener=DatePickerDialog.OnDateSetListener { datePicker, year, month,day ->
            birth_day=" $day / ${month+1} / $year"
            btn_birth_day.text = birth_day
        }
       btn_birth_day.setOnClickListener {
           show_picker_dialog()
       }


        check_auth_and_goTo_loginPage()
        add_skill_btn.setOnClickListener {
            //get data from edit text and convert to string
            user_name = view.findViewById<EditText>(R.id.edt_full_name).text.toString()
            phone_number = view.findViewById<TextView>(R.id.edt_phone_num).text.toString()

            province = spinProvince.selectedItem.toString()

            user_skill = view.findViewById<TextView>(R.id.edt_skill).text.toString()
            price = view.findViewById<TextView>(R.id.edt_price).text.toString()
            more = view.findViewById<TextView>(R.id.edt_more_user).text.toString()


            if(user_name.isNotEmpty()&&phone_number.isNotEmpty()&&province.isNotEmpty()
                    &&price.isNotEmpty()&&more.isNotEmpty()&&birth_day.isNotEmpty()) {
                if (isNetworkConnected) {
                    if (currentUser != null) {
                        try {

                            var photo_url = ""
                            if (currentUser.photoUrl == null) {
                                photo_url = "http://i.imgur.com/DvpvklR.png"
                            } else {
                                photo_url = currentUser.photoUrl!!.toString()
                            }
                            val myRef = db.reference
                            val key = myRef!!.push().key.toString()
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
                            map_data["id"]=key

//                    Toast.makeText(ctx, map_data.toString(), Toast.LENGTH_SHORT).show()

                            myRef.child("skill_users").child(key).setValue(map_data)
                            //  Toast.makeText(ctx, "تم الارسال", Toast.LENGTH_SHORT).show()

                            val pd=ProgressDialog(ctx)
                            pd.setMessage("أنتظر لحضة")
                            pd.setTitle("جاري الاضافة")
                            pd.show()
                            val thread=Thread(){
                                run{
                                    Thread.sleep(2500)
                                    pd.dismiss()
                                    val i=Intent(ctx,MainActivity::class.java)
                                    i.putExtra("from_add",true)
                                    startActivity(i)
//                                    Toast.makeText(ctx, "تم الاضافة بنجاح", Toast.LENGTH_SHORT).show()

                                }

                            }
                          thread.start()

                        } catch (e: Exception) {
                            Toast.makeText(ctx, e.message!!.toString(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(ctx, "لم يتم تسجيل الدخول", Toast.LENGTH_SHORT).show()
                    }
                    //   msg_et.setText("")
                } else {
                    Toast.makeText(ctx, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(ctx, "يرجى التأكد من ادخال جميع البيانات", Toast.LENGTH_SHORT).show()

            }
        }

        btn_user_profile_addPage.setOnClickListener {
            user_profile()
        }

        load_your_skills()

        btn_signOut.setOnClickListener {
            mAuth!!.signOut()
            Toast.makeText(ctx, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show()
            check_auth_and_goTo_loginPage()
        }

        return view

    }

    private fun show_picker_dialog() {
        val cal=Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)
        val month=cal.get(Calendar.MONTH)
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val dailog=DatePickerDialog(ctx,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,year,month,day)
        dailog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dailog.show()
    }

    private fun user_profile() {
        load_your_skills()
        val i = Intent(ctx, UserProfilePage::class.java)
        i.putExtra("user_name", your_skill_list[0].name_person)
        i.putExtra("province", your_skill_list[0].province)
        i.putExtra("skill", your_skill_list[0].name_skill)
        i.putExtra("price", your_skill_list[0].price)
        i.putExtra("details", your_skill_list[0].details)
        i.putExtra("phone_number", your_skill_list[0].phone_number)
        i.putExtra("birth_day", your_skill_list[0].birth_day)
        startActivity(i)

    }

    private fun load_your_skills() {
        val dba = FirebaseDatabase.getInstance().reference.child("skill_users")
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

        try {

            val i = p0!!.children.iterator()
            while (i.hasNext()) {
                val birth_day = i.next().value.toString()
                val details = i.next().value.toString()
                val email = i.next().value.toString()
                val id = i.next().value.toString()
                val phone_number = i.next().value.toString()
                val photo_url = i.next().value.toString()
                val price = i.next().value.toString()
                val province = i.next().value.toString()
                val uid = i.next().value.toString()
                val user_name = i.next().value.toString()
                val user_skill = i.next().value.toString()


                if (mAuth!!.currentUser != null && uid == mAuth!!.currentUser!!.uid) {

                    your_skill_list.add(ListSkill(user_skill, user_name, price,
                            photo_url, province,
                            phone_number, details, birth_day, uid, email,id))
                }

            }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun check_auth_and_goTo_loginPage() {
        if (mAuth!!.currentUser == null) {
            val i = Intent(ctx, LogInPage::class.java)
            ctx.startActivity(i)
        }
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
