package com.example.museet.data


data class HomeDataModel (
    val username: String="",
    val phone: String="",
    val location: String="",
    val usertype: String="",
    val password: String="",
    val youtubeLinks : String = "",
    val name:String="",
    val instruments: List<String> = ArrayList()
)


data class NotificationsDataModel (
    val connectionRequestedTo: String="",
    val connection: String="",
    val connectionRequestedToUserName : String = "",
    val connectionRequestFrom: String ="",
    val connectionRequestedFromUserName : String = "",
    var documentReference:String=""
)

data class ProfileDataModel (
    val username: String="",
    val phone: String="",
    val location: String="",
    val usertype: String="",
    val password: String="",
    val youtubeLinks : String = "",
    val name:String="",
    val instruments: List<String> = ArrayList()
)


data class LoggedInUser(
    val userId: String,
    val displayName: String
)

data class NewUserDataModel (
    val username: String="",
    val phone: String="",
    val location: String="",
    val usertype: String="",
    val password: String="",
    val youtubeLinks : String = "",
    val name:String="",
    val instruments: List<String> = ArrayList()
)

data class ConnectionRequests (
    val connectionRequestedTo: String="",
    val connection: String="",
    val connectionRequestedToUserName : String = "",
    val connectionRequestFrom: String ="",
    val connectionRequestedFromUserName : String = "",
    var documentReference:String=""
)

data class LoginUser(
val username: String,
val password:String
)