import org.http4k.client.Java8HttpClient
import org.http4k.core.Method
import org.http4k.core.Request


fun main() {
   Java8HttpClient()(Request(Method.GET, "https://httpbin.org/uuid")).also(::println)
}
