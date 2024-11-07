import dev.forkhandles.result4k.get
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import org.http4k.core.Filter
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.LensFailure
import org.http4k.lens.Query
import org.http4k.lens.asResult
import org.http4k.lens.int

fun main() {
    val lens = Query.int().required("foo")
    val handler = { req: Request -> Response(Status.OK).body("value = ${lens(req)}") }
    val badRequest = Request(Method.GET, "http://localhost:9000").query("foo", "abc")

    try {
        handler(badRequest)
    } catch (e: Exception) {
        println("exception thrown")
    }

    val handler2 = ServerFilters.CatchLensFailure().then(handler)

    handler2(badRequest).also(::println) // HTTP/1.1 400 Bad Request

    val betterLens = lens.asResult()

    val handler3 = { req: Request ->
        betterLens(req)
            .map { Response(Status.OK).body("value = ${it}") }
            .mapFailure { Response(Status.BAD_REQUEST.description("invalid foo")) }
            .get()
    }

    handler3(badRequest).also(::println) // HTTP/1.1 400 Bad Request

    val customFilter = Filter { next ->
        {
            try {
                next(it)
            } catch (e: LensFailure) {
                Response(Status.BAD_REQUEST.description("invalid foo"))
            }
        }
    }

    val handler4 = customFilter.then(handler)

    handler4(badRequest).also(::println) // HTTP/1.1 400 Bad Request
}
