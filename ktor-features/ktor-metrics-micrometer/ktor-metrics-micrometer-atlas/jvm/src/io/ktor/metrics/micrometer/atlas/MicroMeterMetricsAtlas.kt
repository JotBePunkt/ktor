/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.atlas

import io.ktor.metrics.micrometer.*
import io.micrometer.atlas.*
import io.micrometer.core.instrument.*
import com.netflix.spectator.atlas.AtlasConfig as IAtlasConfig

class MicrometerMetricsAtlas(config: Configuration) :
    AbstractMicrometerMetrics<AtlasMeterRegistry, MicrometerMetricsAtlas.Configuration>(config) {

    class Configuration : AbstractConfiguration<AtlasMeterRegistry>() {

        fun registry(block: AtlasRegistryConfig.() -> Unit) {
            val config = AtlasRegistryConfig().apply(block)

            registry = AtlasMeterRegistry(config.config, config.clock)
        }

    }

    class AtlasRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = AtlasConfig()
            private set

        fun config(block: AtlasConfig.() -> Unit) {
            config = AtlasConfig().apply(block)
        }

        class AtlasConfig : IAtlasConfig {
            /**
             * Should an exception be thrown for warnings
             */
            var propagateWarnings: Boolean = super.propagateWarnings()

            override fun propagateWarnings() = propagateWarnings


            // this method is not needed here
            override fun get(k: String?) = null
        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<AtlasMeterRegistry, Configuration>(
            ::MicrometerMetricsAtlas,
            ::Configuration
        )
}
