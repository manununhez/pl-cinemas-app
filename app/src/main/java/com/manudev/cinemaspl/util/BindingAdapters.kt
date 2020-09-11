/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.manudev.cinemaspl.util

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.elevation.ElevationOverlayProvider
import com.manudev.cinemaspl.R
import java.util.*


/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl", "circleCrop", requireAll = false)
fun bindImage(imgView: ImageView, imgUrl: String?, circleCrop: Boolean) {
    imgUrl?.let {

        val scheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) "https" else "http"

        val imgUri = imgUrl.toUri().buildUpon().scheme(scheme).build()
        Log.d(BindingAdapter::class.java.simpleName, imgUri.toString())
        val request = Glide.with(imgView.context).load(imgUri)
        if (circleCrop) request.circleCrop()

        request.apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        )

        request.timeout(2000)
        request.into(imgView)
    }
}

@BindingAdapter("elevationOverlay")
fun View.bindElevationOverlay(previousElevation: Float, elevation: Float) {
    if (previousElevation == elevation) return
    val color = ElevationOverlayProvider(context)
        .compositeOverlayWithThemeSurfaceColorIfNeeded(elevation)
    setBackgroundColor(color)
}

@BindingAdapter("classification")
fun bindClassification(view: TextView, str: String) {
    view.text = str.split("|").joinToString(" ") { it.capitalize(Locale.getDefault()) }
}



