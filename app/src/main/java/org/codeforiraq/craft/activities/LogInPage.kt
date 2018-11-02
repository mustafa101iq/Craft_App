package org.codeforiraq.craft.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_log_in_page.*
import org.codeforiraq.craft.R
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LogInPage : AppCompatActivity() {
   private var email:String=""
    lateinit var password:String
   private var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    val RC_SIGN_IN = 3211
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

        mAuth = FirebaseAuth.getInstance();

        if(intent.hasExtra("email")){
            email=intent.extras.getString("email")
            edt_email.setText(email)
        }

        txt_register.setOnClickListener {
            val i =Intent(this,RegisterPage::class.java)
            startActivity(i)
        }

        btn_login.setOnClickListener {
            login()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)

            } catch (e: Exception) {
                // Google Sign In failed, update UI appropriately
                // ...
                Snackbar.make(findViewById(R.id.signInGoogle),e.message!!.toString(), Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        // Log.d(FragmentActivity.TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(FragmentActivity.TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                        Snackbar.make(findViewById(R.id.signInGoogle), "تم تسجيل الدخول", Snackbar.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        //  Log.w(FragmentActivity.TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(findViewById(R.id.signInGoogle), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // ...
                }
    }



    private fun login() {
        email=edt_email.text.toString()
        password=edt_password.text.toString()
        val pd: ProgressDialog = ProgressDialog.show(this,"يرجى الانتظار","جاري المعالجة",true)
        pd.show()
        if(!email.isEmpty()&&!password.isEmpty()) {

            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            pd.dismiss()
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth!!.currentUser
                            updateUI(user)

                            Toast.makeText(this, "تم تسجيل الدخول.", Toast.LENGTH_SHORT).show()

                            val i = Intent(this, MainActivity::class.java)
                            i.putExtra("from_login",true)
                            startActivity(i)
                        } else {
                            pd.dismiss()
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    })
        }else{
            pd.dismiss()
            Toast.makeText(this,"يرجى ادخال البيانات اولا ",Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        if(FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this,"تم تسجيل الدخول",Toast.LENGTH_SHORT).show()
            val i=Intent(this,MainActivity::class.java)
                    i.putExtra("from_login",true)
            startActivity(i)
            finish()
        }else{
            Toast.makeText(this,"يرجى اعادة المحاولة",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        val i=Intent(this,MainActivity::class.java)
        startActivity(i)
    }

}
