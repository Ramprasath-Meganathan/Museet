package com.example.museet.ui.signup

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.museet.R
import com.example.museet.ui.login.LoggedInUserView
import com.example.museet.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class InstrumentSelectionActivity : AppCompatActivity() {
    lateinit var register: Button
    lateinit var veena: CheckBox
    lateinit var sitar: CheckBox
    lateinit var algoza: CheckBox
    lateinit var harmonium: CheckBox
    lateinit var saranga: CheckBox
    lateinit var tabla: CheckBox
    lateinit var tavil: CheckBox
    lateinit var mridangam: CheckBox
    private lateinit var auth: FirebaseAuth
    var instrumentsList: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_instrumentselection)
        val cancel = findViewById<Button>(R.id.cancel_button)
        register = findViewById<Button>(R.id.register_button)
        veena = findViewById<CheckBox>(R.id.veena)
        sitar = findViewById<CheckBox>(R.id.sitar)
        algoza = findViewById<CheckBox>(R.id.algoza)
        saranga = findViewById<CheckBox>(R.id.saranga)
        tabla = findViewById<CheckBox>(R.id.tabla)
        tavil = findViewById<CheckBox>(R.id.tavil)
        var username = intent.getStringExtra("username")
        var location = intent.getStringExtra("location")
        var phone = intent.getStringExtra("phone")
        var youtubeLinks = intent.getStringExtra("youtubeLinks")
        var userType = intent.getStringExtra("userType")
        var password = intent.getStringExtra("password")
        var name: String? = intent.getStringExtra("name")
        var signupViewModel: SignupViewModel =
            ViewModelProviders.of(this, SignupViewModelFactory())
                .get(SignupViewModel::class.java)

        register.setOnClickListener {
            auth.createUserWithEmailAndPassword(username.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        signupViewModel.signup(
                            username,
                            location,
                            phone,
                            youtubeLinks,
                            password,
                            userType,
                            name,
                            instrumentsList
                        )
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Alert")
                        builder.setMessage("User Registered")
                        builder.setCancelable(true);
                        builder.setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            });
                        builder.show()
                    } else {
                        UpdateUI()
                    }

                }
            cancel.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent);
            }
        }
        }
        fun UpdateUI() {
            // TODO : initiate successful logged in experience
            Toast.makeText(
                applicationContext,
                "something wrong with the user registration",
                Toast.LENGTH_LONG
            ).show()
        }

        fun read(b:Array<Byte>,off:Int=0,len:Int=b.size,)
        {
            
        }
        fun onCheckboxClicked(view: View) {
            if (veena.isChecked) {
                if (!instrumentsList.contains("veena"))
                    instrumentsList.add("veena")
            } else {
                if (instrumentsList.contains("veena"))
                    instrumentsList.remove("veena")
            }
            if (saranga.isChecked) {
                if (!instrumentsList.contains("saranga"))
                    instrumentsList.add("saranga")
            } else {
                if (instrumentsList.contains("saranga"))
                    instrumentsList.remove("saranga")
            }
            if (sitar.isChecked) {
                if (!instrumentsList.contains("sitar"))
                    instrumentsList.add("sitar")
            } else {
                if (instrumentsList.contains("sitar"))
                    instrumentsList.remove("sitar")
            }
            if (algoza.isChecked) {
                if (!instrumentsList.contains("algoza"))
                    instrumentsList.add("algoza")
            } else {
                if (instrumentsList.contains("algoza"))
                    instrumentsList.remove("algoza")
            }
            if (tavil.isChecked) {
                if (!instrumentsList.contains("tavil"))
                    instrumentsList.add("tavil")
            } else {
                if (instrumentsList.contains("tavil"))
                    instrumentsList.remove("tavil")
            }
            if (tabla.isChecked) {
                if (!instrumentsList.contains("tabla"))
                    instrumentsList.add("tabla")
            } else {
                if (instrumentsList.contains("tabla"))
                    instrumentsList.remove("tabla")
            }
            register.isEnabled =
                veena.isChecked || saranga.isChecked || sitar.isChecked || algoza.isChecked || tavil.isChecked || tabla.isChecked
        }

    }

