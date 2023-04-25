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
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_login: Button
    private lateinit var tv_goToRegister: TextView
    private lateinit var tie_email: TextInputEditText
    private lateinit var tie_password: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login = findViewById<Button>(R.id.btn_login)
        tv_goToRegister = findViewById<TextView>(R.id.tv_goToRegister)
        tie_email = findViewById<TextInputEditText>(R.id.tie_email)
        tie_password = findViewById<TextInputEditText>(R.id.tie_password)


        tv_goToRegister.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            finish()
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
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
            { emailInvalid: Boolean, passwordInvalid: Boolean ->
                !emailInvalid && !passwordInvalid
            })
        invalidFields.subscribe { isValid ->
            if (isValid) {
                btn_login.isEnabled = true
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            } else {
                btn_login.isEnabled = false
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }

    }
}