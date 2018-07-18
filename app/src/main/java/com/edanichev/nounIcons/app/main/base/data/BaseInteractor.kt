package com.edanichev.nounIcons.app.main.base.data

import io.reactivex.disposables.CompositeDisposable

open class BaseInteractor(
    val compositeDisposable: CompositeDisposable
) {

    fun onDestroy() {
        compositeDisposable.clear()
    }
}