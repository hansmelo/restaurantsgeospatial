package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.bson.BsonArray
import org.bson.BsonDouble
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@ExtendWith(MockKExtension::class)
@RunWith(JUnitPlatform::class)
class RestaurantServiceTest {

    @MockK
    lateinit var repository: RestaurantRepository

    @InjectMockKs
    lateinit var service: RestaurantService

    @MockK
    lateinit var restaurant: Restaurant

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `test get with null params should return All`() {
        // given
        coEvery { repository.findAll() } returns Flux.just(restaurant)

        // when
        val result = runBlocking { service.get(null, null, null, null) }

        // then
        coVerify { repository.findAll() }
        StepVerifier.create(result.asFlux())
                .expectNext(restaurant)
                .verifyComplete()
    }

    @Test
    fun `test get with params longitude, latitude, radius should return the nearby restaurants by radius`() {
        // given
        val restaurantFlux = Flux.just(restaurant)
        coEvery { repository.findNearbyRestaurantsByRadius(any()) } returns restaurantFlux.asFlow()

        // when
        val result = runBlocking { service.get(1.0, 1.0, 1.0, null) }

        // then
        coVerify {
            repository.findNearbyRestaurantsByRadius(
                    BsonArray(listOf(BsonArray(listOf(BsonDouble(1.0), BsonDouble(1.0))), BsonDouble(1.0)))
            )
        }

        StepVerifier.create(result.asFlux())
                .expectNext(restaurant)
                .verifyComplete()
    }

    @Test
    fun `test get with params longitude, latitude, distance should return the find nearby restaurants by maxDistance`() {
        // given
        val restaurantFlux = Flux.just(restaurant)
        coEvery { repository.findNearbyRestaurantsByMaxDistance(any(), any(), any()) } returns restaurantFlux.asFlow()

        // when
        val result = runBlocking { service.get(1.0, 1.0, null, 1.0) }

        // then
        coVerify { repository.findNearbyRestaurantsByMaxDistance(1.0, 1.0, 1.0) }

        StepVerifier.create(result.asFlux())
                .expectNext(restaurant)
                .verifyComplete()
    }
}