package org.codeforiraq.craft.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.codeforiraq.craft.R
import org.codeforiraq.craft.activities.InfoPage
import org.codeforiraq.craft.activities.InfoPageYourSkill
import org.codeforiraq.craft.modules.ListSkill


class SkillAdapter(var list_skill: ArrayList<ListSkill>, var context: Context?, var viewType: Int)
    : RecyclerView.Adapter<SkillAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val row_skill_layout = holder
        val list_skill_local = list_skill[position]
        row_skill_layout.name_skill.text = list_skill_local.name_skill
        row_skill_layout.name_person.text = list_skill_local.name_person
        row_skill_layout.price.text = "${list_skill_local.price} دينار "


        Picasso.with(context).load(list_skill_local.url_img)
                .placeholder(R.drawable.image)
                .resize(250, 250)
                .into(row_skill_layout.profile_img)

        row_skill_layout.detials.setOnClickListener {
            if (viewType == 0) {
                val i = Intent(context, InfoPage::class.java)
                i.putExtra("user_name", list_skill_local.name_person)
                i.putExtra("email", list_skill_local.email)
                i.putExtra("province", list_skill_local.province)
                i.putExtra("skill", list_skill_local.name_skill)
                i.putExtra("price", list_skill_local.price)
                i.putExtra("details", list_skill_local.details)
                i.putExtra("phone_number", list_skill_local.phone_number)
                i.putExtra("birth_day", list_skill_local.birth_day)
                i.putExtra("id", list_skill_local.id)
                context!!.startActivity(i)
            } else if (viewType == 1||viewType==2) {
                var isFromAdminPanel=0
                if (viewType==2){
                    isFromAdminPanel=1
                }else{
                    isFromAdminPanel=0
                }
                val i = Intent(context, InfoPageYourSkill::class.java)
                i.putExtra("user_name", list_skill_local.name_person)
                i.putExtra("email", list_skill_local.email)
                i.putExtra("province", list_skill_local.province)
                i.putExtra("skill", list_skill_local.name_skill)
                i.putExtra("price", list_skill_local.price)
                i.putExtra("details", list_skill_local.details)
                i.putExtra("phone_number", list_skill_local.phone_number)
                i.putExtra("birth_day", list_skill_local.birth_day)
                i.putExtra("id", list_skill_local.id)
                i.putExtra("isFromAdminPanel", isFromAdminPanel)
                i.putExtra("show", list_skill_local.Show)

                context!!.startActivity(i)
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // val view=LayoutInflater.from(parent!!.context).inflate(R.layout.card_image,parent,false)
        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflator.inflate(R.layout.row_skill, null)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return list_skill.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name_skill = itemView.findViewById<TextView>(R.id.txt_name_skill)
        var name_person = itemView.findViewById<TextView>(R.id.txt_name_person)
        var price = itemView.findViewById<TextView>(R.id.txt_price)
        var detials = itemView.findViewById<TextView>(R.id.txt_detials)
        var profile_img = itemView.findViewById<CircleImageView>(R.id.profile_image)
    }


}

