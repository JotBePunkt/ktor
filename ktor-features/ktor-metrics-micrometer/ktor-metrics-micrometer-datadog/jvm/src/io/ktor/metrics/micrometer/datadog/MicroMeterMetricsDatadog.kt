/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.datadog


import io.ktor.metrics.micrometer.* 
import io.micrometer.core.instrument.*
import io.micrometer.datadog.*
import io.micrometer.datadog.DatadogConfig as IDatadogConfig

class MicrometerMetricsDatadog(config: Configuration) :
    AbstractMicrometerMetrics<DatadogMeterRegistry, MicrometerMetricsDatadog.Configuration>(config) {

    class Configuration : AbstractConfiguration<DatadogMeterRegistry>() {

        fun registry(block: DataddogRegistryConfig.() -> Unit) {
            val config = DataddogRegistryConfig().apply(block)

            registry = DatadogMeterRegistry(config.config, config.clock)
        }

    }

    class DataddogRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = DatadogConfig()
            private set

        fun config(block: DatadogConfig.() -> Unit) {
            config = DatadogConfig().apply(block)
        }

        class DatadogConfig : PushRegistryConfig(), IDatadogConfig {

            /**
             * Datadog API key
             * @see IDatadogConfig.apiKey
             */
            //TODO: Make sure there is a propper Exception when the Plugin is loaded without the api key
            lateinit var apiKey: String

            override fun apiKey() = apiKey

            /**
             * the Datadog application key
             * @see IDatadogConfig.applicationKey
             */
            var applicationKey: String? = null

            override fun applicationKey() = applicationKey

            /**
             * The tag that will be mapped to `host` when
             * shipping metrics to Datadog or `null` if
             * host should be omitted on publishing
             * @see IDatadogConfig.hostTag
             */
            var hostTag: String? = super.hostTag()

            override fun hostTag() = hostTag

            /**
             * the URI to ship metrics to
             * @see IDatadogConfig.uri
             */
            var uri: String = super.uri()

            override fun uri() = uri

            /**
             * set to false if values should not be sent to Datadog
             */
            var descriptions: Boolean = true

            override fun descriptions() = descriptions
        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<DatadogMeterRegistry, Configuration>(
            ::MicrometerMetricsDatadog,
            ::Configuration
        )
}
