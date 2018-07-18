package ru.alfabank.mobile.android.core.domain.network.callback

open class RequestCallback<Result>() {

    protected var successAction: ((Result) -> Unit)? = null
    private var errorAction: ((Throwable) -> Unit)? = null

    fun setError(errorAction: ((Throwable) -> Unit)?): RequestCallback<Result> {
        this.errorAction = errorAction
        return this
    }

    fun setSuccess(successAction: ((Result) -> Unit)?): RequestCallback<Result> {
        this.successAction = successAction
        return this
    }

    fun onRequestSuccess(result: Result) {
        successAction?.invoke(result)
    }

    fun onRequestFailure(throwable: Throwable) {
        errorAction?.invoke(throwable)
    }
}
