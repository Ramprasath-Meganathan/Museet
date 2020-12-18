package com.example.museet.ui.notifications

import android.app.Notification
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.HomeDataModel
import com.example.museet.data.NotificationsDataModel
import com.example.museet.ui.home.MusiciansRecyclerAdapter
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_homeoptions.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_listnotifications.*

class NotificationsFragment : Fragment() {

  private lateinit var notificationsViewModel: NotificationsViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
    ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_notifications, container, false)
    val textView: TextView = root.findViewById(R.id.text_notifications)
    var notificationDataList : MutableList<NotificationsDataModel> = ArrayList()
    val notificationsList: RecyclerView = root.findViewById<RecyclerView>(R.id.notifications_recycler_View)
    val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser?.email.toString()
    val query = db.collection("connectionrequests").whereEqualTo("connectionRequestedTo",user)
      .whereEqualTo("connection","pending")
    query.get().addOnSuccessListener(){document->
      Log.d(ContentValues.TAG,"document values:${document.documents}")
      if(!document.isEmpty) {
        for(document in document)
        {
          var notificationModel = document.toObject(NotificationsDataModel::class.java)
          notificationModel.documentReference = document.reference.id
          notificationDataList.add(notificationModel)
        }
        //tried to add a notification badge but didn't work as expected so have commented it out
//        val navViewValue: BottomNavigationView = root.findViewById(R.id.nav_view)
//        val badgeDrawable = navViewValue.getBadge(R.id.navigation_notifications)
//        if (badgeDrawable != null) {
//          badgeDrawable.isVisible = false
//          badgeDrawable.clearNumber()
//        }
//        notificationDataList = document!!.toObjects(NotificationsDataModel::class.java)
        val adapter = NotificationsRecyclerAdapter(notificationDataList)
        adapter.notifyDataSetChanged()
        notificationsList.layoutManager = LinearLayoutManager(activity)
        notificationsList.adapter = adapter
      }
      else
      {
        Toast.makeText(
          context,
          "No pending requests",
          Toast.LENGTH_LONG
        ).show()

      }
      var imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }.addOnFailureListener {
      Log.d(ContentValues.TAG,"Error:${it.message!!}") }
    return root
  }
}