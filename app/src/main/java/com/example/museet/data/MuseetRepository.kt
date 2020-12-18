package com.example.museet.data

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log

import com.google.firebase.firestore.FirebaseFirestore

//Repository class for calling methods for registering the user
class MuseetRepository  {

    fun register(username: String?,location:String?,phone:String?,youtubeLink: String?,password: String?,userType:String?,name:String?,
                 instruments:MutableList<String>)
    {
        val newUser =       hashMapOf(
            "username" to username,
            "location" to location,
            "phone" to phone,
            "youtubeLinks" to youtubeLink,
            "password" to password,
            "usertype" to userType,
         "name" to name,
        "instruments" to instruments)
        val collection = FirebaseFirestore.getInstance().collection("logincredentials")
        collection.add(newUser)
            .addOnSuccessListener {documentReference ->
                System.out.println("successfully inserted the values")
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }.addOnFailureListener {
                System.out.println("failed to register user")
            }
    }

    fun Login(username: String, password: String)
    {
        data class User(val username: String, val password:String)
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("logincredentials")
        collection.get().addOnSuccessListener { result->System.out.println(result) }
            .addOnFailureListener { System.out.println("user not found") }
        }


}