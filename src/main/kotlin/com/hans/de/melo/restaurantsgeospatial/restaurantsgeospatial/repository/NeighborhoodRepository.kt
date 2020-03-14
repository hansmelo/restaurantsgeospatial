package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NeighborhoodRepository : ReactiveMongoRepository<Neighborhood, ObjectId> {

    @Query("{ geometry: { \$geoIntersects: { \$geometry: { type: \"Point\", coordinates: [ ?0, ?1 ] } } } }")
    suspend fun findByGeometryIntersect(longitude: Double, latitude: Double): Neighborhood
}