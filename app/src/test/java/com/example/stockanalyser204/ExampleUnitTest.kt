package com.example.stockanalyser204

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequestBlocking
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.select.Elements
import io.ktor.client.request.headers
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.typeOf

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun scraping() {
        var url =
            "https://finance.yahoo.com/calendar/earnings?from=2025-01-05&to=2025-01-11&day=2025-01-06&symbol=SOUN"
        val doc: Document = Ksoup.parseGetRequestBlocking(url = url) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0"
                )
            }
        }
        val data: Elements = doc.select("tr.yf-2twxe2")
        var doc2 = Ksoup.parseBodyFragment(data[0].select("div.yf-2twxe2")[0].toString())
        println(data[0].select("div.yf-2twxe2"))
    }
}