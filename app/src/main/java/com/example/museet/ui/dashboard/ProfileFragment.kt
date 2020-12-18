package com.example.museet.ui.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museet.R
import com.example.museet.data.HomeDataModel
import com.example.museet.data.ProfileDataModel
import com.example.museet.ui.home.HomeFragment
import com.example.museet.ui.home.HomeViewModel
import com.example.museet.ui.home.MusiciansRecyclerAdapter
import com.example.museet.ui.signup.SignupViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
  var userEmail ="";
  var documentReference="";
  private lateinit var profileViewModel: ProfileViewModel
  private lateinit var auth: FirebaseAuth
  var profileList : List<ProfileDataModel> = ArrayList()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    profileViewModel =
      ViewModelProviders.of(this, UpdateProfileViewModelFactory()).get(ProfileViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_profile, container, false)
    val updateProfile = root.findViewById<Button>(R.id.update_profile)
//    val cancelButton = root.findViewById<Button>(R.id.cancel_button)
    val textView: TextView = root.findViewById(R.id.text_profile)
    val name = root.findViewById<EditText>(R.id.name)
    val phone = root.findViewById<EditText>(R.id.phone)
    val location = root.findViewById<EditText>(R.id.location)
//    val password = root.findViewById<EditText>(R.id.password)
    val youtubeLink = root.findViewById<EditText>(R.id.youtubeLinks)

    val youtubeLinks = root.findViewById<EditText>(R.id.youtubeLinks)
    profileViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
      val db = FirebaseFirestore.getInstance()
      val query = db.collection("logincredentials").whereEqualTo("username", Firebase.auth.currentUser?.email.toString()
      ).limit(1)
      query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
        querySnapshot?.let {
          if (!it.isEmpty) {
            for (doc in it) {
              location.setText(doc.getString("location"));
              name.setText(doc.getString("name"))
              userEmail= doc.getString("username").toString()
              documentReference = doc.reference.id
//            password.setText(doc.getString("password"))
              phone.setText(doc.getString("phone"))
              youtubeLinks.setText(doc.getString("youtubeLinks"))
              profileViewModel.profileUpdateForm.observe(viewLifecycleOwner, Observer {
                val profileFormState = it ?: return@Observer

                if (profileFormState.nameError != null) {
                  name.error = getString(profileFormState.nameError)
                }
                if (profileFormState.locationError != null) {
                  location.error = getString(profileFormState.locationError)
                }
                if (profileFormState.phoneError != null) {
                  phone.error = getString(profileFormState.phoneError)
                }
                if (profileFormState.youtubeLinkError != null) {
                  youtubeLinks.error = getString(profileFormState.youtubeLinkError)
                }
                // disable login button unless both username / password is valid
                updateProfile.isEnabled = profileFormState.isDataValid

              })

            }
          } else {
            System.out.println("user not found")
          }
        }
      }
    })




    name.afterTextChanged {
      profileViewModel.profileDataChanged(
        name.text.toString(),
        location.text.toString(),
        phone.text.toString(),
        youtubeLinks.text.toString()

      )
    }


    phone.afterTextChanged {
      profileViewModel.profileDataChanged(
        name.text.toString(),
        location.text.toString(),
        phone.text.toString(),
        youtubeLinks.text.toString()

      )
    }

    youtubeLinks.afterTextChanged {
      profileViewModel.profileDataChanged(
        name.text.toString(),
        location.text.toString(),
        phone.text.toString(),
        youtubeLinks.text.toString()
      )
    }
    location.afterTextChanged {
      profileViewModel.profileDataChanged(
        name.text.toString(),
        location.text.toString(),
        phone.text.toString(),
        youtubeLinks.text.toString()
      )
    }



      updateProfile.setOnClickListener {
        var imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        val db = FirebaseFirestore.getInstance()
        var doc = documentReference
        val query = db.collection("logincredentials").document(documentReference).update(
          "location",location.text.toString(),"name",name.text.toString(), "phone",phone.text.toString()
          ,"youtubeLinks",youtubeLinks.text.toString()
        )
        query.addOnSuccessListener() {document->
          Log.d(ContentValues.TAG, "document values:${document}")
                  val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Profile updated")
        builder.setCancelable(true);
        builder.setPositiveButton(
          "Ok", null
        );
          builder.show()
        }.addOnFailureListener { Log.d(ContentValues.TAG, "Error: something wrong") }

      }



      return root

  //  }

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

