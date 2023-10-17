package org.wit.placemark.main

import android.app.Application
import org.wit.placemark.models.PlacemarkMemStore
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

   // val placemarks = ArrayList<PlacemarkModel>()
   lateinit var placemarks : PlacemarkMemStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkMemStore()
        i("Placemark Started")
        /*placemarks.add(PlacemarkModel("One", "About one..."))
        placemarks.add(PlacemarkModel("Two", "About two..."))
        placemarks.add(PlacemarkModel("Three", "About three..."))*/
    }
}