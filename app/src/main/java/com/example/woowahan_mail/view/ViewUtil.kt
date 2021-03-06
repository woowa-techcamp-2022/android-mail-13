package com.example.woowahan_mail.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.woowahan_mail.R
import com.example.woowahan_mail.view.main.MainActivity
import com.google.android.material.navigation.NavigationView

fun Activity.getDeviceWidth(): Float {
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    var widthPixel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
    val density = resources.displayMetrics.density
    return widthPixel / density
}

fun Activity.setDrawerIconColor(num: Int){
    val idArr = arrayOf(R.id.item_drawer_menu_primary, R.id.item_drawer_menu_social, R.id.item_drawer_menu_promotions)
    val menu = (this as MainActivity).findViewById<NavigationView>(R.id.navigation_main_drawer).menu

    for(i in 0..2){
        menu.findItem(idArr[i]).isChecked = i == num
    }
}

fun Fragment.getMainActivity(): MainActivity = requireActivity() as MainActivity