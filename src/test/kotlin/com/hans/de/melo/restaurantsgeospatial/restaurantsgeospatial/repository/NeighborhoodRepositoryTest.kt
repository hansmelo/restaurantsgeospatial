package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.domain.Neighborhood
import com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.generateNeighborhoodToTests
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.geo.GeoJsonModule
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataMongoTest
class NeighborhoodRepositoryTest {

    @Autowired
    lateinit var repository: NeighborhoodRepository

    private lateinit var mapper: ObjectMapper

    private lateinit var neighborhood: Neighborhood

    @BeforeEach
    fun setUp() {
        mapper = ObjectMapper()
        mapper.registerModule(GeoJsonModule())

        neighborhood = generateNeighborhoodToTests(mapper, repository)
    }

    @Test
    fun `test findByGeometryIntersect`() {
        val result = runBlocking { repository.findByGeometryIntersect(-73.95, 40.69) }

        assertEquals(result, neighborhood)
    }
}