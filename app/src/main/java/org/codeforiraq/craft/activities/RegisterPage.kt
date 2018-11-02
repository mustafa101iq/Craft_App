package org.codeforiraq.craft.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.codeforiraq.craft.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register_page.*
import android.widget.Toast
import android.app.ProgressDialog
import android.content.Intent


class RegisterPage : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val pd:ProgressDialog= ProgressDialog.show(this,"يرجى الانتظار","جاري المعالجة",true)
        pd.show()
        val email=edt_email_reg.text.toString()
        val password=edt_password_reg.text.toString()
        if(!email.isEmpty()&&!password.isEmpty()) {


            mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            pd.dismiss()
                            val user = mAuth!!.currentUser
                            updateUI(user)
                            Toast.makeText(this, "تم التسحيل بنجاح ",
                                    Toast.LENGTH_SHORT).show()
                            val i = Intent(this, LogInPage::class.java)
                            i.putExtra("email", user!!.email!!.toString())
                            startActivity(i)
                        } else {
                            pd.dismiss()
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "لم يتم التسجيل ",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
        }else{
            pd.dismiss()
            Toast.makeText(this,"يرجى ادخال البيانات اولا ",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
       if(user !=null){

      }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.getCurrentUser()
        updateUI(currentUser)
    }
}
