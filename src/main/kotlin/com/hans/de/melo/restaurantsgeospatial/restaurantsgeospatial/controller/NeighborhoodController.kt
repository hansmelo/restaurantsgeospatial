package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.controller

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.exception.NotFoundException
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.service.NeighborhoodService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
        value = ["reactivemap/v1/neighborhoods"],
        produces = [MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE]
)
class NeighborhoodController(
        private val neighborhoodService: NeighborhoodService
) {
    @GetMapping
    suspend fun getAll() =
            neighborhoodService.getAll()

    @GetMapping("/{longitude}/{latitude}")
    suspend fun getByPoint(@PathVariable longitude: Double, @PathVariable latitude: Double) =
            neighborhoodService.getByPoint(longitude, latitude) ?: throw NotFoundException()

    @GetMapping("/{longitude}/{latitude}/restaurants")
    suspend fun getRestaurants(@PathVariable longitude: Double, @PathVariable latitude: Double) =
            neighborhoodService.getRestaurants(longitude, latitude)

}