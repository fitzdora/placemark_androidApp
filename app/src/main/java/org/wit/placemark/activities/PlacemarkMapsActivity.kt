package org.wit.placemark.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.wit.placemark.databinding.ActivityPlacemarkMapsBinding

class PlacemarkMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    }

}