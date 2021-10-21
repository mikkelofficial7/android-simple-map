package com.example.myapplication.Utility

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.Window
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R

object GpsUtil {
    private val TAG_REQUEST_GPS = 101

    fun isGpsActive(context: Context): Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun openGps(activity: FragmentActivity) {
        activity.startActivityForResult(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
            TAG_REQUEST_GPS
        )
    }

    fun showDialogGps(activity: FragmentActivity) {
        val builder = AlertDialog.Builder(
            activity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        ).apply {
            setTitle(context.getString(R.string.gps_title))
            setMessage(context.getString(R.string.gps_desc))
            setPositiveButton(context.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface, which ->
                    openGps(activity)
                })
            setNegativeButton(context.getString(R.string.no),
                DialogInterface.OnClickListener { dialogInterface, which ->
                    dialogInterface.dismiss()
                })
        }

        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }
}