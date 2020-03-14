package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.generateNeighborhoodToTests
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.generateRestaurantsToTests
import kotlinx.coroutines.reactor.asFlux
import org.bson.BsonArray
import org.bson.BsonDouble
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.geo.GeoJsonModule
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@DataMongoTest
class RestaurantRepositoryTest {

    @Autowired
    lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    lateinit var neighborhoodRepository: NeighborhoodRepository

    private lateinit var mapper: ObjectMapper

    private lateinit var restaurants: List<Restaurant>

    private lateinit var neighborhood: Neighborhood

    @BeforeEach
    fun setUp() {
        mapper = ObjectMapper()
        mapper.registerModule(GeoJsonModule())

        restaurants = generateRestaurantsToTests(mapper, restaurantRepository)

        neighborhood = generateNeighborhoodToTests(mapper, neighborhoodRepository)
    }

    @Test
    fun `test findByGeometryIntersect`() {
        StepVerifier.create(restaurantRepository.findByGeometry(neighborhood.geometry).asFlux())
                .expectNext(restaurants[0])
                .expectNext(restaurants[1])
                .verifyComplete()
    }

    @Test
    fun `test findNearbyRestaurantsByRadius`() {
        val coordinates = listOf(BsonDouble(-73.9440286), BsonDouble(40.7006576))
        val centerSphereArray = listOf(BsonArray(coordinates), BsonDouble(0.00000000000000000001))
        StepVerifier.create(restaurantRepository.findNearbyRestaurantsByRadius(BsonArray(centerSphereArray)).asFlux())
                .expectNext(restaurants[0])
                .verifyComplete()
    }

    @Test
    fun `test findNearbyRestaurantsByMaxDistance`() {
        StepVerifier.create(restaurantRepository.findNearbyRestaurantsByMaxDistance(-73.94195, 40.823392, 21.0).asFlux())
                .expectNext(restaurants[2])
                .verifyComplete()
    }
}