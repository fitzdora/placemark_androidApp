package org.wit.placemark.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber.Forest.i

class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    var placemark = PlacemarkModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    // var location = Location(52.245696, -7.139102, 15f)
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edit = false

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Placemark Activity started...")


        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            binding.btnAdd.setText(R.string.button_savePlacemark)
            Picasso.get()
                .load(placemark.image)
                .into(binding.placemarkImage)
            if (placemark.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_placemark_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            i("PlacemarkActivity","Add button clicked") //log the button click
           try{ placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            if (placemark.title.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_placemark_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }
            }
            i("add Button Pressed: $placemark")
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            i("error creating placemark: $e")
        }
        }

        binding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher, this)
        }

        binding.placemarkLocation.setOnClickListener {
            i("Set location Pressed")
            val location = Location(52.245696, -7.139102, 15f)
            if (placemark.zoom != 0f) {
                location.lat = placemark.lat
                location.lng = placemark.lng
                location.zoom = placemark.zoom
            }
            val launcherIntent =
                Intent(this, MapActivity::class.java).putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99) // might need to changed this back to 99
                app.placemarks.delete(placemark)
                finish()
            }
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            // placemark.image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            placemark.image = image

                            Picasso.get()
                                .load(placemark.image)
                                .into(binding.placemarkImage)
                            binding.chooseImage.setText(R.string.change_placemark_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            when(result.resultCode){
                RESULT_OK -> {
                    if (result.data != null) {
                        i("Got Location ${result.data.toString()}")
                        val location = result.data!!.extras?.getParcelable<Location>("location")!!
                        i("Location == $location")
                        placemark.lat = location.lat
                        placemark.lng = location.lng
                        placemark.zoom = location.zoom
                    } //end of if
                }
                RESULT_CANCELED -> { } else -> { }
            }
        }
    }
}