package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.NeighborhoodRepository
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service

@Service
class NeighborhoodService(
        private val neighborhoodRepository: NeighborhoodRepository,
        private val restaurantRepository: RestaurantRepository
) {
    suspend fun getAll() = neighborhoodRepository.findAll().asFlow()

    suspend fun getByPoint(longitude: Double, latitude: Double) =
            neighborhoodRepository.findByGeometryIntersect(longitude, latitude)

    suspend fun getRestaurants(longitude: Double, latitude: Double): Flow<Restaurant> {
        val neighborhood = neighborhoodRepository.findByGeometryIntersect(longitude, latitude)
        return restaurantRepository.findByGeometry(neighborhood.geometry)
    }
}