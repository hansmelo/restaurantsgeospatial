package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository

import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Restaurant
import kotlinx.coroutines.flow.Flow
import org.bson.BsonArray
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : ReactiveMongoRepository<Restaurant, ObjectId> {

    @Query("{ location: { \$geoWithin: { \$geometry: ?0 } } }")
    fun findByGeometry(geometry: GeoJson<*>): Flow<Restaurant>

    @Query("{ location: { \$geoWithin: { \$centerSphere: ?0 } } }")
    fun findNearbyRestaurantsByRadius(centerSphereArray: BsonArray): Flow<Restaurant>

    @Query("{ location: { \$nearSphere: { \$geometry: { type: \"Point\", coordinates: [ ?0, ?1 ] }, \$maxDistance: ?2 }}}")
    fun findNearbyRestaurantsByMaxDistance(longitude: Double, latitude: Double, maxDistance: Double): Flow<Restaurant>
}