package com.inavi.myapplication.model

import android.view.View

interface OnItemClick {
    fun onItemClick(v: View, position: Int)
}