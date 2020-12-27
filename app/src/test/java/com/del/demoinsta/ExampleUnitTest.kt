package com.del.demoinsta

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 *
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println("addition_isCorrect")
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun addition_isCorrect2() {
        println("addition_isCorrect2")

        assertEquals(4, (2 + 2).toLong())

    }

    companion object {
        private val TAG = ExampleUnitTest::class.java.simpleName + " : "

        @BeforeClass
        @kotlin.jvm.JvmStatic
        fun setUp() {
            println("setUp")
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("tearDown")
        }
    }
}