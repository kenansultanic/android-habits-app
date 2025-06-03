package ba.kenan.myhabits.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ba.kenan.myhabits.domain.network.NetworkStatusProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkStatusProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStatusProvider {
    override fun isOffline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return true
        val capabilities = cm.getNetworkCapabilities(network) ?: return true
        return !(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}
