package ru.sberschool.hystrix

import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SlowlyApiTestWithTwoClient {

    private val clientMock = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "http://127.0.0.1:18080", FallbackSlowlyApi())

    private val clientPoke = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "https://pokeapi.co/api/v2/", FallbackSlowlyApi())


    private lateinit var mockServer: ClientAndServer

    @BeforeEach
    fun setup() {
        mockServer = ClientAndServer.startClientAndServer(18080)
    }

    @AfterEach
    fun shutdown() {
        mockServer.stop()
    }

    @Test
    fun `should return fallback`() {
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(400)
                    .withDelay(TimeUnit.SECONDS, 30)
                    .withBody("{\n" +
                            "  \"name\": \"slowpoke\",\n" +
                            "  \"count\": 7\n" +
                            "}")
            )
        assertEquals("fallback", clientMock.getPokemon().name)
    }

    @Test
    fun `should return right name`() {
        assertEquals("slowpoke", clientPoke.getPokemon().name)
    }

    @Test
    fun `should nod return right name`() {
        assertNotEquals("bulbasaur", clientPoke.getPokemon().name)
    }


}
