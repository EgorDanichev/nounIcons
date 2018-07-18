package com.edanichev.nounIcons.app.main.Utils.Network.rx

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class SimpleSingleObserver<T>(
    block: (SimpleSingleObserver<T>.() -> Unit)? = null
) : SingleObserver<T> {

    var onComplete: ((T) -> Unit)? = null
    var onSubscribe: ((Disposable) -> Unit)? = null
    var onError: ((throwable: Throwable) -> Unit)? = null

    init {
        block?.invoke(this)
    }

    override fun onSuccess(result: T) {
        onComplete?.invoke(result)
    }

    override fun onSubscribe(disposable: Disposable) {
        onSubscribe?.invoke(disposable)
    }

    override fun onError(throwable: Throwable) {
        onError?.invoke(throwable)
    }
}