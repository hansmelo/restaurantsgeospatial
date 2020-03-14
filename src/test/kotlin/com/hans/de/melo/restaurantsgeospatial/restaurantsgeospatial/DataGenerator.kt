package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial

import com.fasterxml.jackson.databind.ObjectMapper
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.NeighborhoodRepository
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository.RestaurantRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.geo.GeoJsonModule
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon

fun generateDataToTests(neighborhoodRepository: NeighborhoodRepository, restaurantRepository: RestaurantRepository) {
    val mapper = ObjectMapper()
    mapper.registerModule(GeoJsonModule())

    generateNeighborhoodToTests(mapper, neighborhoodRepository)

    generateRestaurantsToTests(mapper, restaurantRepository)
}

fun generateNeighborhoodToTests(mapper: ObjectMapper, neighborhoodRepository: NeighborhoodRepository): Neighborhood {
    val geoJsonPolygon = mapper.readValue(polygonJson, GeoJsonPolygon::class.java)
    val neighborhood = Neighborhood(ObjectId("58a58bdb85979b5415f39f23"), "Bedford", geoJsonPolygon)

    neighborhoodRepository.save(neighborhood).block()
    return neighborhood
}

fun generateRestaurantsToTests(mapper: ObjectMapper, restaurantRepository: RestaurantRepository): List<Restaurant> {
    val locationRestaurant1 = mapper.readValue(location1, GeoJsonPoint::class.java)
    val restaurant1 = Restaurant(ObjectId("58a58bdb85979b5415f39f32"), "Habanero Cafe Mexican Grill", locationRestaurant1)
    val locationRestaurant2 = mapper.readValue(location2, GeoJsonPoint::class.java)
    val restaurant2 = Restaurant(ObjectId("58a58bdb85979b5415f39f64"), "Los Angeles Bakery", locationRestaurant2)
    val locationRestaurant3 = mapper.readValue(location3, GeoJsonPoint::class.java)
    val restaurant3 = Restaurant(ObjectId("55cba2476c522cafdb05500b"), "Peoples Choice Kitchen", locationRestaurant3)

    val restaurants = listOf(restaurant1, restaurant2, restaurant3)
    restaurantRepository.saveAll(restaurants).blockLast()
    return restaurants
}