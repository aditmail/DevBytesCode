package com.example.devbytesexercice.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.devbytesexercice.R
import com.example.devbytesexercice.database.getDatabase
import com.example.devbytesexercice.databinding.ActivityMainBinding
import com.example.devbytesexercice.ui.fragment.DetailVideoFragmentArgs
import com.example.devbytesexercice.viewmodels.DetailVideoViewModel
import kotlinx.android.synthetic.main.fragment_list_video.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    private lateinit var bindingMain: ActivityMainBinding

    val viewModelMain: MainActivityViewModel by lazy {
        ViewModelProviders.of(this)
            .get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingMain.lifecycleOwner = this

        navController = this.findNavController(R.id.navhost_fragment)
        appBarConfig = AppBarConfiguration(navController.graph)

        //bindingMain.toolbar.setupWithNavController(navController, appBarConfig)
        //bindingMain.collapsingToolbarLayout
          //  .setupWithNavController(bindingMain.toolbar, navController, appBarConfig)
        //NavigationUI.setupActionBarWithNavController(this, navController)

        //Dynamically Change Title Toolbar for Fragments..
        //Or could user Navigation Label in the XML
        /*viewModelMain._navigationTitle.observe(this, Observer {
            supportActionBar?.title = it
        })*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
        //return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}

class MainActivityViewModel : ViewModel() {
    var _navigationTitle = MutableLiveData<String>()
}
