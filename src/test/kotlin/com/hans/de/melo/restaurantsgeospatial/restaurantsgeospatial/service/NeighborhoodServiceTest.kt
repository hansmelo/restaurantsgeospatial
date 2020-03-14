package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.NeighborhoodRepository
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@ExtendWith(MockKExtension::class)
@RunWith(JUnitPlatform::class)
class NeighborhoodServiceTest {

    @MockK
    lateinit var neighborhoodRepository: NeighborhoodRepository

    @MockK
    lateinit var restaurantRepository: RestaurantRepository

    @InjectMockKs
    lateinit var service: NeighborhoodService

    @MockK
    lateinit var neighborhood: Neighborhood

    @MockK
    lateinit var restaurant: Restaurant

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `test getAll`() {
        // given
        coEvery { neighborhoodRepository.findAll() } returns Flux.just(neighborhood)

        // when
        val result = runBlocking { service.getAll() }

        // then
        coVerify { neighborhoodRepository.findAll() }
        StepVerifier.create(result.asFlux())
                .expectNext(neighborhood)
                .verifyComplete()
    }

    @Test
    fun `test getAll is empty`() {
        // given
        coEvery { neighborhoodRepository.findAll() } returns Flux.empty()

        // when
        val result = runBlocking { service.getAll() }

        // then
        coVerify { neighborhoodRepository.findAll() }
        StepVerifier.create(result.asFlux())
                .verifyComplete()
    }

    @Test
    fun `test getByPoint`() {
        // given
        coEvery { neighborhoodRepository.findByGeometryIntersect(any(), any()) } returns neighborhood

        // when
        val result = runBlocking { service.getByPoint(-1.0, 1.0) }

        // then
        coVerify { neighborhoodRepository.findByGeometryIntersect(-1.0, 1.0) }
        assertEquals(result, neighborhood)
    }

    @Test
    fun `test getRestaurants`() {
        // given
        val restaurantFlux = Flux.just(restaurant)
        coEvery { neighborhoodRepository.findByGeometryIntersect(any(), any()) } returns neighborhood
        coEvery { restaurantRepository.findByGeometry(neighborhood.geometry) } returns restaurantFlux.asFlow()

        // when
        val result = runBlocking { service.getRestaurants(-1.0, 1.0) }

        // then
        coVerifySequence {
            neighborhoodRepository.findByGeometryIntersect(-1.0, 1.0)
            restaurantRepository.findByGeometry(neighborhood.geometry)
        }

        StepVerifier.create(result.asFlux())
                .expectNext(restaurant)
                .verifyComplete()
    }


    @Test
    fun `test getRestaurants is Empty`() {
        // given
        val restaurantFlux = Flux.empty<Restaurant>()
        coEvery { neighborhoodRepository.findByGeometryIntersect(any(), any()) } returns neighborhood
        coEvery { restaurantRepository.findByGeometry(neighborhood.geometry) } returns restaurantFlux.asFlow()

        // when
        val result = runBlocking { service.getRestaurants(-1.0, 1.0) }

        // then
        coVerifySequence {
            neighborhoodRepository.findByGeometryIntersect(-1.0, 1.0)
            restaurantRepository.findByGeometry(neighborhood.geometry)
        }

        StepVerifier.create(result.asFlux())
                .verifyComplete()
    }

}
