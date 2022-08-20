package com.satyasoft.myschoolavhiyan.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Context.CONNECTIVITY_SERVICE as CONNECTIVITY_SERVICE1

class NetworkConnectivityStatus {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {

        val connectivityMgr = context.getSystemService(CONNECTIVITY_SERVICE1) as ConnectivityManager

        val allNetworks: Array<Network> = connectivityMgr.allNetworks // added in API 21 (Lollipop)

        for (network in allNetworks) {
            val networkCapabilities = connectivityMgr!!.getNetworkCapabilities(network)
            return (networkCapabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)))
        }
        return false
    }

}