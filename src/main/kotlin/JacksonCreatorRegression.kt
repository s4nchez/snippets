import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings
import org.http4k.lens.BiDiMapping
import org.http4k.routing.bind
import org.http4k.routing.routes

data class WrappedId(@JsonValue val value: Int) {
    companion object {
        val biDiMapping = BiDiMapping(::WrappedId) { it.value }

        @JvmStatic
        @JsonCreator
        fun create(value: String): WrappedId = biDiMapping.asOut(value.substringAfter("device-").toInt())
        @JvmStatic
        @JsonCreator
        fun create(value: Int): WrappedId = WrappedId(value)
    }
}

object CustomJackson : ConfigurableJackson(
    KotlinModule.Builder().build()
        .asConfigurable()
        .withStandardMappings()
        .done()
        .setPropertyNamingStrategy(SNAKE_CASE)
        .enable(com.fasterxml.jackson.core.JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
)


fun main() {
    val wrappedIdsLens = Body.auto<List<WrappedId>>().toLens()
    val routes = routes (
        "/test" bind POST to { r ->
            val eventIdsLens1 = wrappedIdsLens(r)
            Response(OK).body("OK got ${eventIdsLens1}")
        })

    routes(Request(POST, Uri.of("/test")).body("""["device-1", "device-2", "device-3"]""")).also(::println)
}
