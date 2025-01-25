import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes


fun main() {
    val okHandler = { request: Request -> Response(Status.OK).body(
        "path=${request.uri.path}, sub-path=${request.path("path") ?: "empty :)"}")
    }
    val server = routes(
        "/api" bind routes(
            "{path:.*}" bind okHandler
        )
    )

    server(Request(Method.GET, "/api")).debug()
    server(Request(Method.GET, "/api/")).debug()
    server(Request(Method.GET, "/api/anything")).debug()
}

private fun Response.debug() {println("status=${status}, $body")}