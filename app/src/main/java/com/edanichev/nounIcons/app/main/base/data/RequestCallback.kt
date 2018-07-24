package ru.alfabank.mobile.android.core.domain.network.callback

open class RequestCallback<Result>() {

    protected var successAction: ((Result) -> Unit)? = null
    private var errorAction: ((Throwable) -> Unit)? = null

    open fun setError(errorAction: ((Throwable) -> Unit)?): RequestCallback<Result> {
        this.errorAction = errorAction
        return this
    }

    open  fun setSuccess(successAction: ((Result) -> Unit)?): RequestCallback<Result> {
        this.successAction = successAction
        return this
    }

    open fun onRequestSuccess(result: Result) {
        successAction?.invoke(result)
    }

    open fun onRequestFailure(throwable: Throwable) {
        errorAction?.invoke(throwable)
    }
}
