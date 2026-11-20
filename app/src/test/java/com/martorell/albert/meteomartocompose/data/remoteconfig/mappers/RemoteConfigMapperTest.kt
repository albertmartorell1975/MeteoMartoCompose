package com.martorell.albert.meteomartocompose.data.remoteconfig.mappers

import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteConfigMapperTest {

    private val mapper = RemoteConfigMapper(
        defaultThreshold = 30.0,
        minLimit = -100.0,
        maxLimit = 100.0,
        defaultInterval = 60L,
        minInterval = 15L,
    )

    @Test
    fun `mapToThreshold returns in-bounds value and defaults on out-of-bounds or null`() {
        assertEquals(25.0, mapper.mapToThreshold(25.0), 0.0)
        assertEquals(30.0, mapper.mapToThreshold(150.0), 0.0)
        assertEquals(30.0, mapper.mapToThreshold(-150.0), 0.0)
        assertEquals(30.0, mapper.mapToThreshold(100.0), 0.0)
        assertEquals(30.0, mapper.mapToThreshold(-100.0), 0.0)
        assertEquals(30.0, mapper.mapToThreshold(null), 0.0)
    }

    @Test
    fun `mapToInterval returns value at or above minimum and defaults on too-low or null`() {
        assertEquals(15L, mapper.mapToInterval(15L))
        assertEquals(30L, mapper.mapToInterval(30L))
        assertEquals(60L, mapper.mapToInterval(5L))
        assertEquals(60L, mapper.mapToInterval(null))
    }
}
