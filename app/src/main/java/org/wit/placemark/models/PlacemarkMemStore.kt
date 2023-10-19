package org.wit.placemark.models

import timber.log.Timber.Forest.i

var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

class PlacemarkMemStore : PlacemarkStore {
    val placemarks = ArrayList<PlacemarkModel>()

    override fun findAll(): List<PlacemarkModel> {
        return  placemarks
    }

    override fun create(placemark: PlacemarkModel) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }

    override fun update(placemark: PlacemarkModel) {
        var foundPlacemark: PlacemarkModel? = placemarks.find { p -> p.id == placemark.id }
        if (foundPlacemark != null){
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.lat = placemark.lat
            foundPlacemark.lng = placemark.lng
            foundPlacemark.zoom = placemark.zoom
            logAll()
        }
    }

    override fun delete(placemark: PlacemarkModel){
        placemarks.remove(placemark)
    }

    private fun logAll(){
        placemarks.forEach{ i("${it}") }
    }
}