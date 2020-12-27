package com.del.demoinsta.uitls.rx

import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler

/**
 *
 * TSP with TP helps to get only one kind of thread from io()/comp()/ui()
 *
 *
 * Same as [com.del.demoinsta.utils.rx.RxSchedulerProvider]
 *
 * In RxSchedulerProvider, we return actual Scheduler
 *
 * But here, we return TestScheduler.
 * |||||||||||||||||||||||||||||||||||||||
 *
 * But used for Testing
 *
 * @param testScheduler : TestScheduler, Helper class from Rx for testing in synchronous way.
 *
 */
class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler

}