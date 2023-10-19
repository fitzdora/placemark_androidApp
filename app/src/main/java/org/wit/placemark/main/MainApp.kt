package org.wit.placemark.main

import android.app.Application
import org.wit.placemark.models.PlacemarkMemStore
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.models.PlacemarkJSONStore
import org.wit.placemark.models.PlacemarkStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

   // val placemarks = ArrayList<PlacemarkModel>()
   lateinit var placemarks : PlacemarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        //placemarks = PlacemarkMemStore()
        i("Placemark Started")
        /*placemarks.add(PlacemarkModel("One", "About one..."))
        placemarks.add(PlacemarkModel("Two", "About two..."))
        placemarks.add(PlacemarkModel("Three", "About three..."))*/
    }
}