package com.glovo.utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun Activity.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_INDEFINITE) =
    findViewById<View>(android.R.id.content).snackbar(text, duration)

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_INDEFINITE) =
    Snackbar.make(this, text, duration).show()
