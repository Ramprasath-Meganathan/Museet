package com.example.museet.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.museet.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeOptions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeoptions)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        var badge: BadgeDrawable = navView.getOrCreateBadge(R.id.navigation_notifications)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

//        val query = FirebaseFirestore.getInstance().collection("connectionrequests")
//            .whereEqualTo("connectionRequestedTo", Firebase.auth.currentUser?.email.toString())
//            .whereEqualTo("connection", "pending")
//        query.get().addOnSuccessListener()
//        { document ->
//            Log.d(ContentValues.TAG, "document values:${document.documents}")
//            if (!document.isEmpty) {
//                badge.isVisible = true
//                badge.number = document.size()
//
//            }
//        }


//        val menu: Menu = navView.getMenu()
//        menu.findItem(R.id.navigation_notifications).setOnMenuItemClickListener{
//
//        }

//            navView.menu.findItem(R.id.navigation_notifications).setOnMenuItemClickListener() {
//                badge.isVisible = false
//                getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.nav_host_fragment,  NotificationsFragment ()).commit()
//                true
//            }


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @Override
    public override fun onBackPressed() {
        if (Firebase.auth.currentUser != null) {
            Toast.makeText(
                this,
                "User still logged in",
                Toast.LENGTH_LONG
            ).show()

        }
    }


        }

