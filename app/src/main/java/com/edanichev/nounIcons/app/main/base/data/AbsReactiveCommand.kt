package com.edanichev.nounIcons.app.main.base.data

abstract class AbsReactiveCommand<RESULT>
constructor(
    private val resultClass: Class<RESULT>
) : ReactiveCommand<RESULT> {

    override fun getResultClass() = resultClass
}