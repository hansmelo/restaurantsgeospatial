package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.deserializer.GeoJsonPointDeserializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Restaurant(
        @Id val id: ObjectId,
        val name: String,
        @JsonDeserialize(using = GeoJsonPointDeserializer::class)
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val location: GeoJsonPoint
)