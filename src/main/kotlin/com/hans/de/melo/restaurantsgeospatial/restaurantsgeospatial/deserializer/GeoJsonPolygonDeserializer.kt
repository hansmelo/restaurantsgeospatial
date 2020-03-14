package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import java.io.IOException

class GeoJsonPolygonDeserializer : JsonDeserializer<GeoJsonPolygon>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): GeoJsonPolygon {
        if (jsonParser == null) return GeoJsonPolygon(listOf(Point(0.0, 0.0)))

        val tree = jsonParser.codec.readTree<JsonNode>(jsonParser)

        return if (tree != null)
            getGeoJsonPolygon(tree)
        else
            GeoJsonPolygon(listOf(Point(0.0, 0.0)))
    }

    private fun getGeoJsonPolygon(tree: JsonNode): GeoJsonPolygon {
        val points = tree["coordinates"][0]["coordinates"]
                .map { Point(it["x"].doubleValue(), it["y"].doubleValue()) }

        return GeoJsonPolygon(points)
    }
}