package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.integration

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.controller.NeighborhoodController
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.generateDataToTests
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.habaneroCafeMexicanGrillJson
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.losAngelesBakeryJson
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.neighborhoodJson
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.NeighborhoodRepository
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NeighborhoodControllerTest {

    @Autowired
    private lateinit var restController: NeighborhoodController

    @Autowired
    private lateinit var neighborhoodRepository: NeighborhoodRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var client: WebTestClient

    @Before
    fun setup() {
        client = WebTestClient.bindToController(restController).build()
        client = client.mutate().responseTimeout(Duration.ofMillis(30000)).build()

        generateDataToTests(restaurantRepository = restaurantRepository, neighborhoodRepository = neighborhoodRepository)
    }

    @Test
    fun `get all neighborhoods streaming`() {
        client.get().uri("/reactivemap/v1/neighborhoods")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(Neighborhood::class.java)
                .hasSize(1)
    }

    @Test
    fun `get all neighborhoods`() {
        client.get().uri("/reactivemap/v1/neighborhoods")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json("[$neighborhoodJson]")
    }

    @Test
    fun `get neighborhood by one specific point streaming`() {
        client.get().uri("/reactivemap/v1/neighborhoods/-73.95/40.69")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(Neighborhood::class.java)
                .hasSize(1)
    }

    @Test
    fun `get neighborhood by one specific point`() {
        client.get().uri("/reactivemap/v1/neighborhoods/-73.95/40.69")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json(neighborhoodJson)
    }

    @Test
    fun `get all restaurants by neighborhood streaming`() {
        client.get().uri("/reactivemap/v1/neighborhoods/-73.95/40.69/restaurants")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(Restaurant::class.java)
                .hasSize(2)
    }

    @Test
    fun `get all restaurants by neighborhood`() {
        client.get().uri("/reactivemap/v1/neighborhoods/-73.95/40.69/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json("[$habaneroCafeMexicanGrillJson, $losAngelesBakeryJson]")
    }


    @Test
    fun `the request's params don't match with any neighborhood, the response should be NotFound`() {
        client.get().uri("/reactivemap/v1/neighborhoods/-93.95/70.69")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound
    }
}