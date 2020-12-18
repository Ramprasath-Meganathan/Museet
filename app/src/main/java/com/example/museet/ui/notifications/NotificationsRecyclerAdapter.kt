package com.example.museet.ui.notifications

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.NotificationsDataModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_item.view.name
import kotlinx.android.synthetic.main.fragment_listnotifications.view.*

class NotificationsRecyclerAdapter(var connectionRequests:MutableList<NotificationsDataModel>) : RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.fragment_listnotifications, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(connectionRequests[position])
    }

    override fun getItemCount(): Int {
        return connectionRequests.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(notificationsDataModel: NotificationsDataModel) {
            itemView.name.text = notificationsDataModel.connectionRequestFrom
            itemView.accept.setOnClickListener {
                notificationsAcceptancelistener(notificationsDataModel)
                connectionRequests.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                if (connectionRequests.size == 0) {
                    Toast.makeText(
                        itemView.context,
                        "No more pending users",
                        Toast.LENGTH_LONG
                    ).show()
                }
                notifyItemRangeChanged(adapterPosition, connectionRequests.size)
                Toast.makeText(itemView.context, "Request sent", Toast.LENGTH_LONG).show()
            }
            itemView.deny.setOnClickListener{
                notificationsRejectionlistener(notificationsDataModel)
                connectionRequests.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                if (connectionRequests.size == 0) {
                    Toast.makeText(
                        itemView.context,
                        "No more pending users",
                        Toast.LENGTH_LONG
                    ).show()
                }
                notifyItemRangeChanged(adapterPosition, connectionRequests.size)
                Toast.makeText(itemView.context, "Request denied", Toast.LENGTH_LONG).show()
            }
        }

    }

}

    fun notificationsAcceptancelistener(notificationsDataModel: NotificationsDataModel) {
        val query = FirebaseFirestore.getInstance().collection("connectionrequests").document(notificationsDataModel.documentReference)
            .update( "connection","accepted").addOnSuccessListener() {document->
            Log.d(ContentValues.TAG, "document values:${document}")
                val newUser =       hashMapOf(
                    "connectionRequestFrom" to notificationsDataModel.connectionRequestedTo,
                    "connectionRequestedTo" to notificationsDataModel.connectionRequestFrom,
                    "connection" to "accepted",
                    "connectionRequestedToUserName" to notificationsDataModel.connectionRequestedFromUserName,
                    "connectionRequestedFromUserName" to notificationsDataModel.connectionRequestedToUserName)
                val queryToAdd = FirebaseFirestore.getInstance().collection("connectionrequests")
                    .add(newUser).addOnSuccessListener {   Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}") }
                    .addOnFailureListener{  Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.message}")}
        }.addOnFailureListener { Log.d(ContentValues.TAG, "${it.message}") }
                }


    fun notificationsRejectionlistener(notificationsDataModel: NotificationsDataModel) {

        val query = FirebaseFirestore.getInstance().collection("connectionrequests").document(notificationsDataModel.documentReference)
            .update( "connection","rejected").addOnSuccessListener() {document->
                Log.d(ContentValues.TAG, "document values:${document}")
            }.addOnFailureListener { Log.d(ContentValues.TAG, "${it.message}") }

            }



