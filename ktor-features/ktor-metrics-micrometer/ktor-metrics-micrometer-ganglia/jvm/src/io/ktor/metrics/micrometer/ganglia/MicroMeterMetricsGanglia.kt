/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer.ganglia

import io.ktor.metrics.micrometer.*
import io.micrometer.core.instrument.*
import io.micrometer.ganglia.*
import io.micrometer.ganglia.GangliaConfig as IGangliaConfig

class MicrometerMetricsGanglia(config: Configuration) :
    AbstractMicrometerMetrics<GangliaMeterRegistry, MicrometerMetricsGanglia.Configuration>(config) {

    class Configuration : AbstractConfiguration<GangliaMeterRegistry>() {

        fun registry(block: GangliaRegistryConfig.() -> Unit) {
            val config = GangliaRegistryConfig().apply(block)

            registry = GangliaMeterRegistry(config.config, config.clock)
        }

    }

    class GangliaRegistryConfig {

        var clock: Clock = Clock.SYSTEM

        var config = GangliaConfig()
            private set

        fun config(block: GangliaConfig.() -> Unit) {
            config = GangliaConfig().apply(block)
        }

        class GangliaConfig : PushRegistryConfig(), IGangliaConfig {

            var rateUnits = super.rateUnits()
            override fun rateUnits() = rateUnits

            var durationUnits = super.durationUnits()
            override fun durationUnits() = durationUnits

            var protocolVersion = super.protocolVersion()
                set(value) {
                    if (value == "3.1" || value == "3.0") {
                        field = value
                    } else {
                        throw IllegalArgumentException("Ganglia version must me 3.0 or 3.1")
                    }
                }

            override fun protocolVersion() = protocolVersion

            var addressingMode = super.addressingMode()
            override fun addressingMode() = addressingMode

            var ttl = super.ttl()
            override fun ttl() = ttl

            var host: String = super.host()
            override fun host() = host

            var port = super.port()
            override fun port() = port

            override fun enabled() = super<PushRegistryConfig>.enabled()

        }
    }

    companion object AppMetricsFeature :
        AbstractMetricsFeature<GangliaMeterRegistry, Configuration>(
            ::MicrometerMetricsGanglia,
            ::Configuration
        )
}
