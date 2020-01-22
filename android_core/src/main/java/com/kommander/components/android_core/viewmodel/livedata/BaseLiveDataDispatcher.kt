package com.kommander.components.android_core.viewmodel.livedata

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Flowable<out T>.dispatchTo(liveData: MutableLiveData<ContentEvent<T>>): Disposable {
    liveData.value = ContentEvent.Loading(liveData.value?.data)
    return this
            .subscribe(
                    { data -> liveData.value = ContentEvent.Success(data) },
                    { throwable -> liveData.value = ContentEvent.Error(throwable, liveData.value?.data) },
                    { liveData.value = ContentEvent.Complete(liveData.value?.data) }
            )
}

fun <T> Observable<out T>.dispatchTo(liveData: MutableLiveData<ContentEvent<T>>): Disposable {
    liveData.value = ContentEvent.Loading(liveData.value?.data)
    return this
            .subscribe(
                    { data -> liveData.value = ContentEvent.Success(data) },
                    { throwable -> liveData.value = ContentEvent.Error(throwable, liveData.value?.data) },
                    { liveData.value = ContentEvent.Complete(liveData.value?.data) }
            )
}

fun <T> Single<out T>.dispatchTo(liveData: MutableLiveData<ContentEvent<T>>): Disposable {
    liveData.value = ContentEvent.Loading(liveData.value?.data)
    return this
            .subscribe(
                    { data -> liveData.value = ContentEvent.Success(data) },
                    { throwable -> liveData.value = ContentEvent.Error(throwable, liveData.value?.data) }
            )
}

fun <T> Maybe<out T>.dispatchTo(liveData: MutableLiveData<ContentEvent<T>>): Disposable {
    liveData.value = ContentEvent.Loading(liveData.value?.data)
    return this
            .subscribe(
                    { data -> liveData.value = ContentEvent.Success(data) },
                    { throwable -> liveData.value = ContentEvent.Error(throwable, liveData.value?.data) },
                    { liveData.value = ContentEvent.Complete(liveData.value?.data) }
            )
}

fun <T> Completable.dispatchTo(liveData: MutableLiveData<Event>): Disposable {
    liveData.value = Event.Loading
    return this
            .doOnComplete { liveData.value = Event.Complete }
            .doOnError { throwable -> liveData.value = Event.Error(throwable) }
            .subscribe(
                    { liveData.value = Event.Complete },
                    { throwable -> liveData.value = Event.Error(throwable) }
            )
}