package ba.kenan.myhabits.domain.network

interface NetworkStatusProvider {
    fun isOffline(): Boolean
}
