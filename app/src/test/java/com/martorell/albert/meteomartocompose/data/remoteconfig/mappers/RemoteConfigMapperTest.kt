package com.martorell.albert.meteomartocompose.data.remoteconfig.mappers

import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteConfigMapperTest {

    private val mapper = RemoteConfigMapper(
        defaultThreshold = 30.0,
        minLimit = -100.0,
        maxLimit = 100.0,
        defaultInterval = 60L,
        minInterval = 15L
    )

    @Test
    fun `mapToThreshold should return same value when within bounds`() {
        val input = 25.0
        val expected = 25.0
        val result = mapper.mapToThreshold(input)
        assertEquals(expected, result, 0.0)
    }

    @Test
    fun `mapToThreshold should return default when value is too high`() {
        val input = 150.0
        val expected = 30.0
        val result = mapper.mapToThreshold(input)
        assertEquals(expected, result, 0.0)
    }

    @Test
    fun `mapToThreshold should return default when value is too low`() {
        val input = -150.0
        val expected = 30.0
        val result = mapper.mapToThreshold(input)
        assertEquals(expected, result, 0.0)
    }

    @Test
    fun `mapToInterval should return same value when above minimum`() {
        val input = 30L
        val expected = 30L
        val result = mapper.mapToInterval(input)
        assertEquals(expected, result)
    }

    @Test
    fun `mapToInterval should return default when value is too low`() {
        val input = 5L
        val expected = 60L
        val result = mapper.mapToInterval(input)
        assertEquals(expected, result)
    }

    @Test
    fun `mapToInterval should return default when value is null`() {
        val expected = 60L
        val result = mapper.mapToInterval(null)
        assertEquals(expected, result)
    }
}
