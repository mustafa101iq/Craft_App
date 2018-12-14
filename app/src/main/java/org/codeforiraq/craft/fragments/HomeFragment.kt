package org.codeforiraq.craft.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import org.codeforiraq.craft.R
import org.codeforiraq.craft.activities.AdminPanelPage
import org.codeforiraq.craft.adapters.SkillAdapter
import org.codeforiraq.craft.modules.ListSkill

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
    var list_skill = ArrayList<ListSkill>()
    lateinit var recycler_view_home: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var spinerSP: Spinner
    lateinit var ctx: Context
    lateinit var btn_admin:ImageView
    private var mAuth : FirebaseAuth? = null
    lateinit var currentUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val context = container!!.context
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val tool_bar = view.findViewById<TextView>(R.id.toolbar_title_home)
        recycler_view_home = view.findViewById<RecyclerView>(R.id.recycler_view_home)
        progressBar = view.findViewById(R.id.progressB)
        spinerSP = view.findViewById(R.id.spinerSelectProvince)
        ctx = container.context
        btn_admin=view.findViewById(R.id.btn_admin)
        //set title of toolbar
        tool_bar.text = "قائمة المهارات"
        //set up recycler view
//        recycler_view_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        recycler_view_home.adapter = SkillAdapter(list_skill, context, 0)
//        recycler_view_home.adapter.notifyDataSetChanged()
//        load_list_skill()
//        recycler_view_home.adapter = SkillAdapter(list_skill, context, 0)

        mAuth = FirebaseAuth.getInstance();
        if (mAuth!!.currentUser != null) {
          currentUser = mAuth!!.currentUser!!
          if ( currentUser.uid=="sMPUdGlVXhMtJcu6MqRjbLjoTdB3"||currentUser.uid=="B2njJUzWO0f1EH6PIpikGfwQInw2"
                  ||currentUser.uid=="d6hiz8eyjHd8YQaBal4qlyTsv9t2"||
                  currentUser.uid=="Pt5JSr2FJxcQgA8AZfzZ3jSpiR93"||currentUser.uid=="eDeUmuJGKdhYGkt10nweicqJLLp1") {
              btn_admin.visibility=View.VISIBLE
          }else{
              btn_admin.visibility=View.INVISIBLE
          }
        }else{
            btn_admin.visibility=View.INVISIBLE
        }
        //go to admin panel page when click
        btn_admin.setOnClickListener {
          val i =Intent(ctx,AdminPanelPage::class.java)
            ctx.startActivity(i)
        }

        val listPrevice = ArrayList<String>()
        listPrevice.add("بغداد")
        listPrevice.add("جميع المحافظات")
        listPrevice.add("ديالى")
        listPrevice.add("الأنبار")
        listPrevice.add("بابل")
        listPrevice.add("اربيل")
        listPrevice.add("البصرة")
        listPrevice.add("دهوك")
        listPrevice.add("القادسية")
        listPrevice.add("ذي قار")
        listPrevice.add("السليمانية")
        listPrevice.add("صلاح الدين")
        listPrevice.add("كركوك")
        listPrevice.add("كربلاء")
        listPrevice.add("المثنى")
        listPrevice.add("ميسان")
        listPrevice.add("النجف")
        listPrevice.add("نينوى")
        listPrevice.add("واسط")


        var adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(ctx, R.layout.my_spinner_style, listPrevice) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                (v as TextView).textSize = 16f

                return v

            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getDropDownView(position, convertView, parent)

                (v as TextView).gravity = Gravity.CENTER

                return v

            }
        }

        spinerSP.adapter = adapter
        spinerSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                list_skill.clear()
                recycler_view_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recycler_view_home.adapter = SkillAdapter(list_skill, context, 0)
                recycler_view_home.adapter.notifyDataSetChanged()
                load_list_skill()
            }

        }

        return view
    }


    private fun load_list_skill() {

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
                val show = i.next().value.toString()
                val uid = i.next().value.toString()
                val user_name = i.next().value.toString()
                val user_skill = i.next().value.toString()


                val selectProvince = spinerSelectProvince.selectedItem
                if (selectProvince == "جميع المحافظات") {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "ديالى" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "بغداد" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "الأنبار" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "بابل" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "أربيل" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "البصرة" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "دهوك" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "القادسية" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "ذي قار" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "السليمانية" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "صلاح الدين" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "كركوك" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "كربلاء" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "المثنى" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "ميسان" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "النجف" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "نينوى" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                } else if (selectProvince == "واسط" && province == selectProvince) {
                    set_up_recyclerView(birth_day, details, email, id, phone_number, photo_url, price, uid, user_name, user_skill, province,show)
                }

                if (list_skill.isNotEmpty()) {
                    progressBar.visibility = View.GONE
                }


            }

        } catch (e: Exception) {
            // Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun set_up_recyclerView(birth_day: String, details: String, email: String, id: String, phone_number: String,
                                    photo_url: String, price: String,
                                    uid: String, user_name: String, user_skill: String, province: String,show:String) {
        if(show!="0") {
            list_skill.add(ListSkill(user_skill, user_name, price,
                    photo_url, province,
                    phone_number, details, birth_day, uid, email, id, show))
        }
        list_skill.reverse()
        recycler_view_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view_home.adapter = SkillAdapter(list_skill, context, 0)
        recycler_view_home.adapter.notifyDataSetChanged()
    }



}
