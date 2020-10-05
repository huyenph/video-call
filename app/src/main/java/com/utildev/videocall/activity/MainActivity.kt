package com.utildev.videocall.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.utildev.videocall.R
import com.utildev.videocall.utils.Constant
import com.utildev.videocall.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferenceManager = PreferenceManager(this)

        actMain_tvUserName.text = String.format(
            "Hello %s %s",
            preferenceManager.getString(Constant.FIRST_NAME),
            preferenceManager.getString(Constant.LAST_NAME)
        )

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                sendFCMTokenToDatabase(task.result!!.token)
            }
        }
    }

    private fun sendFCMTokenToDatabase(token: String) {
        val database = FirebaseFirestore.getInstance()
        val documentReference = preferenceManager.getString(Constant.USER_ID)?.let {
            database.collection(Constant.USERS)
                .document(it)
        }
        documentReference?.update(Constant.FCM_TOKEN, token)
            ?.addOnSuccessListener {
                Toast.makeText(this, "Token updated", Toast.LENGTH_SHORT).show()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Unable to send token: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun signOut() {
        val database = FirebaseFirestore.getInstance()
        val documentReference = preferenceManager.getString(Constant.USER_ID)?.let {
            database.collection(Constant.USERS)
                .document(it)
        }
        val updates = HashMap<String, Any>()
        updates[Constant.FCM_TOKEN] = FieldValue.delete()
        documentReference?.update(updates)
            ?.addOnSuccessListener {
                preferenceManager.clearPreference()
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Unable to sign out: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}