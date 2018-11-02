package org.codeforiraq.craft.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_your_skills.*

import org.codeforiraq.craft.R
import org.codeforiraq.craft.adapters.SkillAdapter
import org.codeforiraq.craft.modules.ListSkill

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class YourSkillsFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth
    val list_skill=ArrayList<ListSkill>()
    lateinit var state_your_skills:TextView
    lateinit var ctx:Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        ctx = container!!.context
        mAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_your_skills, container, false)
        state_your_skills=view.findViewById<TextView>(R.id.txt_state_your_skills)
        //prepare recycler view
        val recycler_view_your_skill = view.findViewById<RecyclerView>(R.id.recycler_view_your_skill)
        recycler_view_your_skill.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)

        load_list_skill()

        recycler_view_your_skill.adapter=SkillAdapter(list_skill,ctx,1)

        if(mAuth.currentUser!=null&&list_skill.isEmpty()){
            state_your_skills.text="لم يتم اضافة مهارة"
        }else if(mAuth.currentUser==null){
            state_your_skills.text="يرجى تسجيل الدخول لعرض مهاراتك"
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
                val uid = i.next().value.toString()
                val user_name = i.next().value.toString()
                val user_skill = i.next().value.toString()


                if(mAuth.currentUser !=null && uid==mAuth.currentUser!!.uid){

                    list_skill.add(ListSkill(user_skill, user_name, price,
                            photo_url, province,
                            phone_number,details,birth_day,uid,email,id))
                    list_skill.reverse()
                    recycler_view_your_skill.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    recycler_view_your_skill.adapter = SkillAdapter(list_skill, context,1)
                    recycler_view_your_skill.adapter.notifyDataSetChanged()

                    if(list_skill.isNotEmpty()){
                        state_your_skills.visibility=View.GONE

                    }

                }

            }

        } catch (e: Exception) {
            Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
        }

    }


}
