package com.edanichev.nounIcons.app.main.base

interface BasePresenter<VIEW> {

    fun setView(view: VIEW)
    fun onCreate()
}