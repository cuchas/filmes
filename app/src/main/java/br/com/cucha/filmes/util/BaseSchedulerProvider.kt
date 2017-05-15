package br.com.cucha.filmes.util

import rx.Scheduler

/**
 * Allow providing different types of [Scheduler]s.
 */
interface BaseSchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}
