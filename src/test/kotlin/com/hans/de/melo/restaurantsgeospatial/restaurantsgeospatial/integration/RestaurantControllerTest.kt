package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.integration

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.controller.RestaurantController
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.generateDataToTests
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.habaneroCafeMexicanGrillJson
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.peoplesChoiceKitchenJson
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
class RestaurantControllerTest {

    @Autowired
    private lateinit var restController: RestaurantController

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
    fun `get all restaurants streaming`() {
        client.get().uri("/reactivemap/v1/restaurants")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(Restaurant::class.java)
                .hasSize(3)
    }

    @Test
    fun `get all restaurants`() {
        client.get().uri("/reactivemap/v1/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Restaurant::class.java)
                .hasSize(3)
    }

    @Test
    fun `get all nearby restaurants by radius streaming`() {
        client.get().uri("/reactivemap/v1/restaurants?longitude=-73.9440286&latitude=40.7006576&radius=0.00000000000000000001")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBody()
                .json(habaneroCafeMexicanGrillJson)
    }

    @Test
    fun `get all nearby restaurants by radius`() {
        client.get().uri("/reactivemap/v1/restaurants?longitude=-73.9440286&latitude=40.7006576&radius=0.00000000000000000001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json("[$habaneroCafeMexicanGrillJson]")
    }

    @Test
    fun `get all nearby restaurants by distance streaming`() {
        client.get().uri("/reactivemap/v1/restaurants?longitude=-73.94195&latitude=40.823392&distance=21.0")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBody()
                .json(peoplesChoiceKitchenJson)
    }

    @Test
    fun `get all nearby restaurants by distance`() {
        client.get().uri("/reactivemap/v1/restaurants?longitude=-73.94195&latitude=40.823392&distance=21.0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json("[$peoplesChoiceKitchenJson]")
    }

    @Test
    fun `request with distance and radius both not null at the same, the response should be BadRequest`() {
        client.get().uri("/reactivemap/v1/restaurants?longitude=-73.94195&latitude=40.823392&distance=21.06&radius=0.00000000000000000001")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
    }
}