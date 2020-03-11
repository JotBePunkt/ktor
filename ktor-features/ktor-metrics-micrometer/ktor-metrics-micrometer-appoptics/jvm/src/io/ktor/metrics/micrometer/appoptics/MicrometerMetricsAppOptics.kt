/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.appoptics

import io.ktor.metrics.micrometer.*
import io.micrometer.appoptics.*
import io.micrometer.core.instrument.*
import io.micrometer.appoptics.AppOpticsConfig as IAppOpticsConfig

class MicrometerMetricsAppOptics(config: Configuration) :
    AbstractMicrometerMetrics<AppOpticsMeterRegistry, MicrometerMetricsAppOptics.Configuration>(config) {

    class Configuration : AbstractConfiguration<AppOpticsMeterRegistry>() {

        fun registry(block: AppOpticsRegistryConfig.() -> Unit) {
            val config = AppOpticsRegistryConfig().apply(block)

            registry = AppOpticsMeterRegistry(config.config, config.clock)
        }

    }

    class AppOpticsRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = AppOpticsConfig()
            private set

        fun config(block: AppOpticsConfig.() -> Unit) {
            config = AppOpticsConfig().apply(block)
        }

        class AppOpticsConfig : PushRegistryConfig(), IAppOpticsConfig {

            /**
             * AppOptics API token
             */
            var apiToken: String? = null

            override fun apiToken() = apiToken

            /**
             * the URI to ship metrics to
             */
            var uri: String = super.uri()

            override fun uri() = uri

            /**
             * The tag that will be mapped to `host` when shipping metrics to AppOptics.
             */
            var hostTag: String? = super.hostTag()

            override fun hostTag() = hostTag


            /**
             * @see PushRegistryConfig.batchSize
             *
             * setter throws an exception if the max value is exceeded.
             *
             * @see io.micrometer.appoptics.AppOpticsConfig.DEFAULT_BATCH_SIZE
             * @see io.micrometer.appoptics.AppOpticsConfig.MAX_BATCH_SIZE
             */
            override var batchSize = IAppOpticsConfig.DEFAULT_BATCH_SIZE
                set(value) {
                    if (value > IAppOpticsConfig.MAX_BATCH_SIZE) {
                        throw IllegalArgumentException("batch size must not be bigger then ${IAppOpticsConfig.MAX_BATCH_SIZE}")
                    } else {
                        field = value
                    }
                }

            override fun batchSize() = batchSize
        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<AppOpticsMeterRegistry, Configuration>(
            ::MicrometerMetricsAppOptics,
            ::Configuration
        )
}
