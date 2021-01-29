package `in`.phoenix.currencyconverter.util

import android.view.View

/**
 * Created by Charan on January 29, 2021
 */

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.isVisibleOnScreen() = this.visibility == View.VISIBLE

fun View.isVisible() = this.visibility == View.VISIBLE || this.visibility == View.INVISIBLE