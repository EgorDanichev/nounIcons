package com.edanichev.nounIcons.app.main.base.data

import io.reactivex.Single

interface ReactiveCommand<RESULT> {

    fun getResultClass(): Class<RESULT>

    fun execute(): Single<RESULT>
}