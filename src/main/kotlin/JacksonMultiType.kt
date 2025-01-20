import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import dev.forkhandles.result4k.peek
import dev.forkhandles.result4k.peekFailure
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.lens.asResult

val deviceJson = """{
    "id": "device-10930",
    "title": "FT",
    "start": "2025-01-20T12:30:00Z",
    "end": "2025-01-20T13:30:00Z",
    "calendarEventType": "device",
    "parameters": {
        "buildingId": 2,
        "eventType": "OPL"
    }
}"""

val unavailabilityJson = """
{
    "id": "unavailability-1051",
    "title": "Crown Place",
    "start": "2025-01-20T13:00:00Z",
    "end": "2025-01-20T14:30:00Z",
    "calendarEventType": "unavailability",
    "parameters": {
        "buildingId": 1
    }
}"""

@JsonTypeInfo(use = Id.NAME, property = "calendarEventType")
@JsonSubTypes(
    JsonSubTypes.Type(value = Event.UnavailabilityEvent::class, name = "unavailability"),
    JsonSubTypes.Type(value = Event.DeviceEvent::class, name = "device")
)
sealed class Event {
    abstract val id: String
    abstract val title: String
    data class UnavailabilityEvent(override val id: String, override val title: String) : Event()
    data class DeviceEvent(override val id: String, override val title: String) : Event()
}

fun main() {

    val eventLens = Body.auto<Event>().toLens().asResult()
    eventLens(Request(Method.POST, "").body(deviceJson)).peek(::println).peekFailure(::println)
    eventLens(Request(Method.POST, "").body(unavailabilityJson)).peek(::println).peekFailure(::println)
}