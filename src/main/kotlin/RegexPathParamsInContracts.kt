import org.http4k.contract.contract
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.regex
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class RegexPathParamsInContracts {

    @Test
    fun `routed`() {
        val app = routes("/foo/bar/{foo:.*}/zap" bind POST to { _ -> Response(OK) })

        assertEquals(
            Response(OK),
            app(Request(POST, "/foo/bar/baz/zap"))
        )
    }


    @Test
    fun `should route wildcards around contracts`() {
        val app = routes("/foo/bar/{foo:(.*)}/zap" bind contract {
            routes += "" meta {} bindContract POST to { _ -> Response(OK) }
        }).also { println(it.description) }

        assertEquals(
            Response(OK),
            app(Request(POST, "/foo/bar/baz/zap"))
        )
    }

    @Test
    fun `should route wildcards in contracts`() {
        val app = contract {
            routes += "/foo/bar/{foo:(.*)}/zap" meta {} bindContract POST to { _ -> Response(OK) }
        }

        assertEquals(
            app(Request(POST, "/foo/bar/baz/zap")),
            Response(OK)
        )
    }

    @Test
    fun `should route wildcards in contracts with path params`() {
        val pathParam = Path.regex("(.*)").of("foo")
        val app = contract {
            routes += "/foo/bar" / pathParam / "zap" meta {} bindContract POST to { _, _ -> { Response(OK) } }
        }.also { println(it.description) }

        assertEquals(
            Response(OK),
            app(Request(POST, "/foo/bar/baz/zap"))
        )
    }

    @Test
    fun `normal routing without regex for path lenses work`() {
        val pathParam = Path.string().of("foo")
        val app = contract {
            routes += "/foo/bar" / pathParam / "zap" meta {} bindContract POST to { _, _ -> { Response(OK) } }
        }

        assertEquals(
            app(Request(POST, "/foo/bar/baz/zap")),
            Response(OK)
        )
    }

}