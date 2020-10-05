package com.utildev.videocall.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.utildev.videocall.R
import com.utildev.videocall.utils.Constant
import com.utildev.videocall.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        preferenceManager = PreferenceManager(this)
        val database = FirebaseFirestore.getInstance()

        if (preferenceManager.getBoolean(Constant.IS_SIGNED_IN)) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        actSignIn_tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        actSignIn_btSignIn.setOnClickListener {
            database.collection(Constant.USERS)
                .whereEqualTo(Constant.EMAIL, actSignIn_etEmail.text.toString().trim())
                .whereEqualTo(Constant.PASSWORD, actSignIn_etPassword.text.toString().trim())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null && task.result!!.documents.size > 0) {
                        val snapshot = task.result!!.documents[0]
                        preferenceManager.putBoolean(Constant.IS_SIGNED_IN, true)
                        preferenceManager.putString(Constant.USER_ID, snapshot.id)
                        snapshot.getString(Constant.FIRST_NAME)?.let { it ->
                            preferenceManager.putString(
                                Constant.FIRST_NAME,
                                it
                            )
                        }
                        snapshot.getString(Constant.LAST_NAME)?.let { it ->
                            preferenceManager.putString(
                                Constant.LAST_NAME,
                                it
                            )
                        }
                        snapshot.getString(Constant.EMAIL)?.let { it ->
                            preferenceManager.putString(
                                Constant.EMAIL,
                                it
                            )
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}