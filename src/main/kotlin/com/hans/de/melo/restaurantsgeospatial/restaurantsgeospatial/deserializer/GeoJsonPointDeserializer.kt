package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.io.IOException

class GeoJsonPointDeserializer : JsonDeserializer<GeoJsonPoint>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): GeoJsonPoint {
        val tree = jsonParser.codec.readTree<JsonNode>(jsonParser)

        return if (tree != null && tree["type"] == null)
            GeoJsonPoint(tree["longitude"].asDouble(), tree["latitude"].asDouble())
        else {
            getGeoJsonPoint(tree)
        }

    }

    private fun getGeoJsonPoint(tree: JsonNode): GeoJsonPoint {
        val type = tree["type"].asText()
        val coordsNode = tree["coordinates"]

        return if ("Point".equals(type, ignoreCase = true))
            GeoJsonPoint(coordsNode[0].asDouble(), coordsNode[1].asDouble())
        else {
            println("No logic present to deserialize ${tree.asText()}")
            GeoJsonPoint(0.0, 0.0)
        }
    }
}