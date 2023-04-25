package com.example.home_rental

import android.annotation.SuppressLint
import android.content.Intent
import io.reactivex.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.plugins.RxJavaPlugins

@SuppressLint("CheckResult")
class CreateAccountActivity : AppCompatActivity() {

    private lateinit var tie_full_name : TextInputEditText
    private lateinit var tie_username : TextInputEditText
    private lateinit var tie_password1 : TextInputEditText
    private lateinit var tie_email : TextInputEditText
    private lateinit var tie_password2 : TextInputEditText
    private lateinit var btn_register : Button
    private lateinit var tv_goToLogin : TextView
    private lateinit var rb1 : RadioButton
    private lateinit var rb2 : RadioButton
    private lateinit var radio: RadioGroup
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //Authentication
        auth = FirebaseAuth.getInstance()

        btn_register = findViewById<Button>(R.id.btn_register)
        tv_goToLogin = findViewById<TextView>(R.id.tv_goToLogin)
        tie_full_name = findViewById<TextInputEditText>(R.id.tie_full_name)
        tie_username = findViewById<TextInputEditText>(R.id.tie_username)
        tie_password1 = findViewById<TextInputEditText>(R.id.tie_password1)
        tie_email = findViewById<TextInputEditText>(R.id.tie_email)
        tie_password2 = findViewById<TextInputEditText>(R.id.tie_password2)
        rb1 = findViewById<RadioButton>(R.id.rb1)
        rb2 = findViewById<RadioButton>(R.id.rb2)
        radio = findViewById<RadioGroup>(R.id.radio)

        //Validare nume complet
        val numeComplet = RxTextView.textChanges(tie_full_name)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        numeComplet.subscribe {
            showNameExistAlert(it)
        }

        //Validare email
        val email = RxTextView.textChanges(tie_email)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        email.subscribe {
            showEmailValidAlert(it)
        }

        //Validare username
        val username = RxTextView.textChanges(tie_username)
            .skipInitialValue()
            .map { username ->
                username.length < 6
            }
        username.subscribe {
            showTextMinimalAlert(it, "Nume utilizator")
        }

        //Validare parola
        val password = RxTextView.textChanges(tie_password1)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        password.subscribe {
            showTextMinimalAlert(it, "Parola")
        }

        //Validare RadioButtons
        val rad = RxRadioGroup.checkedChanges(radio)
            .skipInitialValue()
            .map { checked ->
                radio.checkedRadioButtonId == -1
            }


        // Validare confirmare parola
        val passwordConf = Observable.merge(
            RxTextView.textChanges(tie_password1)
                .skipInitialValue()
                .map { password ->
                    password.toString() != tie_password2.text.toString()
                },
            RxTextView.textChanges(tie_password2)
                .skipInitialValue()
                .map { password2 ->
                    password2.toString() != tie_password1.text.toString()
                })
        passwordConf.subscribe{
            showPasswordConfirmAlert(it)
        }

        //Button enable
        val invalidFields = Observable.combineLatest(
            numeComplet,
            email,
            username,
            password,
            passwordConf,
            rad,
            {nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean, radInvalid: Boolean ->
                !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmInvalid && !radInvalid
            })
        invalidFields.subscribe { isValid ->
            if (isValid) {
                btn_register.isEnabled = true
                btn_register.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            }else{
                btn_register.isEnabled = false
                btn_register.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }
        // Click
        tv_goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_register.setOnClickListener {
            val email = tie_email.text.toString().trim()
            val password = tie_password1.text.toString().trim()
            registerUser(email, password)
        }


    }

    private fun showNameExistAlert(isNotValid : Boolean){
        tie_full_name.error = if(isNotValid) "Numele nu este valid" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text : String){
        if(text == "Nume utilizator")
            tie_username.error = if(isNotValid) "$text trebuie sa contina minim 6 caractere" else null
        else if(text == "Parola")
            tie_password1.error = if(isNotValid) "$text trebuie sa contina minim 8 caractere" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        tie_email.error = if(isNotValid) "Adresa de email nu e valida" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        tie_password2.error = if(isNotValid) "Parolele trebuie sa coincida" else null
    }

    private fun registerUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            Toast.makeText(this, "Verificati email-ul!", Toast.LENGTH_SHORT).show()
                        }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Contul a fost creat cu succes!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}