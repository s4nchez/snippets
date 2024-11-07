import org.http4k.core.*
import org.http4k.routing.RoutedRequest
import org.http4k.routing.bind
import org.http4k.routing.routes

fun main() {
    val routes = routes("/" bind Method.GET to { req: Request ->
        Response(Status.OK)
            .body((req as? RoutedRequest)?.let { "routing info available in handler" } ?: "no routing info available in handler")
    })

    val diagnosisFilter = Filter { next ->
        {
            (it as? RoutedRequest)?.let { println("routing info available in filter") } ?: println("no routing info available in filter")
            next(it)
        }
    }

    val app = Filter.NoOp
        .then(diagnosisFilter)
        .then(routes as HttpHandler)

    val appWithRoutedHandler = Filter.NoOp
        .then(diagnosisFilter)
        .then(routes)

    println("Regular HttpHandler: \n\n")
    app(Request(Method.GET, "")).also(::println)

    println("RoutingHttpHandler: \n\n")
    appWithRoutedHandler(Request(Method.GET, "")).also(::println)
}