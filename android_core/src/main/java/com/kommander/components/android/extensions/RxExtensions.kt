package com.kommander.components.android.extensions

import com.kommander.components.domain.rx.RxSchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
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

fun <T : Any> T?.toMaybe(): Maybe<T> = if (this == null) Maybe.empty() else Maybe.just(this)

fun <T : Any> single(block: () -> T): Single<T> = Single.fromCallable(block)

fun completable(block: () -> Unit): Completable = Completable.fromAction(block)