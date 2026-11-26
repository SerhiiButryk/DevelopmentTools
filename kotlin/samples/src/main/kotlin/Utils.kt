import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*

fun <T : Any> log(message: T) {
    val date = SimpleDateFormat("hh:mm:ss").format(Date())
    println(
        "\"$date " +
                "[${Thread.currentThread().id}-${Thread.currentThread().name}]\" " +
                "$message"
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> CoroutineScope.logDebug(message: T) {
    log(
        "DEBUG: coname: '${coroutineContext[CoroutineName]?.name}' '$this', parent job: '${coroutineContext[Job]?.parent}'" +
                ": " + message
    )
}