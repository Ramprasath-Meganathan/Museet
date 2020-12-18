package com.example.museet.ui.Connections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.museet.R
import com.example.museet.data.ConnectionRequests
import com.example.museet.data.NewUserDataModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_item.view.name
import kotlinx.android.synthetic.main.fragment_listconnections.view.*

class ConnectionsRecyclerAdapter(var connectionsList: MutableList<ConnectionRequests>) : RecyclerView.Adapter<ConnectionsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.fragment_listconnections, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bind(connectionsList[position])
    }

    override fun getItemCount(): Int {
        return connectionsList.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(connectionsModel : ConnectionRequests)
        {
                var query = FirebaseFirestore.getInstance().collection("logincredentials")
                    .whereEqualTo("username", connectionsModel.connectionRequestFrom)
                    .get().addOnSuccessListener {
                        for (doc in it) {
                            var viewModel = doc.toObject(NewUserDataModel::class.java)
                            itemView.name.text = viewModel.name.take(10)
                            itemView.phone.text = viewModel.phone.take(11)
                            itemView.email.text = viewModel.username.take(10)
                        }
                    }
            }
        }

}
