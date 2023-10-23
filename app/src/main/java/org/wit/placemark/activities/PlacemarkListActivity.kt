package org.wit.placemark.activities

import android.content.Intent
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkListBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.adapters.PlacemarkAdapter
import org.wit.placemark.adapters.PlacemarkListener
import org.wit.placemark.models.PlacemarkModel

class PlacemarkListActivity : AppCompatActivity(), PlacemarkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPlacemarkListBinding
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacemarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
       //binding.recyclerView.adapter = PlacemarkAdapter(app.placemarks)
        binding.recyclerView.adapter = PlacemarkAdapter(app.placemarks.findAll(),this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PlacemarkActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                //(binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.placemarks.size)
                (binding.recyclerView.adapter)?. notifyItemRangeChanged(0, app.placemarks.findAll().size)
            }
        }

    override fun onPlacemarkClick(placemark: PlacemarkModel, pos: Int) {
        val launcherIntent = Intent(this, PlacemarkActivity::class.java)
        launcherIntent.putExtra("placemark_edit", placemark)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK){
            (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.placemarks.findAll().size)
        }
        else // Deleting code
            if (it.resultCode == 99)    (binding.recyclerView.adapter)?.notifyItemRemoved(position)
    }
}
