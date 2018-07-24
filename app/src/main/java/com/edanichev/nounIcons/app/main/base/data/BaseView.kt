package com.edanichev.nounIcons.app.main.base.data

import com.arellomobile.mvp.MvpView

interface BaseView<PRESENTER> : MvpView {

    var presenter: PRESENTER
}