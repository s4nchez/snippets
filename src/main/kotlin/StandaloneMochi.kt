import com.squareup.moshi.Json
import com.squareup.moshi.Moshi.Builder
import org.http4k.format.ConfigurableMoshi
import org.http4k.format.asConfigurable
import org.http4k.format.text
import org.http4k.format.withStandardMappings


data class Something(@Json(name="foo_bar") val fooBar: String)

object MyMoshi : ConfigurableMoshi(
    Builder().asConfigurable()
        .withStandardMappings()
        .text(::Something, Something::fooBar)
        .done()
)

fun main() {
    val something = Something("hello")
    val json = MyMoshi.asFormatString(something)
    println(json)

    val fromJson = MyMoshi.asA(json, Something::class)
    println(fromJson)
}
