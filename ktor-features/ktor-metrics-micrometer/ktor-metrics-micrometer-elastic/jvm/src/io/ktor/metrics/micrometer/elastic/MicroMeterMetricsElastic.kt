/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.elastic


import io.ktor.metrics.micrometer.*
import io.micrometer.core.instrument.*
import io.micrometer.elastic.*
import io.micrometer.elastic.ElasticConfig as IElasticConfig

class MicrometerMetricsElastic(config: Configuration) :
    AbstractMicrometerMetrics<ElasticMeterRegistry, MicrometerMetricsElastic.Configuration>(config) {

    class Configuration : AbstractConfiguration<ElasticMeterRegistry>() {

        fun registry(block: ElasticRegistryConfig.() -> Unit) {
            val config = ElasticRegistryConfig().apply(block)

            registry = ElasticMeterRegistry(config.config, config.clock)
        }

    }

    class ElasticRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = ElasticConfig()
            private set

        fun config(block: ElasticConfig.() -> Unit) {
            config = ElasticConfig().apply(block)
        }

        class ElasticConfig : PushRegistryConfig(), IElasticConfig {

            /**
             * he host to send metrics to
             */
            var host = super.host()

            override fun host() = host

            /**
             * The index name to write metrics to
             * default is "metrics"
             */
            var index = super.index()
            override fun index() = index

            /**
             * The inddex date format used for rolleing indices
             * @see IElasticConfig.indexDateFormat
             */
            var indexDateFormat = super.indexDateFormat()
            override fun indexDateFormat() = indexDateFormat

        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<ElasticMeterRegistry, Configuration>(
            ::MicrometerMetricsElastic,
            ::Configuration
        )
}
