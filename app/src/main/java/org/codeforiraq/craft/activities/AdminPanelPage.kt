package org.codeforiraq.craft.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_panel_page.*
import kotlinx.android.synthetic.main.fragment_your_skills.*
import org.codeforiraq.craft.R
import org.codeforiraq.craft.adapters.SkillAdapter
import org.codeforiraq.craft.modules.ListSkill

class AdminPanelPage : AppCompatActivity() {
    val list_skill=ArrayList<ListSkill>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel_page)
         load_list_skill()

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

               if(show=="0"){
                    list_skill.add(ListSkill(user_skill, user_name, price,
                            photo_url, province,
                            phone_number,details,birth_day,uid,email,id,show))
                    }
                    list_skill.reverse()
                recycler_view_admin.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recycler_view_admin.adapter = SkillAdapter(list_skill, this,2)
                recycler_view_admin.adapter.notifyDataSetChanged()
            }

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        var i= Intent(this,MainActivity::class.java)
        startActivity(i)
    }

}
