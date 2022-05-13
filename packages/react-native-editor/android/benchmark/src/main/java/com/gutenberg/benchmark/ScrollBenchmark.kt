package com.gutenberg.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.*
import junit.framework.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollToEndFrameMetrics() = benchmarkRule.measureRepeated(
        packageName = "com.gutenberg",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        // Ensure app is done starting up so that we're only testing scroll performance.
        // This might be unnecessary.
        sleep(2000)

        scrollToEnd(device)
    }

    // Based on UiScrollable's flingToEnd and scrollToEnd functions. Difference is that this
    // adds a short sleep between flings to give time to load additional content in the scrollable.
    // Without this scrollable.flingToEnd() would think it got to the end before actually getting
    // to the end.
    private fun scrollToEnd(device: UiDevice, maxSwipes: Int = 10, steps: Int = 5) {

        val scrollable = UiScrollable(UiSelector().scrollable(true))
        Tracer.trace(maxSwipes, steps)

        for (x in 0 until maxSwipes) {
            if (!scrollable.scrollForward(steps)) {
                break
            }

            // Give the app time to load more of the scrollable view
            sleep(3000)
        }

        val lastBlock = device.findObject(By.text(BuildConfig.BENCHMARK_LAST_BLOCK_TEXT))
        if (lastBlock == null) {
            fail("Did not scroll until the last block was on screen")
        }
    }
}
