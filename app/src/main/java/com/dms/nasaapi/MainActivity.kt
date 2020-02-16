package com.dms.nasaapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.api.RetrofitClient
import com.dms.nasaapi.model.PictureOfTheDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainActivityViewModel::class.java)

        viewModel.getApod()?.observe(this, Observer {
            if (it != null) {
                displayData(it)
                Log.d("TAG","success vm")
            }
            else  {Log.d("TAG", "error vm")

            }

        })
    }


    private fun displayData(list: PictureOfTheDay) {
        date.text = list.date
        title_api.text = list.title
        explanation.text = list.explanation
        Glide.with(this)
            .load(list.picture)
            .into(image)
//        Log.d("TAG",list.title?: "ds")
//        Log.d("TAG",list.date?: "ds")
//        Log.d("TAG",list.explanation?: "ds")
    }
}
