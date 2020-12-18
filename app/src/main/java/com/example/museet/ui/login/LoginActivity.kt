package com.example.museet.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.museet.R
import com.example.museet.ui.HomeOptions
import com.example.museet.ui.signup.SignupActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val register = findViewById<TextView>(R.id.registerLink)
        val loading = findViewById<ProgressBar>(R.id.loading)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid


            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })


        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                auth.signInWithEmailAndPassword(username.text.toString(),password.text.toString())
                    .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        loading.visibility = View.VISIBLE
                        val intent = Intent(context, HomeOptions::class.java)
                        startActivity(intent);
                        loading.visibility = View.GONE
                        val user = auth.currentUser?.email
                        closeKeyBoard()
                    } else {
                        UpdateUI()
                    }

                }

            }

            register.setOnClickListener{
                loading.visibility = View.VISIBLE
                val intent = Intent(context, SignupActivity::class.java)
                 startActivity(intent);
                loading.visibility =View.GONE
                closeKeyBoard()
            }

        }

    }
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun UpdateUI() {
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "Authentication failed, please try again",
            Toast.LENGTH_LONG
        ).show()
    }
    public override fun onStart() {
        super.onStart()

    }

    @Override
    public override fun onBackPressed(){
        if(auth.currentUser!=null)
        {
            
        }
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

