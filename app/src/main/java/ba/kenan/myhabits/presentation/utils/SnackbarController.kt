package ba.kenan.myhabits.presentation.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val onClick: () -> Unit
)

class SnackbarController {
    private val _events = Channel<SnackbarEvent>(Channel.CONFLATED)
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}
