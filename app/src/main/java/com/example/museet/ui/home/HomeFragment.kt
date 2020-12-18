package com.example.museet.ui.home

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.ConnectionRequests
import com.example.museet.data.HomeDataModel
import com.example.museet.ui.login.LoginActivity
import com.example.museet.ui.login.afterTextChanged
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.location


class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    var homeDataList : MutableList<HomeDataModel> = ArrayList()
    homeViewModel =
    ViewModelProviders.of(this,HomeViewModelFactory()).get(HomeViewModel::class.java)

    val user = Firebase.auth.currentUser
    val root = inflater.inflate(R.layout.fragment_home, container, false)
    val textLocation = root.findViewById<EditText>(R.id.location)
//    val helpLink = root.findViewById<TextView>(R.id.helpLink)
    val search = root.findViewById<Button>(R.id.search)
    val logout = root.findViewById<Button>(R.id.logout)
    val musicianList: RecyclerView = root.findViewById<RecyclerView>(R.id.musician_recycler_view)
    var connect = root.findViewById<TextView>(R.id.connect)
    var connectionRequests : List<ConnectionRequests> = ArrayList()

    homeViewModel.homeFormState.observe(viewLifecycleOwner, Observer {
      var homeFormState = it?: return@Observer
      search.isEnabled = homeFormState.isDataValid

      if (homeFormState.locationError != null) {
        location.error = getString(homeFormState.locationError!!)
      }
    })

    textLocation.afterTextChanged {
      homeViewModel.homeDataChanged(
        location.text.toString()
      )
    }


      logout.setOnClickListener {
        Firebase.auth.signOut()
        val intent=Intent(activity,LoginActivity::class.java)
        startActivity(intent)
      }


//Initially had plan to create a help link but couldn't do it due to time constraint
//      helpLink.setOnClickListener{
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Alert")
//        builder.setMessage("Yet to be built")
//        builder.setCancelable(true);
//        builder.setPositiveButton("Ok", null
//        );
//        builder.show()
//      }

    search.setOnClickListener {
      homeDataList.clear()
      var imm =
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view?.windowToken, 0)
      val db = FirebaseFirestore.getInstance()
      var users =  db.collection("logincredentials").whereEqualTo("location", location.text.toString())
        .get().addOnSuccessListener()
        { locationusers ->
          if (!locationusers.isEmpty) {
            var imm =
              activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            var listOfUsers = locationusers!!.toObjects(HomeDataModel::class.java)
            for (user in listOfUsers) {
              val query  =
                db.collection("connectionrequests")
                  .whereEqualTo("connectionRequestedTo", user.username).whereEqualTo( "connection","accepted")
                  .get()
                  .addOnSuccessListener {
                    if (!it.isEmpty) {
                      for (doc in it) {
                        var connections = doc!!.toObject(ConnectionRequests::class.java)
                        if (connections == null) {
                          homeDataList.add(user)
                        }
                      }
                    }
                    else
                    {
                      homeDataList.add(user)
                    }

                    if (homeDataList.isEmpty()) {
                      Toast.makeText(
                        context,
                        "no users found for the given location",
                        Toast.LENGTH_LONG
                      ).show()
                      val adapter = MusiciansRecyclerAdapter(homeDataList)
                      adapter.notifyDataSetChanged()
                      musicianList.layoutManager = LinearLayoutManager(activity)
                      musicianList.adapter = adapter
                    } else {
                      val adapter = MusiciansRecyclerAdapter(homeDataList )
                      adapter.notifyDataSetChanged()
                      musicianList.layoutManager = LinearLayoutManager(activity)
                      musicianList.adapter = adapter
                    }

                  }.addOnFailureListener{
                    Log.d(ContentValues.TAG, "Error:${it.message!!}")
                  }
            }

          }
          else
          {
            Toast.makeText(
              context,
              "no users found for the given location",
              Toast.LENGTH_LONG
            ).show()
          }

        }
    }
    return root
  }

}