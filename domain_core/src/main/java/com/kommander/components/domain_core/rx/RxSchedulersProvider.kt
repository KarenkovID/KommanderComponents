package com.kommander.components.domain_core.rx

import io.reactivex.Scheduler

interface RxSchedulersProvider {

    fun io(): Scheduler

    fun computation(): Scheduler

    fun ui(): Scheduler

    fun newThread(): Scheduler

}