package com.del.demoinsta.utils.rx

import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Singleton

/**
 *
 *
 * The purpose is to have an abstraction rather than locked in single library.
 *
 * and provide someMethod who gives us threads
 * I am not concerned how they are creating and which library creating them.
 *
 * [RxSchedulerProvider] is a concrete class, who has provided the implementation
 *
 * In future, you can use Coroutine/Other to provide the implementation.
 *
 * - Right now, methods are returning Scheduler based on Rx, we can this later.
 */
@Singleton
interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}