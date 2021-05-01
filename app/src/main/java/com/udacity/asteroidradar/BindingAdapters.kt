package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.udacity.asteroidradar.main.NeoApiStatus
import com.udacity.asteroidradar.network.NeoApi

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.apply {
            setImageResource(R.drawable.asteroid_hazardous)
            contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
        }
    } else {
        imageView.apply {
            setImageResource(R.drawable.asteroid_safe)
            contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
        }
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (pictureOfDay.mediaType == "image") {
            Glide
                .with(imageView.context)
                .load(pictureOfDay.url)
                .into(imageView)
        }
    }

}

@BindingAdapter("neoApiStatus")
fun bindStatus(progressBar: ProgressBar, status: NeoApiStatus) {
    when (status) {
        NeoApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        NeoApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
        NeoApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}
