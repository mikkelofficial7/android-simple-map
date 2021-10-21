package com.example.myapplication.Utility

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R

object PermissionUtil {
    var LOCATION_REQ_CODE = 1234

    fun checkLocationPermission(activity: Activity, onPermissionGranted: () -> Unit = {}) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                ActivityCompat.requestPermissions(
                    activity, permissions, LOCATION_REQ_CODE
                )
            return

        }else{
            onPermissionGranted()
        }
    }

    fun isAllPermissionGranted(grantList: IntArray) : Boolean {
        val listGrantedPermission = ArrayList<Boolean>()

        grantList.forEach {
            if(it != PackageManager.PERMISSION_DENIED)  listGrantedPermission.add(true)
        }

        return listGrantedPermission.isNotEmpty()
    }

    fun showDialogPermissionDenied(activity: FragmentActivity) {
        val builder = AlertDialog.Builder(
            activity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).apply {
            setTitle(context.getString(R.string.error))
            setMessage(context.getString(R.string.permission_denied))
        }

        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
    }
}