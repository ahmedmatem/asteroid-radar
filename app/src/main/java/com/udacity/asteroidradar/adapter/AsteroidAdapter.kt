package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {
    var data = listOf<Asteroid>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = data[position]
        holder.codeName.text = item.codename
        holder.closeDate.text = item.closeApproachDate
        if(item.isPotentiallyHazardous){
            holder.hazardIcon.setImageResource(R.drawable.ic_status_potentially_hazardous)
        } else {
            holder.hazardIcon.setImageResource(R.drawable.ic_status_normal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false)
        return AsteroidViewHolder(view)

    }

    class AsteroidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codeName = itemView.findViewById<TextView>(R.id.code_name)
        val closeDate = itemView.findViewById<TextView>(R.id.close_date)
        val hazardIcon: ImageView = itemView.findViewById(R.id.hazard_icon)
    }
}