package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.bson.BsonArray
import org.bson.BsonDouble
import org.springframework.stereotype.Service

@Service
class RestaurantService(
        private val restaurantRepository: RestaurantRepository
) {
    suspend fun get(longitude: Number?, latitude: Number?, radius: Number?, distance: Number?): Flow<Restaurant> {
        return when {
            isAroundRequest(longitude, latitude, radius) -> {
                getNearbyRestaurantsByRadius(longitude!!.toDouble(), latitude!!.toDouble(), radius!!.toDouble())
            }
            isDistanceRequest(longitude, latitude, distance) -> {
                getNearbyRestaurantsByMaxDistance(longitude!!.toDouble(), latitude!!.toDouble(), distance!!.toDouble())
            }
            else -> {
                getAll()
            }
        }
    }

    private suspend fun getAll() = restaurantRepository.findAll().asFlow()

    private suspend fun getNearbyRestaurantsByRadius(longitude: Double, latitude: Double, radius: Double): Flow<Restaurant> {
        val coordinates = listOf(BsonDouble(longitude), BsonDouble(latitude))
        val centerSphereArray = listOf(BsonArray(coordinates), BsonDouble(radius))
        return restaurantRepository.findNearbyRestaurantsByRadius(BsonArray(centerSphereArray))
    }

    private suspend fun getNearbyRestaurantsByMaxDistance(longitude: Double, latitude: Double, maxDistance: Double) =
            restaurantRepository.findNearbyRestaurantsByMaxDistance(longitude, latitude, maxDistance)

    private fun isAroundRequest(longitude: Number?, latitude: Number?, radius: Number?) =
            longitude != null && latitude != null && radius != null

    private fun isDistanceRequest(longitude: Number?, latitude: Number?, distance: Number?) =
            longitude != null && latitude != null && distance != null
}