package com.aneeq.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aneeq.bookhub.R
import com.aneeq.bookhub.fragment.AboutAppFragment
import com.aneeq.bookhub.fragment.DashboardFragment
import com.aneeq.bookhub.fragment.FavouritesFragment
import com.aneeq.bookhub.fragment.ProfileFragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorLayout:CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var collapsingtoolbar:CollapsingToolbarLayout
    var previousMenuItem: MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        collapsingtoolbar=findViewById(R.id.collapsingtoolbar)
        setUpToolbar()
        openDashBoard()



        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!=null) {
                previousMenuItem?.isChecked = false
            }
               it.isCheckable=true
                it.isChecked=true
                previousMenuItem=it        //highlight menu


            when(it.itemId){
                R.id.itDashboard ->{
                   openDashBoard()
                    drawerLayout.closeDrawers()

                    }
                R.id.itFavourites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourites"
                }
                R.id.itAbout_App ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutAppFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="About App"
                }
                R.id.itProfile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }

            }

            return@setNavigationItemSelectedListener true
        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
fun openDashBoard() {
    supportFragmentManager.beginTransaction()
        .replace(R.id.frame, DashboardFragment())
        .commit()
    supportActionBar?.title = "Dashboard"
    navigationView.setCheckedItem(R.id.itDashboard)
}

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment ->openDashBoard()
            else->super.onBackPressed()
        }
    }
}
