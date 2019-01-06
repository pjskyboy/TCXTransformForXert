package com.freesundance

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class TestCoroutinesBasics {


    val fibonacciSeq = sequence {
        var a = 0
        var b = 1

        yield(1)
        while (true) {
            var tmp = a + b
            println(tmp)
            yield(tmp)
            a = b
            b = tmp
        }
    }

    suspend fun expensiveComputation(res: MutableList<String>) {
        delay(1000L)
        res.add("word!")
    }

    @Test
    fun givenAsyncCoroutine_whenStartIt_thenShouldExecuteItInTheAsyncWay() {
        // given
        val res = mutableListOf<String>()

        // when
        runBlocking<Unit> {
            val promise = launch(CommonPool) {
                expensiveComputation(res)
            }
            res.add("Hello,")
            promise.join()
        }

        // then
        assertEquals(res, listOf("Hello,", "word!"))
    }

    @Test
    fun givenHugeAmountOfCoroutines_whenStartIt_thenShouldExecuteItWithoutOutOfMemory() {
        runBlocking<Unit> {
            // given
            val counter = AtomicInteger(0)
            val numberOfCoroutines = 100_000

            // when
            val jobs = List(numberOfCoroutines) {
                launch(CommonPool) {
                    delay(1000L)
                    counter.incrementAndGet()
                }
            }
            jobs.forEach { it.join() }

            // then
            assertEquals(counter.get(), numberOfCoroutines)
        }
    }

    @Test
    fun testFibonacciSequence() {
        val res = fibonacciSeq
                .take(5)
                .toList()

        assertEquals(res, listOf(1, 1, 2, 3, 5))
    }
}