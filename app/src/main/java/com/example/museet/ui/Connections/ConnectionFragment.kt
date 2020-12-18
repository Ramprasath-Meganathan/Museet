package com.example.museet.ui.Connections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.ConnectionRequests
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ConnectionFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    var connectionList: MutableList<ConnectionRequests> = ArrayList()
    val root = inflater.inflate(R.layout.fragment_connections, container, false)
    val connectionRecylerView: RecyclerView = root.findViewById<RecyclerView>(R.id.connections_recyclerView)
    val query = FirebaseFirestore.getInstance().collection("connectionrequests").whereEqualTo(
      "connectionRequestedTo", Firebase.auth.currentUser?.email.toString()
    ).whereEqualTo("connection", "accepted")
      .get().addOnSuccessListener {
        if (!it.isEmpty) {
          connectionList = it!!.toObjects(ConnectionRequests::class.java)
          var adapter = ConnectionsRecyclerAdapter(connectionList)
          connectionRecylerView.layoutManager = LinearLayoutManager(activity)
          connectionRecylerView.adapter = adapter
        }
        else
        {
          Toast.makeText(
            context,
            "No connections",
            Toast.LENGTH_LONG
          ).show()
        }
      }
    return root
  }
}
