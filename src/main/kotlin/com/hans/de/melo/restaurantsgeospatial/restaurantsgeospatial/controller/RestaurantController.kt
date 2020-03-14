package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.controller

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.exception.BadRequestException
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service.RestaurantService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
        value = ["reactivemap/v1/restaurants"],
        produces = [MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE]
)
class RestaurantController(
        private val restaurantService: RestaurantService
) {
    @GetMapping
    suspend fun get(
            @RequestParam(required = false) longitude: Number,
            @RequestParam(required = false) latitude: Number,
            @RequestParam(required = false) radius: Number,
            @RequestParam(required = false) distance: Number
    ): Flow<Restaurant> {
        if (isBadRequest(radius, distance, longitude, latitude)) throw BadRequestException()

        return restaurantService.get(longitude = longitude, latitude = latitude, radius = radius, distance = distance)
    }

    private fun isBadRequest(radius: Number, distance: Number, longitude: Number, latitude: Number) =
            (radius != null && distance != null) || ((longitude != null && latitude == null) || (latitude != null && longitude == null))
}