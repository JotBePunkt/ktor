/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.dynatrace



import io.ktor.metrics.micrometer.*
import io.micrometer.dynatrace.*
import io.micrometer.core.instrument.*
import io.micrometer.dynatrace.DynatraceConfig as IDynatraceConfig

class MicrometerMetricsDynatrace(config: Configuration) :
    AbstractMicrometerMetrics<DynatraceMeterRegistry, MicrometerMetricsDynatrace.Configuration>(config) {

    class Configuration : AbstractConfiguration<DynatraceMeterRegistry>() {

        fun registry(block: DynatraceRegistryConfig.() -> Unit) {
            val config = DynatraceRegistryConfig().apply(block)

            registry = DynatraceMeterRegistry(config.config, config.clock)
        }

    }

    class DynatraceRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = DynatraceConfig()
            private set

        fun config(block: DynatraceConfig.() -> Unit) {
            config = DynatraceConfig().apply(block)
        }

        class DynatraceConfig : PushRegistryConfig(), IDynatraceConfig {

            /**
             * Dynatrace API token
             */
            // TODO make sure the plugin throws a helpful exception when this manatory info is not set
            lateinit var apiToken: String

            override fun apiToken() = apiToken

            /**
             * the URI to ship metrics to
             *
             */
            // TODO make sure the plugin throws a helpful exception when this manatory info is not set

            lateinit var uri: String

            override fun uri() = uri

            /**
             * @see IDynatraceConfig.deviceId
             */
            var deviceId: String? = super.deviceId()

            override fun deviceId() = deviceId

            /**
             * @see IDynatraceConfig.technologyType
             */
            var technologyType: String = "kotlin"
            override fun technologyType() = technologyType

        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<DynatraceMeterRegistry, Configuration>(
            ::MicrometerMetricsDynatrace,
            ::Configuration
        )
}
