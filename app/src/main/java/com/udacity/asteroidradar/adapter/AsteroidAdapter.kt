package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid

class AsteroidAdapter : RecyclerView.Adapter<AsteroidViewHolder>() {
    var data = listOf<Asteroid>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = data[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
    }
}