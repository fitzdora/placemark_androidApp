package org.wit.placemark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.placemark.databinding.CardPlacemarkBinding
import org.wit.placemark.models.PlacemarkModel

interface PlacemarkListener {
    fun onPlacemarkClick(placemark: PlacemarkModel, position: Int)
}

class PlacemarkAdapter constructor(private var placemarks: List<PlacemarkModel>, private val listener: PlacemarkListener) :
    RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(placemark: PlacemarkModel, listener: PlacemarkListener) {
            binding.placemarkTitle.text = placemark.title
            binding.description.text = placemark.description
            Picasso.get().load(placemark.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener {
                listener.onPlacemarkClick(placemark, adapterPosition)
            }
        }
    }
}