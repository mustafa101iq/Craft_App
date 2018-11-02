package org.codeforiraq.craft.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.codeforiraq.craft.R
import org.codeforiraq.craft.fragments.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_home -> {
                selectedFragment = HomeFragment()

            }
            R.id.navigation_dashboard -> {
               selectedFragment = AddSkillFragment()

            }
            R.id.navigation_notifications -> {
                selectedFragment = YourSkillsFragment()

            }
            R.id.navigation_feedback -> {
                selectedFragment = FeedbackFragment()

            }
            R.id.navigation_about -> {
                selectedFragment = AboutFragment()

            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //set home fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                HomeFragment()).commit()
        if(intent.hasExtra("from_add")){
            bottom_navigation.selectedItemId=R.id.navigation_notifications
        }
        if(intent.hasExtra("from_login")){
            bottom_navigation.selectedItemId=R.id.navigation_dashboard
        }



    }
}
