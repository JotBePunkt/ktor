/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.metrics.micrometer

import java.time.*
import io.micrometer.core.instrument.push.PushRegistryConfig as IPushRegistryConfig


/**
 * Kotlin implementation for IPushRegistryConfig for declarative configuration
 */
abstract class PushRegistryConfig : IPushRegistryConfig {

    /**
     * The step size (reporting frequency) to use. The default is 1 minute.
     */
    var step: Duration = super.step()

    override fun step() = step

    /**
     * `true` if publishing is enabled. Default is `true`.
     */
    var enabled: Boolean = true

    override fun enabled() = enabled

    /**
     * The number of threads to use with the scheduler. The default is 2 threads.
     */
    var numTreads = super.numThreads()

    override fun numThreads() = numTreads

    /**
     * The number of measurements per request to use for the backend. If more
     * measurements are found, then multiple requests will be made. The default is
     * 10,000.
     */
    open var batchSize = super.batchSize()
    override fun batchSize() = batchSize

    // these method is not needed for this implementation
    final override fun get(key: String): String? = null
}
