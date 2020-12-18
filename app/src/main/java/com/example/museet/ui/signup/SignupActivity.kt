package com.example.museet.ui.signup

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*

import com.example.museet.R
import com.example.museet.ui.HomeOptions
import com.example.museet.ui.login.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.math.sign

class SignupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var signupViewModel: SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.username)
        val phone = findViewById<EditText>(R.id.phone)
        val location = findViewById<EditText>(R.id.location)
        val password = findViewById<EditText>(R.id.password)
        val youtubeLinks = findViewById<EditText>(R.id.youtubeLinks)
        val userType = findViewById<Spinner>(R.id.userType)
        val name = findViewById<EditText>(R.id.name)
        userType.onItemSelectedListener = this
        val nextButton = findViewById<Button>(R.id.next_button)
        val cancelButton = findViewById<TextView>(R.id.cancel_button)
        val loading = findViewById<ProgressBar>(R.id.loading)

        signupViewModel = ViewModelProviders.of(this, SignupViewModelFactory())
            .get(SignupViewModel::class.java)

        signupViewModel.signupResult.observe(this@SignupActivity, Observer {
            val signupResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (signupResult.error != null) {
                showSignupFailed(signupResult.error)
            }
            if (signupResult.success != null) {
                updateUiWithUser(signupResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.userTypeArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            userType.adapter = adapter
        }

        signupViewModel.signupFormState.observe(this@SignupActivity, Observer {
            val signupState = it ?: return@Observer

            // disable login button unless both username / password is valid
            nextButton.isEnabled = signupState.isDataValid

            if (signupState.usernameError != null) {
                username.error = getString(signupState.usernameError)
            }
            if (signupState.phoneError != null) {
                phone.error = getString(signupState.phoneError)
            }

            if (signupState.youtubeLinkError != null) {
                youtubeLinks.error = getString(signupState.youtubeLinkError)
            }
            if (signupState.passwordError != null) {
                password.error = getString(signupState.passwordError)
            }
            if (signupState.locationError != null) {
                location.error = getString(signupState.locationError)
            }
            if(signupState.nameError!=null){
                name.error = getString(signupState.nameError)
            }
        })

        username.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                location.text.toString(),
                phone.text.toString(),
                youtubeLinks.text.toString(),
                password.text.toString(),
                name.text.toString()

            )
        }


        phone.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                location.text.toString(),
                phone.text.toString(),
                youtubeLinks.text.toString(),
                password.text.toString(),
                name.text.toString()

            )
        }

        youtubeLinks.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                location.text.toString(),
                phone.text.toString(),
                youtubeLinks.text.toString(),
                password.text.toString(),
                name.text.toString()
            )
        }
        location.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                location.text.toString(),
                phone.text.toString(),
                youtubeLinks.text.toString(),
                password.text.toString(),
                name.text.toString()
            )
        }
        userType.onItemSelectedListener {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                location.text.toString(),
                phone.text.toString(),
                youtubeLinks.text.toString(),
                password.text.toString(),
                name.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signupViewModel.signupDataChanged(
                    username.text.toString(),
                    location.text.toString(),
                    phone.text.toString(),
                    youtubeLinks.text.toString(),
                    password.text.toString(),
                    name.text.toString()
                )
            }


            nextButton.setOnClickListener {
                val query = FirebaseFirestore.getInstance().collection("logincredentials")
                    .whereEqualTo("username",username.text.toString()).get()
                    .addOnSuccessListener {
                        if (it.isEmpty) {
                            loading.visibility = View.VISIBLE
                            val intent = Intent(context, InstrumentSelectionActivity::class.java)
                            intent.putExtra("username", username.text.toString())
                            intent.putExtra("phone", phone.text.toString())
                            intent.putExtra("location", location.text.toString())
                            intent.putExtra("youtubeLinks", youtubeLinks.text.toString())
                            intent.putExtra("password", password.text.toString())
                            intent.putExtra("name", name.text.toString())
                            intent.putExtra("userType", userType.selectedItem.toString())
                            startActivity(intent);
                            loading.visibility = View.GONE
                            closeKeyBoard()
                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Alert")
                            builder.setMessage("User already registered. Please select a different user name")
                            builder.setCancelable(true);
                            builder.setPositiveButton(
                                "Ok", null
                            );
                            builder.show()

                        }
                    }
            }

            cancelButton.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent);
                closeKeyBoard()
            }
        }
    }
    private fun updateUiWithUser(model: SignupUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSignupFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var item:String  = parent?.getItemAtPosition(position).toString();
    }
}

private fun Spinner.onItemSelectedListener(function: () -> Unit) {

}


