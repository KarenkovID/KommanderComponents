package com.kommander.components.android_core.extensions

import com.kommander.components.domain_core.rx.RxSchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.schedulersIoToMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.schedulersIoToMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.schedulersIoToMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.schedulersIoToMain() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.schedulersIoToMain(schedulersProvider: RxSchedulersProvider) =
        subscribeOn(schedulersProvider.io()).observeOn(schedulersProvider.ui())

fun <T> Observable<T>.schedulersIoToMain(schedulersProvider: RxSchedulersProvider) =
        subscribeOn(schedulersProvider.io()).observeOn(schedulersProvider.ui())

fun <T> Flowable<T>.schedulersIoToMain(schedulersProvider: RxSchedulersProvider) =
        subscribeOn(schedulersProvider.io()).observeOn(schedulersProvider.ui())

fun Completable.schedulersIoToMain(schedulersProvider: RxSchedulersProvider) =
        subscribeOn(schedulersProvider.io()).observeOn(schedulersProvider.ui())