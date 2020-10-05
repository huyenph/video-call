package com.utildev.videocall.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.utildev.videocall.R
import com.utildev.videocall.utils.Constant
import com.utildev.videocall.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        preferenceManager = PreferenceManager(this)
        val database = FirebaseFirestore.getInstance()
        actSignUp_btSignUp.setOnClickListener {
            val user = HashMap<String, Any>()
            user[Constant.FIRST_NAME] = actSignUp_etFirstName.text.toString().trim()
            user[Constant.LAST_NAME] = actSignUp_etLastName.text.toString().trim()
            user[Constant.EMAIL] = actSignUp_etEmail.text.toString().trim()
            user[Constant.PASSWORD] = actSignUp_etPassword.text.toString().trim()
            database.collection(Constant.USERS)
                .add(user)
                .addOnSuccessListener {
                    preferenceManager.putBoolean(Constant.IS_SIGNED_IN, true)
                    preferenceManager.putString(
                        Constant.FIRST_NAME,
                        actSignUp_etFirstName.text.toString().trim()
                    )
                    preferenceManager.putString(
                        Constant.LAST_NAME,
                        actSignUp_etLastName.text.toString().trim()
                    )
                    preferenceManager.putString(
                        Constant.EMAIL,
                        actSignUp_etEmail.text.toString().trim()
                    )
                    preferenceManager.putString(
                        Constant.PASSWORD,
                        actSignUp_etPassword.text.toString().trim()
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                }
        }
    }
}