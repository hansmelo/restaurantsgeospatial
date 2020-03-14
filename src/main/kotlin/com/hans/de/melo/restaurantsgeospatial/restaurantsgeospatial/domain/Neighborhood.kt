package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.deserializer.GeoJsonPolygonDeserializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Neighborhood(
        @Id val id: ObjectId,
        val name: String,
        @JsonDeserialize(using = GeoJsonPolygonDeserializer::class)
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val geometry: GeoJson<*>
)