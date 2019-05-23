/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.appoptics

import io.ktor.application.*
import io.ktor.server.testing.*
import io.micrometer.appoptics.*
import io.micrometer.core.instrument.*
import org.junit.*
import org.junit.Assert.*

class MicrometerMetricsAppOpticsTest {


    @Test
    fun `plugin cannot be installed without registry`() {
        withTestApplication {
            try {
                application.install(MicrometerMetricsAppOptics)
                fail("no exception thrown")
            } catch (e: IllegalArgumentException) {
                assertEquals("Meter registry is missing. Please initialize the field 'registry'", e.localizedMessage)
            }
        }
    }

    @Test
    fun `plugin can be installed with an manually configured registry`() {
        withTestApplication {
            application.install(MicrometerMetricsAppOptics) {
                registry = AppOpticsMeterRegistry(object : AppOpticsConfig {
                    override fun get(key: String) = null
                    override fun apiToken() = "test"
                    override fun batchSize() = 5
                    override fun hostTag() = "test"
                }, Clock.SYSTEM)
            }
        }
    }

    @Test
    fun `plugin can be installed with configured configuration`() {
        withTestApplication {
            application.install(MicrometerMetricsAppOptics) {
                registry {
                    config {
                        apiToken = "test"
                        batchSize = 5
                        hostTag = "test"
                    }
                    clock = Clock.SYSTEM
                }
            }
        }
    }
}
