package br.com.cucha.filmes.util

import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s immediate.
 */
class ImmediateSchedulerProvider : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.immediate()
    }

    override fun io(): Scheduler {
        return Schedulers.immediate()
    }

    override fun ui(): Scheduler {
        return Schedulers.immediate()
    }
}
