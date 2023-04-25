package com.example.home_rental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_login: Button
    private lateinit var tv_goToRegister: TextView
    private lateinit var tie_email: TextInputEditText
    private lateinit var tie_password: TextInputEditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login = findViewById<Button>(R.id.btn_login)
        tv_goToRegister = findViewById<TextView>(R.id.tv_goToRegister)
        tie_email = findViewById<TextInputEditText>(R.id.tie_email)
        tie_password = findViewById<TextInputEditText>(R.id.tie_password)

        auth = FirebaseAuth.getInstance()

        tv_goToRegister.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            finish()
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val email = tie_email.text.toString().trim()
            val password = tie_password.text.toString().trim()
            loginUser(email, password)
        }

        val email = RxTextView.textChanges(tie_email)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

        val password = RxTextView.textChanges(tie_password)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        val invalidFields = Observable.combineLatest(
            email,
            password,
            {emailInvalid: Boolean,passwordInvalid: Boolean ->
                !emailInvalid && !passwordInvalid
            })
        invalidFields.subscribe { isValid ->
            if (isValid) {
                btn_login.isEnabled = true
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            }else{
                btn_login.isEnabled = false
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }

    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ login ->
                if(auth.currentUser?.isEmailVerified == false){
                    Toast.makeText(this, "Adresa de email nu este verificata!", Toast.LENGTH_SHORT).show()
                }
                else if (login.isSuccessful){
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Autentificare cu succes!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Adresa de email sau parola incorecta!", Toast.LENGTH_SHORT).show()
                }
            }
    }

}