package com.kommander.components.android_core.rx

import com.kommander.components.domain_core.rx.RxSchedulersProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.InjectConstructor
import javax.inject.Inject

@InjectConstructor
class RxSchedulersProviderImpl : RxSchedulersProvider {

    override fun io() = Schedulers.io()

    override fun computation() = Schedulers.computation()

    override fun ui() = AndroidSchedulers.mainThread()

    override fun newThread() = Schedulers.newThread()
}