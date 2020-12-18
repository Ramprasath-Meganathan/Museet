package com.example.museet.ui.home

import android.app.AlertDialog
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.HomeDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_item.view.*

class MusiciansRecyclerAdapter(var musiciansList: MutableList<HomeDataModel>) : RecyclerView.Adapter<MusiciansRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bind(musiciansList[position])
    }

    override fun getItemCount(): Int {
        return musiciansList.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(homedataModel : HomeDataModel)
        {
            itemView.name.text = homedataModel.name.take(4)
            itemView.userType.text = homedataModel.usertype
            itemView.youtubeLinks.text = homedataModel.youtubeLinks.take(16)
            itemView.connect.setOnClickListener{listener(homedataModel)
                musiciansList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
                if(musiciansList.size==0)
                {
                    Toast.makeText(
                        itemView.context,
                        "No more pending users",
                        Toast.LENGTH_LONG
                    ).show()
                }
            notifyItemRangeChanged(adapterPosition,musiciansList.size)
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Alert")
                builder.setMessage("Connection Request sent to "+homedataModel.name)
                builder.setCancelable(true);
                builder.setPositiveButton(
                    "Ok", null
                );
                builder.show()
            }

        }

    }

    fun listener(homedataModel: HomeDataModel) {
        var query = FirebaseFirestore.getInstance().collection("logincredentials").limit(1)
            .whereEqualTo("username",Firebase.auth.currentUser?.email.toString()).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    var user = it.documents[0]?.data?.get("name")!!.toString()
                    val newconnectionRequest = hashMapOf(
                        "connectionRequestedTo" to homedataModel.username,
                        "connection" to "pending",
                        "connectionRequestedFromUserName" to user,
                        "connectionRequestedToUserName" to homedataModel.name,
                        "connectionRequestFrom" to Firebase.auth.currentUser?.email.toString()
                    )
                    val collection =
                        FirebaseFirestore.getInstance().collection("connectionrequests")
                    collection.add(newconnectionRequest)
                        .addOnSuccessListener { documentReference ->
                            System.out.println("successfully inserted the values")
                            Log.d(
                                ContentValues.TAG,
                                "DocumentSnapshot added with ID: ${documentReference.id}"
                            )
                        }.addOnFailureListener {
                            Log.d(ContentValues.TAG,it.message.toString())
                        }
                }
            }
    }

    }
