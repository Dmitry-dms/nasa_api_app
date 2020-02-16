package com.dms.nasaapi

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.PictureOfTheDay
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


  //  private lateinit var viewModel: MainActivityViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery
            ), drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)



//        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
//            .create(MainActivityViewModel::class.java)
//
//        viewModel.getApod()?.observe(this, Observer {
//            if (it != null) {
//               // displayData(it)
//                Log.d("TAG","success vm")
//            }
//            else  {Log.d("TAG", "error vm")
//
//            }
//
//        })

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


//    private fun displayData(list: PictureOfTheDay) {
//        date.text = list.date
//        title_api.text = list.title
//        explanation.text = list.explanation
//        Glide.with(this)
//            .load(list.picture)
//            .into(image)
//        Log.d("TAG",list.title?: "ds")
//        Log.d("TAG",list.date?: "ds")
//        Log.d("TAG",list.explanation?: "ds")
//      }
}



