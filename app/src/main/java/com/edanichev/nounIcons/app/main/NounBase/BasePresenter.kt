package com.edanichev.nounIcons.app.main.NounBase

interface BasePresenter<VIEW>{
    fun setView(view : VIEW)

    fun onCreate()
}