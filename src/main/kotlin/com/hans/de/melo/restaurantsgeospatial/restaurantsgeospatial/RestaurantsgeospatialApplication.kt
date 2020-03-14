package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class RestaurantsgeospatialApplication

fun main(args: Array<String>) {
    runApplication<RestaurantsgeospatialApplication>(*args)
}
