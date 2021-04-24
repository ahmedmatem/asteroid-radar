package com.udacity.asteroidradar.utils.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

@BindingAdapter("codeName")
fun TextView.setCodeName(item: Asteroid?) {
    item?.let {
        text = item.codename
    }
}

@BindingAdapter("closeApproachDate")
fun TextView.setCloseDate(item: Asteroid?) {
    item?.let {
        text = item.closeApproachDate
    }
}

@BindingAdapter("hazardImage")
fun ImageView.setImage(item: Asteroid?) {
    item?.let {
        if (it.isPotentiallyHazardous) {
            setImageResource(R.drawable.ic_status_potentially_hazardous)
        } else {
            setImageResource(R.drawable.ic_status_normal)
        }
    }
}